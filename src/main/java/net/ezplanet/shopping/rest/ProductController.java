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
import net.ezplanet.shopping.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (value = "/product")
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OfferRepository offerRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@RequestParam(value = "name") String name) {
        LOG.debug("listing Products");

        return productRepository.getOne(name);
    }

    @GetMapping(value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> listProducts() {
        LOG.debug("listing rroducts");

        return productRepository.findAll();
    }

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> updateProduct(@RequestBody Product product) {
        LOG.debug("add new product: {}", product.getName());

        product.setName(product.getName().toLowerCase());
        productRepository.save(product);
        return productRepository.findAll();
    }


}
