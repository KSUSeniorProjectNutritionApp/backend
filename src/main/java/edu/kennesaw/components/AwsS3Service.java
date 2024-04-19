package edu.kennesaw.components;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kennesaw.POJO.BrandedProduct;
import edu.kennesaw.POJO.RawProduct;
import edu.kennesaw.repositories.BrandedProductRepository;
import edu.kennesaw.repositories.RawProductRepository;
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
    private static final String BRANDED = "brandedDownload.json";
    private static final String FOUNDATION = "foundationDownload.json";

    private AwsCredentials awsCredentials;
    private AwsCredentialsProvider awsCredentialsProvider;

    public AwsS3Service(@Value("${aws.s3.key}") String key, @Value("${aws.s3.secret}") String secret) {
        awsCredentials = AwsBasicCredentials.create(key, secret);
        awsCredentialsProvider = StaticCredentialsProvider.create(awsCredentials);
    }

    public void downloadBranded(BrandedProductRepository brandedProductRepository, Semaphore semaphore) throws InterruptedException {

        try (S3Client s3Client = S3Client.builder().credentialsProvider(awsCredentialsProvider).region(Region.US_EAST_1).build();
             ResponseInputStream<GetObjectResponse> inputStream =  s3Client.getObject(GetObjectRequest.builder().bucket(BUCKET).key(BRANDED).build())){
            ObjectMapper objectMapper = new ObjectMapper();
            Scanner scanner = new Scanner(inputStream);
            String json;
            BrandedProduct brandedProduct;
            while (scanner.hasNextLine()){
                json = scanner.nextLine();
                if (json.length() < 50) {
                    continue;
                }

                brandedProduct = objectMapper.readValue(json, BrandedProduct.class);
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
            RawProduct rawProduct;
            while (scanner.hasNextLine()){
                json = scanner.nextLine();
                if (json.length() < 50) {
                    continue;
                }

                rawProduct = objectMapper.readValue(json, RawProduct.class);
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


