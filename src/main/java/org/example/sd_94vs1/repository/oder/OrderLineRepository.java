package org.example.sd_94vs1.repository.oder;


import org.example.sd_94vs1.entity.oder.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, String> {
    List<OrderLine> findByOrderCode(String orderCode);
}