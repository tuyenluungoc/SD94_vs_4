package org.example.sd_94vs1.repository.Product;

import org.example.sd_94vs1.entity.product.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, String> {
    // Bạn có thể định nghĩa các phương thức truy vấn tùy chỉnh tại đây
    Optional<Manufacturer> findByManufacturerCode(String manufacturerCode);


}
