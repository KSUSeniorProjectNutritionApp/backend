package edu.kennesaw.repositories.custom;

import edu.kennesaw.POJO.BrandedProduct;
import edu.kennesaw.records.Query;

import java.util.List;

public interface BrandedCustomSearchRepository {
    List<BrandedProduct> search(Query query);
}
