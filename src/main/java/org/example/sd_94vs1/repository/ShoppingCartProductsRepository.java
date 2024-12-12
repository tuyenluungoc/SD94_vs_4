package org.example.sd_94vs1.repository;

import jakarta.transaction.Transactional;
import org.example.sd_94vs1.entity.ShoppingCart;
import org.example.sd_94vs1.entity.ShoppingCartProducts;
import org.example.sd_94vs1.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartProductsRepository extends JpaRepository<ShoppingCartProducts, String> {
        // ShoppingCartProductsRepository.java

        List<ShoppingCartProducts> findByShoppingCart_ShoppingCartCode(String shoppingCartCode);
        @Query("SELECT scp FROM ShoppingCartProducts scp WHERE scp.shoppingCart.shoppingCartCode = :shoppingCartCode and scp.shoppingCart.status=false ")
        List<ShoppingCartProducts> findProductsByShoppingCartCode(String shoppingCartCode);
        List<ShoppingCartProducts> findByShoppingCart(ShoppingCart shoppingCart);

        Optional<ShoppingCartProducts> findByShoppingCartAndProduct(ShoppingCart shoppingCart, Product product);

        @Modifying
        @Transactional
        void deleteByShoppingCart(ShoppingCart shoppingCart);

        Optional<ShoppingCartProducts> findByShoppingCart_ShoppingCartCodeAndProduct_ProductCode(String shoppingCartCode, String productCode);
        @Modifying
        @Transactional
        void deleteByShoppingCart_ShoppingCartCodeAndProduct_ProductCode(String shoppingCartCode, String productCode);  // Dùng productCode

        List<ShoppingCartProducts> findAllByShoppingCart_ShoppingCartCode(String shoppingCartCode);

//    List<ShoppingCartProducts> findByShoppingCartCode(String shoppingCartCode);

//        List<ShoppingCartProducts> findByShoppingCartCode(String shoppingCartCode);
}

