package org.example.sd_94vs1.service.oder;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sd_94vs1.entity.ShoppingCart;
import org.example.sd_94vs1.entity.ShoppingCartProducts;
import org.example.sd_94vs1.entity.oder.Order;
import org.example.sd_94vs1.repository.ShoppingCartProductsRepository;
import org.example.sd_94vs1.repository.ShoppingCartRepository;
import org.example.sd_94vs1.repository.oder.OrderRepository;
import org.example.sd_94vs1.rest.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    private final ShoppingCartProductsRepository shoppingCartProductsRepository;

    // Lấy tất cả các đơn hàng
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Hàm để tạo mã orderCode với tiền tố "hd" và 4 chữ số ngẫu nhiên
    private String generateOrderCode() {
        Random random = new Random();
        int number = 1000 + random.nextInt(9000); // Tạo số ngẫu nhiên từ 1000 đến 9999
        return "hd" + number;
    }

    // Phương thức thêm mới Order
    public Order addOrder(Order order) {
        // Kiểm tra xem giỏ hàng có tồn tại không
        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findById(order.getFromShoppingCartCode());
        if (shoppingCartOpt.isEmpty()) {
            throw new IllegalArgumentException("Shopping cart does not exist");
        }

        // Kiểm tra xem đã có đơn hàng nào với shoppingCartCode chưa
        List<Order> existingOrders = orderRepository.findByFromShoppingCartCode(order.getFromShoppingCartCode());

        if (!existingOrders.isEmpty()) {
            // Nếu có đơn hàng đã tồn tại và chưa được thanh toán, xoá đơn hàng cũ
            for (Order existingOrder : existingOrders) {
                if (Boolean.FALSE.equals(existingOrder.getStatus())) {
                    orderRepository.delete(existingOrder);  // Xoá đơn hàng cũ nếu trạng thái là "false"
                }
            }
        }
        // Tạo mã orderCode mới
        order.setOrderCode(generateOrderCode());

        // Thiết lập các thông tin khác cho đơn hàng
        order.setStatus(false);  // Đặt trạng thái là "pending" nếu chưa thanh toán
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        // Lưu đơn hàng vào database
        return orderRepository.save(order);
    }
    // Tìm đơn hàng theo mã đơn hàng
    public Optional<Order> getOrderById(String orderCode) {
        return orderRepository.findById(orderCode);
    }

    // Cập nhật thông tin đơn hàng
    public Order updateOrder(Order order) {
        order.setUpdatedAt(new Date());
        return orderRepository.save(order);
    }

    // Xoá đơn hàng
    public void deleteOrder(String orderCode) {
        orderRepository.deleteById(orderCode);
    }
    public List<Order> findOrdersByShoppingCartCode(String shoppingCartCode) {
        return orderRepository.findByShoppingCart_ShoppingCartCode(shoppingCartCode);
    }

    public List<Order> getOrdersWithStatusTrue() {
        return orderRepository.findByStatusTrue();
    }
    public Order getOrderDetails(String shoppingCartCode) {
        List<Order> orders = orderRepository.findByFromShoppingCartCode(shoppingCartCode);
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Order not found for shoppingCartCode: " + shoppingCartCode);
        }
        // Lấy phần tử đầu tiên (hoặc logic khác tùy vào yêu cầu)
        return orders.get(0);
    }

    public List<ShoppingCartProducts> getProductsByShoppingCart(String shoppingCartCode) {
        return shoppingCartProductsRepository.findAllByShoppingCart_ShoppingCartCode(shoppingCartCode);
    }
    public void deleteOrdersByShoppingCartCode(String shoppingCartCode) {
        // Tìm và xóa tất cả đơn hàng có shoppingCartCode tương ứng
        List<Order> orders = orderRepository.findByFromShoppingCartCode(shoppingCartCode);
        for (Order order : orders) {
            orderRepository.delete(order);
        }
    }
    public void updateOrdersToNullifyShoppingCartCode(String shoppingCartCode) {
        List<Order> orders = orderRepository.findByFromShoppingCartCode(shoppingCartCode);
        for (Order order : orders) {
            order.setFromShoppingCartCode(null);  // Hoặc giá trị khác nếu cần
            orderRepository.save(order);
        }
    }

}