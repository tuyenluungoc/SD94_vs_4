package org.example.sd_94vs1.service;

import org.example.sd_94vs1.entity.ShoppingCart;
import org.example.sd_94vs1.entity.ShoppingCartProducts;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.example.sd_94vs1.repository.ShoppingCartProductsRepository;
import org.example.sd_94vs1.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ShoppingCartProductsService {
    @Autowired
    private ShoppingCartProductsRepository shoppingCartProductsRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private DetailedProductRepository detailedProductRepository;




    public List<ShoppingCartProducts> getShoppingCartProductsByCode(String shoppingCartCode) {
        return shoppingCartProductsRepository.findProductsByShoppingCartCode(shoppingCartCode);
    }
    public ShoppingCartProducts addProductToCart(User user, Product product, int amount) {
        // Lấy giỏ hàng hiện tại của người dùng
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserAndStatus(user, false)
                .orElseGet(() -> {
                    // Nếu không có giỏ hàng hoặc giỏ hàng hiện tại đã được thanh toán, tạo giỏ hàng mới
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setShoppingCartCode(generateNewShoppingCartCode());
                    newCart.setUser(user);
                    newCart.setStatus(false);
                    newCart.setCreatedAt(new Date());
                    newCart.setUpdatedAt(new Date());
                    return shoppingCartRepository.save(newCart);
                });

        // Kiểm tra sản phẩm có trong giỏ hàng chưa
        Optional<ShoppingCartProducts> existingProductInCart = shoppingCartProductsRepository
                .findByShoppingCartAndProduct(shoppingCart, product);

        if (existingProductInCart.isPresent()) {
            // Nếu sản phẩm đã có, cập nhật số lượng
            ShoppingCartProducts shoppingCartProduct = existingProductInCart.get();
            shoppingCartProduct.setAmount(shoppingCartProduct.getAmount() + amount);
            shoppingCartProduct.setUpdatedAt(new Date());
            return shoppingCartProductsRepository.save(shoppingCartProduct);
        } else {
            // Nếu sản phẩm chưa có, tạo mới và thêm vào giỏ hàng
            ShoppingCartProducts shoppingCartProduct = new ShoppingCartProducts();
            shoppingCartProduct.setShoppingCart(shoppingCart);
            shoppingCartProduct.setProduct(product);
            shoppingCartProduct.setAmount(amount);
            shoppingCartProduct.setShoppingCartProductCode(generateNewShoppingCartCode());
            shoppingCartProduct.setCreatedAt(new Date());
            shoppingCartProduct.setUpdatedAt(new Date());
            return shoppingCartProductsRepository.save(shoppingCartProduct);
        }
    }

    public void updateDetailedProductQuantitiesAfterPayment(String shoppingCartCode) {
        // Lấy các sản phẩm từ giỏ hàng dựa trên mã giỏ hàng
        List<ShoppingCartProducts> cartProducts = shoppingCartProductsRepository.findProductsByShoppingCartCode(shoppingCartCode);

        // Duyệt qua từng sản phẩm trong giỏ hàng
        for (ShoppingCartProducts cartProduct : cartProducts) {
            Product product = cartProduct.getProduct();
            int quantityToDeduct = cartProduct.getAmount();

            System.out.println("Đang xử lý sản phẩm: " + product.getName() + ", Số lượng cần trừ: " + quantityToDeduct);

            // Lấy sản phẩm chi tiết từ database
            Optional<DetailedProduct> detailedProductOptional = detailedProductRepository.findByProduct(product);

            // Kiểm tra xem sản phẩm chi tiết có tồn tại không
            if (detailedProductOptional.isPresent()) {
                DetailedProduct detailedProduct = detailedProductOptional.get();
                int currentQuantity = detailedProduct.getQuantity();
                int newQuantity = currentQuantity - quantityToDeduct;

                // Log số lượng trước và sau khi trừ
                System.out.println("Số lượng hiện tại của " + product.getName() + ": " + currentQuantity);
                System.out.println("Số lượng mới sau khi trừ: " + newQuantity);

                // Kiểm tra nếu số lượng mới không âm
                if (newQuantity >= 0) {
                    // Cập nhật số lượng sản phẩm trong cơ sở dữ liệu
                    detailedProduct.setQuantity(newQuantity);
                    detailedProductRepository.save(detailedProduct);

                    System.out.println("Đã cập nhật số lượng sản phẩm: " + product.getName() +
                            " từ " + currentQuantity + " xuống " + newQuantity);
                } else {
                    // Nếu số lượng sau khi trừ nhỏ hơn 0, báo lỗi
                    throw new IllegalArgumentException("Sản phẩm " + product.getName() + " không đủ số lượng.");
                }
            } else {
                // Nếu không tìm thấy sản phẩm chi tiết, báo lỗi
                throw new IllegalArgumentException("Không tìm thấy sản phẩm chi tiết cho mã sản phẩm: " + product.getProductCode());
            }
        }
    }


    // Phương thức tạo mã giỏ hàng mới
    private String generateNewShoppingCartCode() {
        // Mã ngẫu nhiên cho giỏ hàng mới
        int randomNumber = 1000 + new Random().nextInt(9000);
        return "GH" + randomNumber;
    }

    // Phương thức tạo mã ngẫu nhiên
    private String generateRandomShoppingCartCode() {
        return "GH" + (1000 + new Random().nextInt(9000)); // Mã GHxxxx
    }

    public void clearCart(ShoppingCart shoppingCart) {
        List<ShoppingCartProducts> products = shoppingCartProductsRepository.findByShoppingCart(shoppingCart);
        if (!products.isEmpty()) {
            shoppingCartProductsRepository.deleteAll(products);
        }
    }
    public boolean deleteProductFromCart(String shoppingCartCode, String productCode) {
        // Tìm sản phẩm cụ thể trong giỏ hàng
        Optional<ShoppingCartProducts> productInCart = shoppingCartProductsRepository.findByShoppingCart_ShoppingCartCodeAndProduct_ProductCode(shoppingCartCode, productCode);


        if (productInCart.isPresent()) {
            // Xóa sản phẩm nếu tồn tại
            shoppingCartProductsRepository.deleteByShoppingCart_ShoppingCartCodeAndProduct_ProductCode(shoppingCartCode, productCode);
            return true;
        }

        // Sản phẩm không tồn tại
        return false;
    }

//    public List<ShoppingCartProducts> getShoppingCartProductsByCode(String shoppingCartCode) {
//        return shoppingCartProductsRepository.findByShoppingCartCode(shoppingCartCode);
//    }

}
