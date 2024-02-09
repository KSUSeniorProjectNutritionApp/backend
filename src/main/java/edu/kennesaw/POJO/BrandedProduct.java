package edu.kennesaw.POJO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandedProduct {
    private String foodClass;
    private String description;

    private List<FoodNutrient> foodNutrients;
    private String brandOwner;
    @Id
    private String gtinUpc;
    private String ingredients;
    private Integer servingSize;
    private String servingSizeUnit;


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

    public List<FoodNutrient> getFoodNutrients() {
        return foodNutrients;
    }

    public void setFoodNutrients(List<FoodNutrient> foodNutrients) {
        this.foodNutrients = foodNutrients;
    }

    public String getBrandOwner() {
        return brandOwner;
    }

    public void setBrandOwner(String brandOwner) {
        this.brandOwner = brandOwner;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    public void setGtinUpc(String gtinUpc) {
        this.gtinUpc = gtinUpc;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getServingSize() {
        return servingSize;
    }

    public void setServingSize(Integer servingSize) {
        this.servingSize = servingSize;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public void setServingSizeUnit(String servingSizeUnit) {
        this.servingSizeUnit = servingSizeUnit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "foodClass='" + foodClass + '\'' +
                ", description='" + description + '\'' +
                ", foodNutrients=" + foodNutrients +
                ", brandOwner='" + brandOwner + '\'' +
                ", gtinUpc='" + gtinUpc + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", servingSize=" + servingSize +
                ", servingSizeUnit='" + servingSizeUnit + '\'' +
                '}';
    }
}
