package edu.kennesaw;

import edu.kennesaw.components.AwsS3Service;
import edu.kennesaw.repositories.BrandedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

@Controller
public class RedNutritionController {

    @Autowired
    AwsS3Service awsS3Service;

    @Autowired
    BrandedProductRepository brandedProductRepository;

    @GetMapping("/updateDatabase")
    @ResponseBody
    public ResponseEntity<String> updateDatabase(Model model) {
        awsS3Service.downloadBranded(brandedProductRepository);
        System.out.println(brandedProductRepository.findAll().getFirst());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
