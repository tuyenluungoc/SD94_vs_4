package org.example.sd_94vs1.repository;

import org.example.sd_94vs1.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
    // Tìm tất cả sản phẩm chưa có IMEI hoặc IMEI rỗng
    List<Inventory> findByImeiIsNullOrImei(String imei);


    @Query(value = """
    SELECT i.inventory_code AS inventoryCode, 
           i.product_code AS productCode, 
           i.status AS inventoryStatus, 
           i.date_received AS inventoryDateReceived, 
           p.product_type_code AS productTypeCode, 
           i.imei AS imei,
           i.quantity AS quantity,
           i.id_detailed_product AS idDetailedProduct
    FROM inventory i
    JOIN products p ON i.product_code = p.product_code
    WHERE i.product_code = :productCode 
      AND p.product_type_code = :productTypeCode 
      AND i.status = 'available'
    ORDER BY i.date_received ASC
    LIMIT :quantity
""", nativeQuery = true)
    List<Object[]> findAvailableInventories(
            @Param("productCode") String productCode,
            @Param("productTypeCode") String productTypeCode,
            @Param("quantity") int quantity
    );


    Optional<Inventory> findByImei(String imei);
    boolean existsByImei(String imei);

}
