package edu.kennesaw.repositories;

import edu.kennesaw.POJO.RawProduct;
import edu.kennesaw.repositories.custom.RawCustomSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawProductRepository extends JpaRepository<RawProduct, Integer>, RawCustomSearchRepository {

}
