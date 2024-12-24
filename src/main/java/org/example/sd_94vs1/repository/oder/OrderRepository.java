package org.example.sd_94vs1.repository.oder;


import jakarta.transaction.Transactional;
import org.example.sd_94vs1.entity.oder.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    // Repository cho phép lưu trữ, tìm kiếm, và xử lý Order
    List<Order> findByFromShoppingCartCode(String shoppingCartCode);

    void delete(Order order);  // Xoá đơn hàng

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = true WHERE o.fromShoppingCartCode = :shoppingCartCode")
    int updateOrderStatus(@Param("shoppingCartCode") String shoppingCartCode);

    List<Order> findByShoppingCart_ShoppingCartCode(String shoppingCartCode);
    List<Order> findByStatusTrue();


}