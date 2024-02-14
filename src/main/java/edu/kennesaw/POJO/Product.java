package edu.kennesaw.POJO;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorColumn(name = "type")
public abstract class Product {
    @Id
    private Integer fdcId;
    @Column(length = 2048)
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="product_id", referencedColumnName = "fdcId")
    private List<FoodNutrient> foodNutrients;

    public Integer getFdcId() {
        return fdcId;
    }

    public void setFdcId(Integer fdcId) {
        this.fdcId = fdcId;
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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + fdcId +
                ", description='" + description + '\'' +
                ", foodNutrients=" + foodNutrients +
                '}';
    }
}
