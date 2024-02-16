package edu.kennesaw.POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodCategory {
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
