package edu.kennesaw;

import edu.kennesaw.POJO.Product;
import edu.kennesaw.components.AwsS3Service;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class RedNutritionController {

    @Autowired
    AwsS3Service awsS3Service;

    @Autowired
    BrandedProductRepository brandedProductRepository;

    @Autowired
    RawProductRepository rawProductRepository;

    Logger logger = LoggerFactory.getLogger(RedNutritionController.class);

    @GetMapping("/updateDatabase")
    @ResponseBody
    public ResponseEntity<String> updateDatabase(Model model) {
        logger.info("Database update requested");
        long start = System.nanoTime();
        awsS3Service.downloadRaw(rawProductRepository);
        long time = System.nanoTime() - start;
        logger.info("Database update completed in " + time/1_000_000_000 + " seconds");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public  List<? extends Product> query(@RequestBody Query query) {
        List<Product> products = new ArrayList<>(rawProductRepository.search(query));
        products.addAll(brandedProductRepository.search(query));
        return products;
    }

}
