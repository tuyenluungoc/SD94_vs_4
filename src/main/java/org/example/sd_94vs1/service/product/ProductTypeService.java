package org.example.sd_94vs1.service.product;

import lombok.AllArgsConstructor;
import org.example.sd_94vs1.entity.product.ProductType;
import org.example.sd_94vs1.repository.Product.ProductTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;
    public List<ProductType> findAll() {
        return productTypeRepository.findAll();
    }
}
