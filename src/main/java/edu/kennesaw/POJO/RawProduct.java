package edu.kennesaw.POJO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.kennesaw.converters.RawProductConverter;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@DiscriminatorValue("raw")
@JsonDeserialize(converter = RawProductConverter.class)
public class RawProduct extends Product{

    @JsonIgnore
    @Transient
    private FoodCategory foodCategory;

    @FullTextField
    private String category;

    public FoodCategory getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "RawProduct{" +
                "category='" + category + '\'' +
                "} " + super.toString();
    }
}
