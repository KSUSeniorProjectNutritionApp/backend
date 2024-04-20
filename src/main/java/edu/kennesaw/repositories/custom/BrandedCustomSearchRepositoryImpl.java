package edu.kennesaw.repositories.custom;

import edu.kennesaw.POJO.BrandedProduct;
import edu.kennesaw.records.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;

public class BrandedCustomSearchRepositoryImpl implements BrandedCustomSearchRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<BrandedProduct> search(Query query) {
        SearchSession searchSession = Search.session(entityManager);
        return searchSession.search(BrandedProduct.class)
                .where(f -> f.match()
                        .fields("brandOwner", "description")
                        .matching(query.keywords()))
                .fetchHits(query.hits());
    }
}
