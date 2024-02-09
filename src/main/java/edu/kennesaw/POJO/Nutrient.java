package edu.kennesaw.POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Nutrient {
    private String name;
    private String unitName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "Nutrient{" +
                "name='" + name + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
