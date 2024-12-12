package org.example.sd_94vs1.rest;

import lombok.RequiredArgsConstructor;
import org.example.sd_94vs1.entity.product.Category;
import org.example.sd_94vs1.repository.Product.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryResource {

    private final CategoryRepository categoryRepository;


    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable String id){
        return categoryRepository.findByCategoryCode(id).get();
    }
}
