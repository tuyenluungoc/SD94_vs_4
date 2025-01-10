package org.example.sd_94vs1.service.product;

import lombok.AllArgsConstructor;
import org.example.sd_94vs1.entity.product.Manufacturer;
import org.example.sd_94vs1.repository.Product.ManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    public List<Manufacturer> getAll(){
        return manufacturerRepository.findAll();
    }
    // Lấy tất cả nhà sản xuất
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    // Lấy nhà sản xuất theo mã
    public Optional<Manufacturer> getManufacturerByCode(String manufacturerCode) {
        return manufacturerRepository.findByManufacturerCode(manufacturerCode);
    }
}
