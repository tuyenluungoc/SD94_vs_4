package org.example.sd_94vs1.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.example.sd_94vs1.entity.ShoppingCart;
import org.example.sd_94vs1.entity.ShoppingCartProducts;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.model.enums.UserRole;
import org.example.sd_94vs1.repository.InventoryRepository;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.example.sd_94vs1.repository.Product.ProductRepository;
import org.example.sd_94vs1.repository.ShoppingCartProductsRepository;
import org.example.sd_94vs1.repository.ShoppingCartRepository;
import org.example.sd_94vs1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private HttpSession session;
    @Autowired
    private DetailedProductRepository detailedProductRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShoppingCartProductsRepository shoppingCartProductsRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<ShoppingCart> getAllShoppingCartOfCurrentUser() {
        User user = (User) session.getAttribute("currentUser");
        return shoppingCartRepository.findByUser_UserCode(user.getUserCode(), Sort.by("createdAt").descending());
    }
    public List<ShoppingCartProducts> getProductsByCart(String shoppingCartCode) {
        // Fetch the products for the shopping cart
        List<ShoppingCartProducts> products = shoppingCartRepository.findProductsByShoppingCartCode(shoppingCartCode);

        // Fetch priceVND from DetailedProduct for each product in the shopping cart
        for (ShoppingCartProducts product : products) {
            DetailedProduct detailedProduct = detailedProductRepository.findByProduct_ProductCode(product.getProduct().getProductCode());
            if (detailedProduct != null) {
                product.getProduct().setPriceVND(detailedProduct.getPriceVND());
            }
        }

        return products;
    }

    // tim user
    public ShoppingCart findByUser(User user) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUser(user);
        return shoppingCart.orElse(null);  // Trả về null nếu không tìm thấy giỏ hàng
    }

    // Phương thức cập nhật trạng thái giỏ hàng
    @Transactional
    public void updateShoppingCartStatus(String shoppingCartCode, boolean status) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByShoppingCartCode(shoppingCartCode);
        if (shoppingCart.isPresent()) {
            ShoppingCart cart = shoppingCart.get();
            cart.setStatus(status);  // Cập nhật trạng thái
            shoppingCartRepository.save(cart);  // Lưu giỏ hàng đã cập nhật
            System.out.println("Trạng thái giỏ hàng đã được cập nhật: " + shoppingCartCode);
        } else {
            // Giỏ hàng không tồn tại
            System.out.println("Giỏ hàng không tồn tại: " + shoppingCartCode);
        }
    }






    public List<ShoppingCart> getAllShoppingCartsOfCurrentUser() {
        User user = (User) session.getAttribute("currentUser");
        return shoppingCartRepository.findByUser_UserCode(user.getUserCode(), Sort.by("createdAt").descending());
    }
    public User getRandomAdmin() {
        List<User> admins = userRepository.findByRole(UserRole.ADMIN);
        if (admins.isEmpty()) {
            throw new IllegalStateException("Không có admin nào trong hệ thống");
        }
        // Chọn ngẫu nhiên một admin
        int randomIndex = ThreadLocalRandom.current().nextInt(admins.size());
        return admins.get(randomIndex);
    }

    public ShoppingCart createShoppingCart(User user) {
        User randomAdmin = getRandomAdmin();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .shoppingCartCode(generateRandomShoppingCartCode())
                .user(user)
                .useradmin(randomAdmin)
                .status(false) // Giả sử giỏ hàng ban đầu là active
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        return shoppingCartRepository.save(shoppingCart);
    }

    // Phương thức để tạo mã giỏ hàng ngẫu nhiên
    private String generateRandomShoppingCartCode() {
        int randomNum = (int) (Math.random() * 10000);
        return "GH" + String.format("%04d", randomNum);
    }

    public ShoppingCart findOrCreateShoppingCart(User user) {
        // Tìm giỏ hàng chưa thanh toán của người dùng
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByUserAndStatus(user, false);

        return optionalCart.orElseGet(() -> createShoppingCart(user)); // Tạo mới nếu không có giỏ hàng chưa thanh toán
    }
    public List<ShoppingCart>getall() {

        return shoppingCartRepository.findByShoppingCartStatus(false);
    }
    public List<ShoppingCart> getShoppingCartsForAdmin(String adminCode) {
        return shoppingCartRepository.findByShoppingCartForAdmin(false, adminCode); // status = false
    }
    public Optional<ShoppingCart> findCurrentCart(User user) {
        return shoppingCartRepository.findByUserAndStatus(user, false);
    }

    public void delete(ShoppingCart shoppingCart) {
        shoppingCartRepository.delete(shoppingCart);
    }


}
