package net.ezplanet.shopping.rest;


import net.ezplanet.shopping.data.OfferRepository;
import net.ezplanet.shopping.entity.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (value = "/offer")
public class OfferController {

    @Autowired
    OfferRepository offerRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Offer getProduct(@RequestParam(value = "code") String code) {
        System.out.println("get offer with code: " + code);

        return offerRepository.getOne(code);
    }

    @GetMapping(value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Offer> listOffers() {
        System.out.println("listing offers");

        return offerRepository.findAll();
    }

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Offer> updateProduct(@RequestBody Offer offer) {
        System.out.println("add new offer: " + offer.getCode());

        offerRepository.save(offer);
        return offerRepository.findAll();
    }


}
