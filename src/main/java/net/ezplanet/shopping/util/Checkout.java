package net.ezplanet.shopping.util;

import java.math.BigDecimal;

public interface Checkout {

    void checkoutItems();

    void applyOffers();

    BigDecimal getCheckoutTotal();
}
