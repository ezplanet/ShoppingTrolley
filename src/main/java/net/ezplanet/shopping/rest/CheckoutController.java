package net.ezplanet.shopping.rest;


import net.ezplanet.shopping.data.OfferRepository;
import net.ezplanet.shopping.data.ProductRepository;
import net.ezplanet.shopping.data.TrolleyRepository;
import net.ezplanet.shopping.entity.Offer;
import net.ezplanet.shopping.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping (value = "/checkout")
public class CheckoutController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    TrolleyRepository trolleyRepository;

    @GetMapping("/echo_list")
    public String listItems (@RequestParam(value = "items") String items) {
        System.out.println("hello Mauro!");
        return "Items list: " + items;
    }

    @GetMapping("/clear")
    public String clearTrolley () {
        trolleyRepository.deleteAll();
        return "Trolley cleared";
    }

    @GetMapping(value = "/simple",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public BigDecimal checkoutTotal(@RequestParam("items") List<String> items) {
        System.out.println("Computing shopping cart total...");

        BigDecimal cartTotal = BigDecimal.valueOf(0);
        BigDecimal price;
        for (String item : items) {
            try {
                price = productRepository.getOne(item.toLowerCase()).getPrice();
                cartTotal = cartTotal.add(price);
                System.out.println("Item: " + item + " - Price: " + price.toString() + " - Cart Total: "  + cartTotal.toString());
            } catch (Exception EntityNotFoundException) {
                System.out.println("Item: " + item + " not available. Ignored.");
            }
        }
        return cartTotal;
    }


    @GetMapping(value = "/simple_offers")
    public BigDecimal checkoutWithOffers(@RequestHeader(value="Trolley-Code") String trolleyCode,
                                         @RequestParam("items") List<String> items) {
        System.out.println("Computing shopping cart total..." + trolleyCode);

        BigDecimal cartTotal = BigDecimal.valueOf(0);
        BigDecimal price;
        Map<String, Integer> itemsMap = new HashMap<String, Integer>();

        for (String item : items) {
            try {
                int count;
                if (itemsMap.containsKey(item.toLowerCase()))
                    count = itemsMap.get(item.toLowerCase()) + 1;
                 else
                    count = 1;

                itemsMap.put(item.toLowerCase(), Integer.valueOf(count));
                price = productRepository.getOne(item.toLowerCase()).getPrice();
                cartTotal = cartTotal.add(price);
                System.out.println("Item: " + item + " - Price: " + price + " - Cart Total: "  + cartTotal);
            } catch (EntityNotFoundException e) {
                System.out.println("Item: " + item + " not available. Ignored.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Iterator it = itemsMap.entrySet().iterator();

        Offer offer;
        Product product;
        int discount;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println("Total " + pair.getKey() + "s = " + pair.getValue());
            try {
                product = productRepository.getOne(pair.getKey().toString());
                offer = offerRepository.getOne(product.getOffer());
                discount = ((int) pair.getValue() / offer.getThreshold()) * offer.getFree();
                System.out.println("Product: " + product.getName() + " - Discount: " + discount);
                price = product.getPrice();
                cartTotal = cartTotal.subtract(price.multiply(BigDecimal.valueOf(discount)));
                System.out.println("Product: " + product.getName() + " - Price: " + price + " - Cart Total: "  + cartTotal);

            } catch (EntityNotFoundException e) {
                System.out.println("Product: " + pair.getKey().toString() + " no offer available.");
            }

        }
        return cartTotal;
    }
}