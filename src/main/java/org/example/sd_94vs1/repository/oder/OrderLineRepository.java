package org.example.sd_94vs1.repository.oder;


import org.example.sd_94vs1.entity.oder.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, String> {
    // Repository này giúp quản lý các dòng chi tiết của đơn hàng
}