package org.example.sd_94vs1.rest;

import lombok.RequiredArgsConstructor;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailResource {

    private final DetailedProductRepository detailedProductRepository;

    @GetMapping("/{id}")
    public DetailedProduct getProductById(@PathVariable String id){
        return detailedProductRepository.findByDetailedProductCode(id);
    }
}
