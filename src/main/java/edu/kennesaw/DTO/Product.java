package edu.kennesaw.DTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String foodClass;
    private String description;

    public String getFoodClass() {
        return foodClass;
    }

    public void setFoodClass(String foodClass) {
        this.foodClass = foodClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "foodClass='" + foodClass + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
