package edu.kennesaw.repositories;

import edu.kennesaw.POJO.FoodNutrient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodNutrientRepository extends JpaRepository<FoodNutrient, Integer> {
}
