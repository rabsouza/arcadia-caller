package br.com.battista.arcadia.caller.controller;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
import br.com.battista.arcadia.caller.model.Vendor;
import br.com.battista.arcadia.caller.utils.MergeBean;


@Controller
@RequestMapping("api/v1/vendor/")
public class VendorController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendorController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Vendor>> getAll() {

        LOGGER.info("Retrieve all vendors!");
        List<Vendor> vendors = findAll();

        if (vendors == null || vendors.isEmpty()) {
            LOGGER.debug("No vendors founds!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            LOGGER.debug("Found {} vendors!", vendors.size());
            return buildResponseSuccess(vendors, HttpStatus.OK);
        }
    }

    private List<Vendor> findAll() {
        Objectify objectify = ofy();
        objectify.flush();
        return objectify
                       .load()
                       .type(Vendor.class)
                       .order("-updatedAt")
                       .list();
    }

    @RequestMapping(value = "/product/{codeProduct}", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Vendor>> getByCodeProduct(@PathVariable("codeProduct") Long codeProduct) {

        LOGGER.info("Retrieve vendor by code product:{}!", codeProduct);
        List<Vendor> vendors = findByCodeProduct(codeProduct);

        if (vendors == null) {
            LOGGER.debug("No vendor found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            LOGGER.debug("Found {} vendors!", vendors.size());
            return buildResponseSuccess(vendors, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity deleteById(@PathVariable("id") Long id) {

        LOGGER.info("Find vendor by id:{}!", id);
        Vendor vendor = ObjectifyService.ofy()
                                .load()
                                .type(Vendor.class)
                                .id(id)
                                .now();

        if (vendor == null) {
            LOGGER.debug("No vendor found!");
            return buildResponseErro(HttpStatus.NOT_FOUND);
        } else {
            LOGGER.info("Delete vendor by id:{}!", id);
            ObjectifyService.ofy().delete().entity(vendor).now();

            LOGGER.debug("Deleted vendor!");
            return buildResponseSuccess(HttpStatus.OK);
        }
    }

    private List<Vendor> findByCodeProduct(Long codeProduct) {
        return ofy()
                       .load()
                       .type(Vendor.class)
                       .filter("codeProduct", codeProduct)
                       .order("-updatedAt")
                       .list();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Vendor> save(@RequestBody Vendor vendor) {

        if (vendor == null || vendor.getCodeProduct() == null) {
            LOGGER.debug("Vendor can not be null!");
            return buildResponseErro(HttpStatus.NOT_MODIFIED);
        }

        LOGGER.info("Save to vendor[{}]!", vendor);
        try {
            if (vendor.getVersion() != null) {
                vendor.updateEntity();
            } else {
                vendor.initEntity();
            }
            saveEntity(vendor);
            LOGGER.debug("Save vendor and generate to id: {}!", vendor.getId());
            return buildResponseSuccess(vendor, HttpStatus.OK);
        } catch (SaveException ex) {
            LOGGER.debug("Error to save vendor, cause:{}!", ex.getLocalizedMessage());
            return buildResponseErro(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity update(@RequestBody Vendor vendor) {

        if (vendor == null || vendor.getId() == null) {
            LOGGER.debug("Vendor can not be null!");
            return buildResponseErro(HttpStatus.NOT_MODIFIED);
        }

        LOGGER.info("Update to vendor[{}]!", vendor);
        try {
            long id = vendor.getId();
            Vendor vendorEntity = findById(id);
            if (vendorEntity != null) {
                try {
                    if (!vendorEntity.getVersion().equals(vendor.getVersion())) {
                        String cause = "There is already an vendor object registered with different version!" +
                                               " Please update your object!";
                        LOGGER.debug(cause);
                        return buildResponseErro(cause);
                    }

                    vendor = mergeEntity(vendor, vendorEntity);
                } catch (Exception e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                    return buildResponseErro(HttpStatus.NOT_MODIFIED);
                }
            } else {
                vendor.initEntity();
            }

            saveEntity(vendor);
            LOGGER.debug("update vendor with id: {}!", vendor.getId());
            return buildResponseSuccess(HttpStatus.OK);
        } catch (SaveException ex) {
            LOGGER.debug("Error to update vendor, cause:{}!", ex.getLocalizedMessage());
            return buildResponseErro(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private Vendor findById(long id) {
        return ofy()
                       .load()
                       .type(Vendor.class)
                       .filter("id", id)
                       .first()
                       .now();
    }

    private Vendor mergeEntity(@RequestBody Vendor vendor, Vendor vendorEntity) throws Exception {
        new MergeBean().merge(vendorEntity, vendor);
        vendor = vendorEntity;
        vendor.updateEntity();
        return vendor;
    }

    private void saveEntity(@RequestBody Vendor vendor) {
        ofy()
                .save()
                .entity(vendor)
                .now();
    }

}
