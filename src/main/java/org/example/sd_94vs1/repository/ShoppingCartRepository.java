package org.example.sd_94vs1.repository;

import jakarta.transaction.Transactional;
import org.example.sd_94vs1.entity.ShoppingCart;
import org.example.sd_94vs1.entity.ShoppingCartProducts;
import org.example.sd_94vs1.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,String> {
    List<ShoppingCart> findByUser_UserCode(String userCode, Sort sort);
    @Query("SELECT scp FROM ShoppingCartProducts scp WHERE scp.shoppingCart.shoppingCartCode = :shoppingCartCode AND scp.shoppingCart.status = false")
    List<ShoppingCartProducts> findProductsByShoppingCartCode(String shoppingCartCode);
    //lay ra spc co status
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.user = :user AND sc.status = :status")
    Optional<ShoppingCart> findByUserAndStatus(@Param("user") User user, @Param("status") Boolean status);
    // Phương thức xoá giỏ hàng theo mã shoppingCartCode bắt đầu với một chuỗi nhất định
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.status = :status")
    List<ShoppingCart> findByShoppingCartStatus(@Param("status") Boolean status);
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.status = :status AND sc.useradmin.userCode = :adminCode")
    List<ShoppingCart> findByShoppingCartForAdmin(@Param("status") Boolean status, @Param("adminCode") String adminCode);

    Optional<ShoppingCart> findByUser(User user);
    @Transactional
    @Modifying
    @Query("UPDATE ShoppingCart sc SET sc.status = :status WHERE sc.shoppingCartCode = :shoppingCartCode")
    void updateStatusByCode(@Param("shoppingCartCode") String shoppingCartCode, @Param("status") boolean status);
    Optional<ShoppingCart> findByShoppingCartCode(String shoppingCartCode);

}
