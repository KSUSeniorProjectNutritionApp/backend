package edu.kennesaw;

import edu.kennesaw.POJO.BrandedProduct;
import edu.kennesaw.POJO.Product;
import edu.kennesaw.components.AwsS3Service;
import edu.kennesaw.components.StartupService;
import edu.kennesaw.records.Barcode;
import edu.kennesaw.records.Position;
import edu.kennesaw.records.Query;
import edu.kennesaw.repositories.BrandedProductRepository;
import edu.kennesaw.repositories.RawProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@RestController
public class RedNutritionController {

    Semaphore rawSemaphore = new Semaphore(1, true);
    Semaphore brandedSemaphore = new Semaphore(1, true);
    ReentrantReadWriteLock rawLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock brandedLock = new ReentrantReadWriteLock();

    @Autowired
    StartupService startupService;


    @Autowired
    AwsS3Service awsS3Service;

    @Autowired
    BrandedProductRepository brandedProductRepository;

    @Autowired
    RawProductRepository rawProductRepository;

    Logger logger = LoggerFactory.getLogger(RedNutritionController.class);


    @GetMapping("/")
    public String index() {
        logger.debug("healthy");
        return "healthy";
    }

//    @PostMapping("/updateDatabase")
//    public void updateDatabase() throws InterruptedException {
//        updateRawDatabase();
//        updateBrandedDatabase();
//    }

    @PostMapping("/updateRawDatabase")
    public void updateRawDatabase() throws InterruptedException {
        logger.info("Raw database update requested");
        awsS3Service.downloadRaw(rawProductRepository, rawLock.writeLock());
        logger.info("Raw database update running async");
    }

    @PostMapping(value = "/updateBrandedDatabase", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateBrandedDatabase(@RequestBody Position position) throws InterruptedException {
        logger.info("Branded database update requested");
        awsS3Service.downloadBranded(brandedProductRepository, brandedLock.writeLock(), position.position());
        logger.info("Branded database update running async");
    }

    @PostMapping(value = "/query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public  List<? extends Product> query(@RequestBody Query query) throws InterruptedException {
        logger.info("Requested product with keywords: {}", query.keywords());
        Lock lock = brandedLock.readLock();
        lock.lock();
        List<Product> products = new ArrayList<>(brandedProductRepository.search(query));
        lock.unlock();
        logger.info("Found {} branded products with keywords: {}", products.size(), query.keywords());
        if (products.size() < query.hits()) {
            lock = rawLock.readLock();
            lock.lock();
            products.addAll(rawProductRepository.search(new Query(query.keywords(), query.hits() - products.size())));
            lock.unlock();
        }
        logger.info("Found {} total products with keywords: {}", products.size(), query.keywords());
        return products;
    }

    @PostMapping(value = "/barcode", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BrandedProduct> query(@RequestBody Barcode barcode) throws InterruptedException {
        logger.info("Requested product with barcode: {}", barcode.barcode());
        ReentrantReadWriteLock.ReadLock lock = brandedLock.readLock();
        lock.lock();
        Optional<BrandedProduct> brandedProductOptional = brandedProductRepository.findByGtinUpc(barcode.barcode());
        lock.unlock();
        logger.info("Barcode {} was {}", barcode.barcode(), brandedProductOptional.isPresent() ? "found" : "not found");
        return ResponseEntity.of(brandedProductOptional);
    }

}
