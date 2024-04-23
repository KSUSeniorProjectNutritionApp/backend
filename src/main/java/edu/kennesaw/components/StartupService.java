package edu.kennesaw.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class StartupService {
    @Value("${workspace.directory}")
    private String workspace;


    Logger logger = LoggerFactory.getLogger(StartupService.class);


    @EventListener(ApplicationReadyEvent.class)
    public void cleanup() throws IOException {
        removeLock(workspace+"/BrandedProduct/write.lock");
        removeLock(workspace+"/RawProduct/write.lock");
    }
    private void removeLock(String file) throws IOException {
        Path path = Paths.get(file);
        System.out.println(path);
        if (Files.exists(path)) {
            logger.info("{} exists", path);
            Files.delete(path);
            logger.info("Deleted {}", path);
        }
    }
}
