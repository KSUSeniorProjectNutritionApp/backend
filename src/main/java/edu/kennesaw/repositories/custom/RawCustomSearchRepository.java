package edu.kennesaw.repositories.custom;

import edu.kennesaw.POJO.RawProduct;
import edu.kennesaw.records.Query;

import java.util.List;

public interface RawCustomSearchRepository {
    List<RawProduct> search(Query query);
}
