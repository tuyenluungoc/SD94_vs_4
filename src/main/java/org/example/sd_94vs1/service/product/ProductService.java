package org.example.sd_94vs1.service.product;

import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.repository.Product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findProductsByCodeAndType(String codePrefix, String productTypeCode) {
        return productRepository.findProductsByCodeAndType(codePrefix, productTypeCode);
    }

    public Product findByProductCode(String productCode) {
        Optional<Product> productOpt = productRepository.findByProductCode(productCode);
        return productOpt.orElse(null); // Trả về null nếu không tìm thấy sản phẩm
    }

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Optional<Product> getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    public List<Product> searchProductsByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Product> searchProducts(String query) {
        // Tìm kiếm theo tên sản phẩm hoặc mô tả sản phẩm
        return productRepository.findByNameContainingOrDescriptionContaining(query, query);
    }
}
