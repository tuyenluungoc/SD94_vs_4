package org.example.sd_94vs1.service.product;

import org.example.sd_94vs1.entity.product.Category;
import org.example.sd_94vs1.repository.Product.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    // Lấy tất cả danh mục
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Lấy danh mục theo mã
    public Optional<Category> getCategoryByCode(String categoryCode) {
        return categoryRepository.findByCategoryCode(categoryCode);
    }
}
