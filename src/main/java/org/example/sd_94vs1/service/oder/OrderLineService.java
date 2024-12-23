package org.example.sd_94vs1.service.oder;


import org.example.sd_94vs1.entity.oder.OrderLine;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.repository.Product.ProductRepository;
import org.example.sd_94vs1.repository.oder.OrderLineRepository;
import org.example.sd_94vs1.service.DetailedProductService;
import org.example.sd_94vs1.service.product.ProductService;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderLineService {

    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DetailedProductService detailedProductService;

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

    public List<OrderLine> getOrderLinesByOrderCode(String orderCode) {
        return orderLineRepository.findByOrderCode(orderCode);
    }

    public void createOrderLine(String orderCode, String productCode, Integer quantity) {
        // Tạo mã OrderLine tự động
        String orderLineCode = "ol"+UUID.randomUUID().toString().substring(0, 5);;
        // Lấy sản phẩm từ database
        DetailedProduct product = detailedProductService.findDetailedProduct(productCode);

        BigDecimal priceAtOrder = product.getPriceVND().multiply(BigDecimal.valueOf(quantity));


        System.out.println(quantity);
        System.out.println(priceAtOrder+"hihi");
        // Tạo mới OrderLine
        OrderLine orderLine = new OrderLine();
        orderLine.setOrderCode(orderCode);
        orderLine.setProductCode(productCode);
        orderLine.setQuantity(quantity);
        orderLine.setOrderLineCode(orderLineCode);
        orderLine.setPriceAtOrder(priceAtOrder);

        // Lưu OrderLine vào database
        orderLineRepository.save(orderLine);
        System.out.println("Đã tạo OrderLine với mã: " + orderLineCode);
    }


}