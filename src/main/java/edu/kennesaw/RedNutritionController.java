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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;


@RestController
public class RedNutritionController {

    Semaphore rawSemaphore = new Semaphore(1, true);
    Semaphore brandedSemaphore = new Semaphore(1, true);


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

    @PostMapping("/updateDatabase")
    public void updateDatabase() throws InterruptedException {
        updateRawDatabase();
        updateBrandedDatabase();
    }

//    @PostMapping("/updateRawDatabase")
    public void updateRawDatabase() throws InterruptedException {
        logger.info("Raw database update requested");
        long start = System.nanoTime();
        awsS3Service.downloadRaw(rawProductRepository, rawSemaphore);
        long time = System.nanoTime() - start;
        logger.info("Raw database update completed in {} seconds", time / 1_000_000_000);
    }

//    @PostMapping("/updateBrandedDatabase")
    public void updateBrandedDatabase() throws InterruptedException {
        logger.info("Branded database update requested");
        long start = System.nanoTime();
        awsS3Service.downloadBranded(brandedProductRepository, brandedSemaphore);
        long time = System.nanoTime() - start;
        logger.info("Branded database update completed in {} seconds", time / 1_000_000_000);
    }

    @PostMapping(value = "/query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public  List<? extends Product> query(@RequestBody Query query) throws InterruptedException {
        logger.info("Requested product with keywords: {}", query.keywords());
        rawSemaphore.acquire();
        List<Product> products = new ArrayList<>(rawProductRepository.search(query));
        rawSemaphore.release();
        logger.info("Found {} raw products with keywords: {}", products.size(), query.keywords());
        brandedSemaphore.acquire();
        products.addAll(brandedProductRepository.search(query));
        brandedSemaphore.release();
        logger.info("Found {} total products with keywords: {}", products.size(), query.keywords());
        return products;
    }

    @PostMapping(value = "/barcode", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BrandedProduct query(@RequestBody Barcode barcode) throws InterruptedException {
        logger.info("Requested product with barcode: {}", barcode.barcode());
        brandedSemaphore.acquire();
        Optional<BrandedProduct> brandedProductOptional = brandedProductRepository.findByGtinUpc(barcode.barcode());
        brandedSemaphore.release();
        logger.info("Barcode {} was {}", barcode.barcode(), brandedProductOptional.isPresent() ? "found" : "not found");
        return brandedProductOptional.orElseThrow();
    }

}
