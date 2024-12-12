package org.example.sd_94vs1.service.oder;


import org.example.sd_94vs1.entity.oder.OrderLine;
import org.example.sd_94vs1.repository.oder.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLineService {

    @Autowired
    private OrderLineRepository orderLineRepository;

    // Lấy tất cả các dòng chi tiết đơn hàng
    public List<OrderLine> getAllOrderLines() {
        return orderLineRepository.findAll();
    }

    // Tạo dòng chi tiết đơn hàng mới
    public OrderLine createOrderLine(OrderLine orderLine) {
        return orderLineRepository.save(orderLine);
    }

    // Tìm dòng chi tiết đơn hàng theo mã dòng
    public OrderLine getOrderLineById(String orderLineCode) {
        return orderLineRepository.findById(orderLineCode).orElse(null);
    }

    // Cập nhật dòng chi tiết đơn hàng
    public OrderLine updateOrderLine(OrderLine orderLine) {
        return orderLineRepository.save(orderLine);
    }

    // Xoá dòng chi tiết đơn hàng
    public void deleteOrderLine(String orderLineCode) {
        orderLineRepository.deleteById(orderLineCode);
    }
}