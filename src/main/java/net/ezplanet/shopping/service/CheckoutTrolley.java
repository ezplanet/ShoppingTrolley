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

/**
 * <h1>Checkout Trolley</h1>
 * The CheckoutTrolley class implements 3 methods necessary
 * to put the items in the checkout area, count them, add price
 * and offers and finally apply offers
 * <p>
 *
 * @author  Mauro Mozzarelli
 * @version 1.0
 * @since   2018-12-03
 */
package net.ezplanet.shopping.service;

import net.ezplanet.shopping.data.CheckoutRepository;
import net.ezplanet.shopping.data.OfferRepository;
import net.ezplanet.shopping.data.ProductRepository;
import net.ezplanet.shopping.data.TrolleyRepository;
import net.ezplanet.shopping.entity.CheckoutItem;
import net.ezplanet.shopping.entity.Offer;
import net.ezplanet.shopping.entity.Product;
import net.ezplanet.shopping.entity.TrolleyItem;
import net.ezplanet.shopping.rest.CheckoutController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CheckoutTrolley implements Checkout {
    private static final Logger LOG = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    TrolleyRepository trolleyRepository;

    @Autowired
    CheckoutRepository checkoutRepository;

    /**
     * This method is used to take the items from the trolley into
     * the checkout repository, count them, and augment the data with
     * offers and price
     *
     * @return void.
     */
    @Override
    public void checkoutItems() {

        Product product;

        List<TrolleyItem> trolleyItem = trolleyRepository.findAllOrderByOfferPriceAsc();
        LOG.debug("=== Checkout ITEMS List ====");
        for (TrolleyItem item : trolleyItem) {
            Optional<Product> optionalProduct = productRepository.findById(item.getItem());
            if (optionalProduct.isPresent()) {
                product = optionalProduct.get();
                checkoutRepository.save(new CheckoutItem(item.getItem(),
                        product.getOffer(), item.getQuantity(), 0, product.getPrice()));

                LOG.debug("Item: {} - Offer: {} - Quantity: {} - Price: {}", item.getItem(), product.getOffer(),
                        item.getQuantity(), product.getPrice());
            }

        }
        LOG.debug("============================");
    }

    /**
     * This method is used to apply offers to the items in the checkout
     * repository depending on the parameters associated with each offer
     * If two items have the same offer code, the item with the lowest price
     * is offered free first.
     *
     * @return void.
     */
    @Override
    public void applyOffers() {
        List<Offer> offers = offerRepository.findAll();
        int totalQuantity;
        int discount;
        LOG.debug("=== Applying offers ====");
        for (Offer offer : offers) {
            List<CheckoutItem> checkoutItems = checkoutRepository.findByOfferOrderOfferPriceAsc(offer.getCode());
            totalQuantity = 0;
            for (CheckoutItem checkoutItem: checkoutItems) {
                LOG.debug("Item: {} - Offer: {} - Quantity: {}", checkoutItem.getItem(),
                        checkoutItem.getOffer(), checkoutItem.getQuantity());
                totalQuantity = totalQuantity + checkoutItem.getQuantity();
            }
            discount = (totalQuantity / offer.getThreshold()) * offer.getFree();
            LOG.trace("Offer: {} - Discount: {}", offer.getCode(), discount);
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
        LOG.debug("========================");

    }

    /**
     * This method calculates the total amount from the items in the
     * checkout repository
     *
     * @return BigDecimal this is the total amount due calculated from the checkout items.
     */
    @Override
    public BigDecimal getCheckoutTotal() {

        BigDecimal checkoutTotal = BigDecimal.valueOf(0);

        LOG.debug("=== Checkout TOTAL ====");
        List<CheckoutItem> checkoutItems = checkoutRepository.findAll();
        for (CheckoutItem checkoutItem: checkoutItems) {
            LOG.debug("Item: {} - Offer: {} - Quantity: {} - Discount: {}", checkoutItem.getItem(),
                    checkoutItem.getOffer(), checkoutItem.getQuantity(), checkoutItem.getDiscount());

            checkoutTotal = checkoutTotal.add(BigDecimal.valueOf
                    ((long) checkoutItem.getQuantity() - checkoutItem.getDiscount()).multiply(checkoutItem.getPrice()));
        }
        LOG.debug("============================");
        // Empties the checkout area (repository), ready for the next checkout request.
        checkoutRepository.deleteAll();
        return checkoutTotal;
    }
}
