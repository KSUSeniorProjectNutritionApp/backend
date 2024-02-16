package edu.kennesaw.converters;

import com.fasterxml.jackson.databind.util.StdConverter;
import edu.kennesaw.POJO.RawProduct;

public class RawProductConverter extends StdConverter<RawProduct, RawProduct> {
    @Override
    public RawProduct convert(RawProduct value) {
        value.setCategory(value.getFoodCategory().getDescription());
        return value;
    }
}
