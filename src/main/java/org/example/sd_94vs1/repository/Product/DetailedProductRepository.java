package org.example.sd_94vs1.repository.Product;

import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetailedProductRepository extends JpaRepository<DetailedProduct, String> {
    Optional<DetailedProduct> findByProduct(Product product);
    @Query("SELECT p.priceVND FROM DetailedProduct p WHERE p.product.productCode = :productCode")
    BigDecimal findPriceVNDByProductCode(String productCode);
    DetailedProduct findByProduct_ProductCode(String productCode);

    List<DetailedProduct> findAllByProduct_ProductCode(String productCode);


    DetailedProduct findByDetailedProductCode(String detailedProductCode);


}
