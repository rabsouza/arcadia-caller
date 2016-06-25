package br.com.battista.arcadia.caller.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.SaveException;

import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.model.Offer;
import br.com.battista.arcadia.caller.model.Vendor;
import br.com.battista.arcadia.caller.utils.MergeBean;


@Controller
@RequestMapping("api/v1/offer/")
public class OfferController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Offer>> getAll() {

        LOGGER.info("Retrieve all offers!");
        List<Offer> offers = findAll();

        if (offers == null || offers.isEmpty()) {
            LOGGER.debug("No offers founds!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            for (Offer offer : offers) {
                offer.setVendors(findVendorByCodeProduct(offer.getCodeProduct()));
            }

            LOGGER.debug("Found {} offers!", offers.size());
            return buildResponseSuccess(offers, HttpStatus.OK);
        }
    }

    private List<Offer> findAll() {
        Objectify objectify = ObjectifyService.ofy();
        objectify.flush();
        return objectify
                       .load()
                       .type(Offer.class)
                       .order("-updatedAt")
                       .list();
    }

    @RequestMapping(value = "/revise/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Offer>> getAllRevised() {

        LOGGER.info("Retrieve all offers with pending revise!");
        List<Offer> offers = findAllRevised();

        if (offers == null || offers.isEmpty()) {
            LOGGER.debug("No offers founds with pending revise!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            for (Offer offer : offers) {
                offer.setVendors(null);
            }

            LOGGER.debug("Found {} offers with pending revise!", offers.size());
            return buildResponseSuccess(offers, HttpStatus.OK);
        }
    }

    private List<Offer> findAllRevised() {
        Objectify objectify = ObjectifyService.ofy();
        objectify.flush();
        return objectify
                       .load()
                       .type(Offer.class)
                       .filter("revise", true)
                       .order("-updatedAt")
                       .list();
    }

    @RequestMapping(value = "/product/{codeProduct}", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<Offer> getByCodeProduct(@PathVariable("codeProduct") Long codeProduct) {

        LOGGER.info("Retrieve offer by code product:{}!", codeProduct);
        Offer offer = findByCodeProduct(codeProduct);

        if (offer == null) {
            LOGGER.debug("No offer found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            offer.setVendors(findVendorByCodeProduct(offer.getCodeProduct()));

            LOGGER.debug("Found offer with id {}!", offer.getId());
            return buildResponseSuccess(offer, HttpStatus.OK);
        }
    }

    private List<Vendor> findVendorByCodeProduct(Long codeProduct) {
        return ObjectifyService.ofy()
                       .load()
                       .type(Vendor.class)
                       .filter("codeProduct", codeProduct)
                       .order("-updatedAt")
                       .list();
    }

    private Offer findByCodeProduct(Long codeProduct) {
        return ObjectifyService.ofy()
                       .load()
                       .type(Offer.class)
                       .filter("codeProduct", codeProduct)
                       .first()
                       .now();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Offer> save(@RequestBody Offer offer) {

        if (offer == null || offer.getCodeProduct() == null) {
            LOGGER.debug("Offer can not be null!");
            return buildResponseErro(HttpStatus.NOT_MODIFIED);
        }

        LOGGER.info("Save to offer[{}]!", offer);
        try {
            long codeProduct = offer.getCodeProduct();
            Offer offerEntity = findByCodeProduct(codeProduct);
            if (offerEntity != null) {
                try {
                    if (!offerEntity.getVersion().equals(offer.getVersion())) {
                        String cause = "There is already an offer object registered with different version!" +
                                               " Please update your object!";
                        LOGGER.debug(cause);
                        return buildResponseErro(cause);
                    }

                    offer = mergeEntity(offer, offerEntity);
                } catch (Exception e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                    return buildResponseErro(HttpStatus.NOT_MODIFIED);
                }
            } else {
                offer.initEntity();
            }

            saveEntity(offer);
            offer.setVendors(findVendorByCodeProduct(offer.getCodeProduct()));
            LOGGER.debug("Save offer and generate to id: {}!", offer.getId());
            return buildResponseSuccess(offer, HttpStatus.OK);
        } catch (SaveException ex) {
            LOGGER.debug("Error to save offer, cause:{}!", ex.getLocalizedMessage());
            return buildResponseErro(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity update(@RequestBody Offer offer) {

        if (offer == null || offer.getCodeProduct() == null) {
            LOGGER.debug("Offer can not be null!");
            return buildResponseErro(HttpStatus.NOT_MODIFIED);
        }

        LOGGER.info("Update to offer[{}]!", offer);
        try {
            long codeProduct = offer.getCodeProduct();
            Offer offerEntity = findByCodeProduct(codeProduct);
            if (offerEntity != null) {
                try {
                    if (!offerEntity.getVersion().equals(offer.getVersion())) {
                        String cause = "There is already an offer object registered with different version!" +
                                               " Please update your object!";
                        LOGGER.debug(cause);
                        return buildResponseErro(cause);
                    }

                    offer = mergeEntity(offer, offerEntity);
                } catch (Exception e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                    return buildResponseErro(HttpStatus.NOT_MODIFIED);
                }
            } else {
                offer.initEntity();
            }

            saveEntity(offer);
            offer.setVendors(findVendorByCodeProduct(offer.getCodeProduct()));
            LOGGER.debug("update offer with id: {}!", offer.getId());
            return buildResponseSuccess(HttpStatus.OK);
        } catch (SaveException ex) {
            LOGGER.debug("Error to update offer, cause:{}!", ex.getLocalizedMessage());
            return buildResponseErro(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private Offer mergeEntity(@RequestBody Offer offer, Offer offerEntity) throws Exception {
        new MergeBean().merge(offerEntity, offer);
        offer = offerEntity;
        offer.updateEntity();
        return offer;
    }

    private void saveEntity(@RequestBody Offer offer) {
        ObjectifyService.ofy()
                .save()
                .entity(offer)
                .now();
    }

}
