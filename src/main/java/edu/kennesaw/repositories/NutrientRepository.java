package edu.kennesaw.repositories;

import edu.kennesaw.POJO.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Integer> {
}
