package org.example.sd_94vs1.repository.Product;

import org.example.sd_94vs1.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByCategoryCode(String categoryCode);


}
