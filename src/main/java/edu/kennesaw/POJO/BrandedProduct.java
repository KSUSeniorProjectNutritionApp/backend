package edu.kennesaw.POJO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@DiscriminatorValue("branded")
public class BrandedProduct extends Product{

    @FullTextField
    private String brandOwner;
    private String gtinUpc;
    @Column(length = 4096)
    private String ingredients;
    private Integer servingSize;
    private String servingSizeUnit;

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
        return "BrandedProduct{" +
                "brandOwner='" + brandOwner + '\'' +
                ", gtinUpc='" + gtinUpc + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", servingSize=" + servingSize +
                ", servingSizeUnit='" + servingSizeUnit + '\'' +
                "} " + super.toString();
    }
}
