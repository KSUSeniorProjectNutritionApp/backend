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
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

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


    public void downloadBranded(BrandedProductRepository brandedProductRepository, Semaphore semaphore) throws InterruptedException {

        try(S3Client s3Client = S3Client.builder().credentialsProvider(awsCredentialsProvider).region(Region.US_EAST_1).build()) {
            ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(ListObjectsV2Request.builder().bucket(BUCKET).prefix(BRANDED).build());
            for( S3Object s3Object: listObjectsV2Response.contents()) {
                if(s3Object.key().equals(BRANDED)) {
                    logger.info("Skipping directory prefix {}", s3Object.key());
                } else {
                    logger.info("Processing file {}", s3Object.key());
                }
                downloadBrandedPart(s3Client, s3Object, brandedProductRepository, semaphore);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadBrandedPart(S3Client s3Client, S3Object s3Object, BrandedProductRepository brandedProductRepository, Semaphore semaphore) throws InterruptedException {
        try (ResponseInputStream<GetObjectResponse> inputStream =  s3Client.getObject(GetObjectRequest.builder().bucket(BUCKET).key(s3Object.key()).build())){
            ObjectMapper objectMapper = new ObjectMapper();
            Scanner scanner = new Scanner(inputStream);
            String json;
            BrandedProduct brandedProduct = null;
            while (scanner.hasNextLine()){
                json = scanner.nextLine();
                if (json.length() < 50) {
                    continue;
                }
                try {
                    brandedProduct = objectMapper.readValue(json, BrandedProduct.class);
                } catch(JsonParseException jsonParseException) {
                    logger.warn("Unable to parse line {}", json);
                    continue;
                }
                semaphore.acquire();
                brandedProductRepository.save(brandedProduct);
                semaphore.release();

            }
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void downloadRaw(RawProductRepository rawProductRepository, Semaphore semaphore) throws InterruptedException {

        try (S3Client s3Client = S3Client.builder().credentialsProvider(awsCredentialsProvider).region(Region.US_EAST_1).build();
             ResponseInputStream<GetObjectResponse> inputStream =  s3Client.getObject(GetObjectRequest.builder().bucket(BUCKET).key(FOUNDATION).build())){
            ObjectMapper objectMapper = new ObjectMapper();
            Scanner scanner = new Scanner(inputStream);
            String json;
            RawProduct rawProduct = null;
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
                semaphore.acquire();
                rawProductRepository.save(rawProduct);
                semaphore.release();

            }
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}


