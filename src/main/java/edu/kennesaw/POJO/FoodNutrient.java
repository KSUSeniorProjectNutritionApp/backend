package edu.kennesaw.POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodNutrient {
    private Nutrient nutrient;
    private Integer amount;

    public Nutrient getNutrient() {
        return nutrient;
    }

    public void setNutrient(Nutrient nutrient) {
        this.nutrient = nutrient;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "FoodNutrient{" +
                "nutrient=" + nutrient +
                ", amount=" + amount +
                '}';
    }
}
