package edu.kennesaw.components;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kennesaw.POJO.BrandedProduct;
import edu.kennesaw.POJO.RawProduct;
import edu.kennesaw.repositories.BrandedProductRepository;
import edu.kennesaw.repositories.RawProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

@Component
public class AwsS3Service {
    private static final String BUCKET = "rednutritionbucket";
    private static final String BRANDED = "branded/";
    private static final String FOUNDATION = "foundationDownload.json";

    private AwsCredentials awsCredentials;
    private AwsCredentialsProvider awsCredentialsProvider;
    private Logger logger = LoggerFactory.getLogger(AwsS3Service.class);


    public AwsS3Service(@Value("${aws.s3.key}") String key, @Value("${aws.s3.secret}") String secret) {
        awsCredentials = AwsBasicCredentials.create(key, secret);
        awsCredentialsProvider = StaticCredentialsProvider.create(awsCredentials);
    }

    @Async
    public void downloadBranded(BrandedProductRepository brandedProductRepository, Lock lock, Integer position) {
        try(S3Client s3Client = S3Client.builder().credentialsProvider(awsCredentialsProvider).region(Region.US_EAST_1).build()) {
            ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(ListObjectsV2Request.builder().bucket(BUCKET).prefix(BRANDED).build());
            List<S3Object> s3Objects = listObjectsV2Response.contents();
            S3Object s3Object;
            int total = s3Objects.size() - position -1;
            int start = position;
            for(; position <= s3Objects.size(); position++){
                s3Object = s3Objects.get(position);
                if(s3Object.key().equals(BRANDED)) {
                    logger.info("Skipping directory prefix {}", s3Object.key());
                    continue;
                } else {
                    logger.info("Processing file {}: num {}/{}", s3Object.key(), position - start + 1,total);
                }
                downloadBrandedPart(s3Client, s3Object, brandedProductRepository, lock);
            }
            logger.info("Completed processing branded files {}-{}",start,s3Objects.size()-1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadBrandedPart(S3Client s3Client, S3Object s3Object, BrandedProductRepository brandedProductRepository, Lock lock) throws InterruptedException {
        List<BrandedProduct> brandedProducts = new ArrayList<>();
        try (ResponseInputStream<GetObjectResponse> inputStream =  s3Client.getObject(GetObjectRequest.builder().bucket(BUCKET).key(s3Object.key()).build())){
            ObjectMapper objectMapper = new ObjectMapper();
            Scanner scanner = new Scanner(inputStream);
            String json;
            BrandedProduct brandedProduct = null;
            while (scanner.hasNextLine()) {
                json = scanner.nextLine();
                if (json.length() < 50) {
                    continue;
                }
                try {
                    brandedProduct = objectMapper.readValue(json, BrandedProduct.class);
                } catch (JsonParseException jsonParseException) {
                    logger.warn("Unable to parse line {}", json);
                    continue;
                }
                brandedProducts.add(brandedProduct);
                if (brandedProducts.size() < 10) {
                    continue;
                }
                lock.lock();
                try {
                    brandedProductRepository.saveAll(brandedProducts);
                } catch (DataAccessException e) {
                    logger.warn("Skipping insert because of DataAccess Exception with message: {}", e.getMessage());
                }
                lock.unlock();
                brandedProducts.clear();

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        lock.lock();
        try {
            brandedProductRepository.saveAll(brandedProducts);
        } catch (DataAccessException e) {
            logger.warn("Skipping insert because of DataAccess Exception with message: {}", e.getMessage());
        }
        lock.unlock();
        logger.info("Successfully processed {}", s3Object.key());
    }

    @Async
    public void downloadRaw(RawProductRepository rawProductRepository, Lock lock) throws InterruptedException {
        List<RawProduct> rawProducts = new ArrayList<>();

        try (S3Client s3Client = S3Client.builder().credentialsProvider(awsCredentialsProvider).region(Region.US_EAST_1).build();
             ResponseInputStream<GetObjectResponse> inputStream =  s3Client.getObject(GetObjectRequest.builder().bucket(BUCKET).key(FOUNDATION).build())){
            ObjectMapper objectMapper = new ObjectMapper();
            Scanner scanner = new Scanner(inputStream);
            String json;
            RawProduct rawProduct;
            while (scanner.hasNextLine()){
                json = scanner.nextLine();
                if (json.length() < 50) {
                    continue;
                }
                try {
                    rawProduct = objectMapper.readValue(json, RawProduct.class);
                } catch (JsonParseException jsonParseException) {
                    logger.warn("Unable to parse line {}", json);
                    continue;
                }
                if(rawProducts.size() < 10) {
                    rawProducts.add(rawProduct);
                }
                lock.lock();
                rawProductRepository.saveAll(rawProducts);
                lock.unlock();
                rawProducts.clear();

            }
        } catch (Exception e) {
            logger.warn("Failed to finish update raw database");
            throw new RuntimeException(e);
        }
        lock.lock();
        rawProductRepository.saveAll(rawProducts);
        lock.unlock();
        logger.info("Successfully updated raw database");
    }

}


