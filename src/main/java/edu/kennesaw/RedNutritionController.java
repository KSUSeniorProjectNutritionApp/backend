package edu.kennesaw;

import edu.kennesaw.POJO.BrandedProduct;
import edu.kennesaw.POJO.Product;
import edu.kennesaw.components.AwsS3Service;
import edu.kennesaw.records.Barcode;
import edu.kennesaw.records.Query;
import edu.kennesaw.repositories.BrandedProductRepository;
import edu.kennesaw.repositories.RawProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class RedNutritionController {

    @Autowired
    AwsS3Service awsS3Service;

    @Autowired
    BrandedProductRepository brandedProductRepository;

    @Autowired
    RawProductRepository rawProductRepository;

    Logger logger = LoggerFactory.getLogger(RedNutritionController.class);


    @GetMapping("/")
    public String index() {
        logger.info("healthy");
        return "healthy";
    }

    @PostMapping("/updateRawDatabase")
    public void updateRawDatabase() {
        logger.info("Raw database update requested");
        long start = System.nanoTime();
        awsS3Service.downloadRaw(rawProductRepository);
        long time = System.nanoTime() - start;
        logger.info("Raw database update completed in {} seconds", time / 1_000_000_000);
    }

    @PostMapping("/updateBrandedDatabase")
    public void updateBrandedDatabase() {
        logger.info("Branded database update requested");
        long start = System.nanoTime();
        awsS3Service.downloadBranded(brandedProductRepository);
        long time = System.nanoTime() - start;
        logger.info("Branded database update completed in {} seconds", time / 1_000_000_000);
    }

    @PostMapping(value = "/query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public  List<? extends Product> query(@RequestBody Query query) {
        List<Product> products = new ArrayList<>(rawProductRepository.search(query));
        products.addAll(brandedProductRepository.search(query));
        return products;
    }

    @PostMapping(value = "/barcode", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BrandedProduct query(@RequestBody Barcode barcode) {
        logger.info("Requested product with barcode: {}", barcode.barcode());
        Optional<BrandedProduct> brandedProductOptional = brandedProductRepository.findByGtinUpc(barcode.barcode());
        logger.info("Barcode {} was {}", barcode.barcode(), brandedProductOptional.isPresent() ? "found" : "not found");
        return brandedProductOptional.orElseThrow();
    }

}
