package edu.kennesaw.repositories;

import edu.kennesaw.POJO.BrandedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandedProductRepository extends JpaRepository<BrandedProduct, String> {
}
