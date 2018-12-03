package net.ezplanet.shopping.util;

import net.ezplanet.shopping.data.CheckoutRepository;
import net.ezplanet.shopping.data.OfferRepository;
import net.ezplanet.shopping.data.ProductRepository;
import net.ezplanet.shopping.data.TrolleyRepository;
import net.ezplanet.shopping.entity.CheckoutItem;
import net.ezplanet.shopping.entity.Offer;
import net.ezplanet.shopping.entity.Product;
import net.ezplanet.shopping.entity.TrolleyItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.lang.Math.abs;

@Service
public class CheckoutTrolley implements Checkout {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    TrolleyRepository trolleyRepository;

    @Autowired
    CheckoutRepository checkoutRepository;

    @Override
    public void applyOffers() {
        List<Offer> offers = offerRepository.findAll();
        int totalQuantity;
        int discount;
        System.out.println("=== Applying offers ====");
        for (Offer offer : offers) {
            List<CheckoutItem> checkoutItems = checkoutRepository.findByOfferOrderOfferPriceAsc(offer.getCode());
            totalQuantity = 0;
            for (CheckoutItem checkoutItem: checkoutItems) {
                System.out.println("Item: " + checkoutItem.getItem() + " - Offer: " + checkoutItem.getOffer() +
                        " - Quantity: " + checkoutItem.getQuantity());
                totalQuantity = totalQuantity + checkoutItem.getQuantity();
            }
            discount = (totalQuantity / offer.getThreshold()) * offer.getFree();
            //System.out.println("Offer: " + offer.getCode() + " - Discount: " + discount);
            for (CheckoutItem checkoutItem: checkoutItems) {
                if (checkoutItem.getQuantity() < discount) {
                    checkoutItem.setDiscount(checkoutItem.getQuantity());
                    discount = discount - checkoutItem.getQuantity();
                } else {
                    checkoutItem.setDiscount(discount);
                    discount = 0;
                }
                checkoutRepository.save(checkoutItem);

                // No more discounts left to apply
                if (discount <= 0) break;
            }
        }
        System.out.println("========================");

    }

    @Override
    public void checkoutItems() {

        Product product;

        List<TrolleyItem> trolleyItem = trolleyRepository.findAllOrderByOfferPriceAsc();
        System.out.println("=== Checkout ITEMS List ====");
        for (TrolleyItem item : trolleyItem) {
            Optional<Product> optionalProduct = productRepository.findById(item.getItem());
            if (optionalProduct.isPresent()) {
                product = optionalProduct.get();
                checkoutRepository.save(new CheckoutItem(item.getItem(),
                        product.getOffer(), item.getQuantity(), 0, product.getPrice()));

                System.out.println("Item: " + item.getItem() + " - Offer: " + product.getOffer() +
                        " - Quantity: " + item.getQuantity() + " - Price: " + product.getPrice());

            }

        }
        System.out.println("============================");
    }

    @Override
    public BigDecimal getCheckoutTotal() {

        BigDecimal checkoutTotal = BigDecimal.valueOf(0);

        System.out.println("=== Checkout TOTAL ====");
        List<CheckoutItem> checkoutItems = checkoutRepository.findAll();
        for (CheckoutItem checkoutItem: checkoutItems) {
            System.out.println("Item: " + checkoutItem.getItem() + " - Offer: " + checkoutItem.getOffer() +
                    " - Quantity: " + checkoutItem.getQuantity() + " - Discount: " + checkoutItem.getDiscount());

            checkoutTotal = checkoutTotal.add(BigDecimal.valueOf
                    (checkoutItem.getQuantity() - checkoutItem.getDiscount()).multiply(checkoutItem.getPrice()));
        }
        System.out.println("============================");
        checkoutRepository.deleteAll();
        return checkoutTotal;
    }
}
