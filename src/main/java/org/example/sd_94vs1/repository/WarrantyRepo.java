package org.example.sd_94vs1.repository;

import org.example.sd_94vs1.entity.Inventory;
import org.example.sd_94vs1.entity.warranty.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarrantyRepo extends JpaRepository<Warranty, String> {
//    Warranty findByInventory_DetailedProduct(DetailedProduct detailedProduct);
    Warranty findByInventory(Inventory inventory);

    boolean existsByWarrantyCode(String warrantyCode);


        Warranty findByWarrantyCode(String warrantyCode);
    Optional<Warranty> findByInventory_InventoryCode(String inventoryCode);


}
