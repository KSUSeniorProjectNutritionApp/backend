package edu.kennesaw.repositories.custom;

import edu.kennesaw.POJO.RawProduct;
import edu.kennesaw.records.Query;
import edu.kennesaw.repositories.custom.RawCustomSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;

public class RawCustomSearchRepositoryImpl implements RawCustomSearchRepository {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<RawProduct> search(Query query) {
        SearchSession searchSession = Search.session(entityManager);
        return searchSession.search(RawProduct.class)
                .where(f -> f.match()
                        .fields("category", "description")
                        .matching(query.keywords()))
                .fetchHits(query.hits());
    }
}
