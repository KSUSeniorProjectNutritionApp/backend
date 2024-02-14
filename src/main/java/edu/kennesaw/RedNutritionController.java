package edu.kennesaw;

import edu.kennesaw.components.AwsS3Service;
import edu.kennesaw.repositories.BrandedProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RedNutritionController {

    @Autowired
    AwsS3Service awsS3Service;

    @Autowired
    BrandedProductRepository brandedProductRepository;

    Logger logger = LoggerFactory.getLogger(RedNutritionController.class);

    @GetMapping("/updateDatabase")
    @ResponseBody
    public ResponseEntity<String> updateDatabase(Model model) {
        logger.info("Database update requested");
        long start = System.nanoTime();
        awsS3Service.downloadBranded(brandedProductRepository);
        long time = System.nanoTime() - start;
        logger.info("Database update completed in " + time/1_000_000_000 + " seconds");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
