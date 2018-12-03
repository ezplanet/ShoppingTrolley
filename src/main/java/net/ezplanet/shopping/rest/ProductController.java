package net.ezplanet.shopping.rest;


import net.ezplanet.shopping.data.OfferRepository;
import net.ezplanet.shopping.data.ProductRepository;
import net.ezplanet.shopping.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (value = "/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OfferRepository offerRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@RequestParam(value = "name") String name) {
        System.out.println("listing Products");

        return productRepository.getOne(name);
    }

    @GetMapping(value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> listProducts() {
        System.out.println("listing rroducts");

        return productRepository.findAll();
    }

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> updateProduct(@RequestBody Product product) {
        System.out.println("add new product: " + product.getName());

        product.setName(product.getName().toLowerCase());
        productRepository.save(product);
        return productRepository.findAll();
    }


}
