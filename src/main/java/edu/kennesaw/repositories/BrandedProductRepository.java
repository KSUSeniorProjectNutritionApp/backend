package edu.kennesaw.repositories;

import edu.kennesaw.POJO.BrandedProduct;
import edu.kennesaw.repositories.custom.BrandedCustomSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandedProductRepository extends JpaRepository<BrandedProduct, Integer>, BrandedCustomSearchRepository {
    Optional<BrandedProduct> findByGtinUpc(String gtinUpc);
}