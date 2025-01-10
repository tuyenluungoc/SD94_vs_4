package org.example.sd_94vs1.repository.oder;


import org.example.sd_94vs1.entity.oder.OrderLine;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, String> {
    // Repository này giúp quản lý các dòng chi tiết của đơn hàng
    List<OrderLine> findByOrderCode(String orderCode);

    @Query("SELECT ol.productCode, SUM(ol.quantity) as totalQuantity " +
            "FROM OrderLine ol " +
            "GROUP BY ol.productCode " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> getTopSellingProducts(PageRequest pageable);
}