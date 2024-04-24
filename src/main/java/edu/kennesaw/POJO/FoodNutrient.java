package edu.kennesaw.POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodNutrient {

    @Id
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Nutrient nutrient;
    private Integer amount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
                "id=" + id +
                ", nutrient=" + nutrient +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodNutrient that = (FoodNutrient) o;
        return id.equals(that.id) && nutrient.equals(that.nutrient) && amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nutrient.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }
}
