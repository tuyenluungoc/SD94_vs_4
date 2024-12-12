package org.example.sd_94vs1.repository.Product;

import org.example.sd_94vs1.entity.product.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p WHERE p.productCode LIKE :productCodePrefix% AND p.productType.productTypeCode = :productTypeCode")
    List<Product> findProductsByCodeAndType(@Param("productCodePrefix") String productCodePrefix, @Param("productTypeCode") String productTypeCode );
    Optional<Product> findByProductCode(String productCode);

    Product findByProductCodeAndProductType_ProductTypeCode(String productCode, String productTypeCode);
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);
}
