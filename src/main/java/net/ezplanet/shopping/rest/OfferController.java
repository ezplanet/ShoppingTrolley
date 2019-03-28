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
import net.ezplanet.shopping.entity.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (value = "/offer")
public class OfferController {
    private static final Logger LOG = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    OfferRepository offerRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Offer getProduct(@RequestParam(value = "code") String code) {
        LOG.debug("get offer with code: {}", code);

        return offerRepository.getOne(code);
    }

    @GetMapping(value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Offer> listOffers() {
        LOG.debug("listing offers");

        return offerRepository.findAll();
    }

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Offer> updateProduct(@RequestBody Offer offer) {
        LOG.debug("add new offer: {}", offer.getCode());

        offerRepository.save(offer);
        return offerRepository.findAll();
    }


}
