package net.ezplanet.shopping.data;

import net.ezplanet.shopping.entity.Offer;
import net.ezplanet.shopping.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestDataInizializer implements ApplicationRunner {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OfferRepository offerRepository;

    @Override
    public void run(ApplicationArguments args) {
        /**
        productRepository.save(Product.builder()
                .withName("Apple")
                .withPrice(BigDecimal.valueOf(0.60))
                .build()
        );
        **/
        productRepository.save(new Product("apple", "A11", BigDecimal.valueOf(0.60)));
        productRepository.save(new Product("orange","A32", BigDecimal.valueOf(0.25)));
        productRepository.save(new Product("banana","A11", BigDecimal.valueOf(0.20)));

        offerRepository.save(new Offer("A11", 2, 1));
        offerRepository.save(new Offer("A32", 3, 1));
    }
}
