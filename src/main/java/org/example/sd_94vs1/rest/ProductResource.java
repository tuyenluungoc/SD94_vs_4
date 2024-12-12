package org.example.sd_94vs1.rest;

import lombok.RequiredArgsConstructor;
import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.repository.Product.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductResource {
    private final ProductRepository productRepository;

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id){
        return productRepository.findByProductCode(id).get();
    }
}
