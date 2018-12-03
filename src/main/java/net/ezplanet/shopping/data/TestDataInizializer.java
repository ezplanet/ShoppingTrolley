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
        productRepository.save(new Product("melon", "B32", BigDecimal.valueOf(1.00)));

        offerRepository.save(new Offer("A11", 2, 1));
        offerRepository.save(new Offer("A32", 3, 1));
        offerRepository.save(new Offer("B32", 3, 1));
    }
}
