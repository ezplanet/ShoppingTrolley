/*
 * (C) Copyright ${year} Mauro Mozzarelli.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     ...
 */
package net.ezplanet.shopping.rest;


import net.ezplanet.shopping.data.OfferRepository;
import net.ezplanet.shopping.data.ProductRepository;
import net.ezplanet.shopping.data.TrolleyRepository;
import net.ezplanet.shopping.entity.Offer;
import net.ezplanet.shopping.entity.Product;
import net.ezplanet.shopping.entity.TrolleyItem;
import net.ezplanet.shopping.util.Checkout;
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

    @Autowired
    Checkout checkout;

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


    @GetMapping(value = "/session")
    public String checkoutItemsWithOffers(@RequestHeader(value="Trolley-Code") String trolleyCode,
                                                     @RequestParam("items") List<String> items) {
        System.out.println("Computing shopping cart total..." + trolleyCode);

        TrolleyItem trolleyItem;

        for (String item : items) {
            item = item.toLowerCase();
            Optional<TrolleyItem> optTrolley = trolleyRepository.findById(item); // returns java8 optional
            if (optTrolley.isPresent()) {
                trolleyItem = optTrolley.get();
                //System.out.println("TrolleyItem item: " + item + " quantity: " + trolleyItem.getQuantity());
                trolleyItem.setQuantity(trolleyItem.getQuantity() + 1);
                trolleyRepository.save(trolleyItem);
            } else {
                //System.out.println("TrolleyItem item: " + item + " not found");
                trolleyItem = new TrolleyItem(item, 1);
            }
            System.out.println("Scanning trolley item: " + trolleyItem.getItem() + " - total quantity: " + trolleyItem.getQuantity());
            trolleyRepository.save(trolleyItem);
        }
        checkout.checkoutItems();
        checkout.applyOffers();
        return ("Checkout Total: " + checkout.getCheckoutTotal());
    }
}
