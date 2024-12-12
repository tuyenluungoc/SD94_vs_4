package org.example.sd_94vs1.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.sd_94vs1.entity.*;
import org.example.sd_94vs1.entity.oder.Order;
import org.example.sd_94vs1.entity.product.Manufacturer;
import org.example.sd_94vs1.entity.warranty.Warranty;
import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.example.sd_94vs1.model.enums.UserRole;
import org.example.sd_94vs1.repository.*;
import org.example.sd_94vs1.repository.Product.ManufacturerRepository;
import org.example.sd_94vs1.repository.oder.OrderRepository;
import org.example.sd_94vs1.service.DetailedProductService;
import org.example.sd_94vs1.service.ShoppingCartProductsService;
import org.example.sd_94vs1.service.ShoppingCartService;
import org.example.sd_94vs1.service.WarrantyService;
import org.example.sd_94vs1.service.oder.OrderService;
import org.example.sd_94vs1.service.product.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartProductsService shoppingCartProductsService;
    private final ProductService productService;
    private final DetailedProductService detailedProductService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderService orderService;
    private final WarrantyClaimRepo warrantyClaimRepository;
    private final WarrantyRepo warrantyRepo;
    private final InventoryRepository inventoryRepository;
    private final StatusRepository statusRepository;

    @GetMapping
    public String getAdminPage(Model model) {
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/dashboard")
    public String getDashboardPage(Model model) {
        return "admin/dashboard/index";
    }
//lấy tất cả các user
    @GetMapping("/users")
    public String getUserPage(Model model) {
        List<User> users = userRepository.findByRole(UserRole.ADMIN);
        model.addAttribute("users", users);
        return "admin/user/index";
    }
//    Lấy tất cả các khách hàng
    @GetMapping("/custumer")
    public String getCustomerPage(Model model) {
        List<User> users = userRepository.findByRole(UserRole.USER);
        model.addAttribute("users", users);
        return "admin/user/custumer";
    }


    // hiển thị giỏ hàng
    @GetMapping("/admin-all-shoppingcarts")
    public String getAllShoppingCarts(Model model, HttpSession session) {
        // Lấy tất cả giỏ hàng từ cơ sở dữ liệu
        // Lấy thông tin User từ session
        User admin = (User) session.getAttribute("currentUser");
        if (admin == null || admin.getRole() != UserRole.ADMIN) {
            return "redirect:/dang-nhap"; // Chưa đăng nhập hoặc không phải admin thì chuyển hướng
        }

        // Lấy mã admin từ User
        String adminCode = admin.getUserCode();
        System.out.println(adminCode);
        // Lấy tất cả giỏ hàng cho admin đang đăng nhập
        List<ShoppingCart> shoppingCarts = shoppingCartService.getShoppingCartsForAdmin(adminCode);
        List<ShoppingCartProducts> allShoppingCartProducts = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Duyệt qua tất cả giỏ hàng và tính tổng giá trị
        for (ShoppingCart cart : shoppingCarts) {
            // Lấy tất cả sản phẩm trong giỏ hàng theo mã giỏ hàng
            List<ShoppingCartProducts> shoppingCartProducts = shoppingCartProductsService.getShoppingCartProductsByCode(cart.getShoppingCartCode());

            for (ShoppingCartProducts shoppingCartProduct : shoppingCartProducts) {
                // Lấy giá của sản phẩm
                BigDecimal productPrice = detailedProductService.getProductPriceByCode(shoppingCartProduct.getProduct().getProductCode());

                // Nếu giá sản phẩm không tồn tại, gán giá mặc định là 0
                if (productPrice == null) {
                    productPrice = BigDecimal.ZERO;
                }

                shoppingCartProduct.setPrice(productPrice);

                // Tính tổng giá trị của sản phẩm trong giỏ hàng
                BigDecimal productTotalPrice = productPrice.multiply(BigDecimal.valueOf(shoppingCartProduct.getAmount()));

                // Cộng vào tổng giỏ hàng
                totalAmount = totalAmount.add(productTotalPrice);
            }

            // Thêm tất cả sản phẩm của giỏ hàng vào danh sách chung
            allShoppingCartProducts.addAll(shoppingCartProducts);
        }

        // Thêm giỏ hàng, thông tin sản phẩm và tổng giá trị vào model
        model.addAttribute("shoppingCarts", shoppingCarts);
        model.addAttribute("shoppingCartProducts", allShoppingCartProducts);
        model.addAttribute("totalAmount", totalAmount);

        return "admin/shoppingcard/shoppingcarts"; // Trang view admin để hiển thị giỏ hàng
    }
    @GetMapping("/ql-warranty")
    public String getQLWarrantyPage(Model model) {
        List<Warranty> warranties = warrantyRepo.findAll();
        Date currentDate = new Date(); // Lấy thời gian thực

        // Kiểm tra và cập nhật trạng thái
        for (Warranty warranty : warranties) {
            if (warranty.getEndDate() != null && currentDate.after(warranty.getEndDate())) {
                warranty.setWarrantyStatus("1"); // Set trạng thái thành 1
                warrantyRepo.save(warranty);     // Lưu lại thay đổi vào database
            }
        }

        model.addAttribute("warranty", warranties);
        return "admin/warranty/ql-warranty";
    }



    @GetMapping("/yc-warranty")
    public String getYcWarrantyPage(Model model) {
        List<WarrantyClaim> warrantyClaims=warrantyClaimRepository.findAll();
        model.addAttribute("warrantyClaims", warrantyClaims);
        return "admin/warranty/yc-warranty";
    }

    @GetMapping("/trang-thai-don-hang")
    public String getStatusPage(Model model) {
        List<Status> status=statusRepository.findAll();
        model.addAttribute("status", status);
        return "admin/trang-thai-don-hang";
    }
    @GetMapping("/shoppingcarts/delete/{shoppingCartCode}")
    public String deleteShoppingCart(@PathVariable String shoppingCartCode, RedirectAttributes redirectAttributes) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByShoppingCartCode(shoppingCartCode);

        if (shoppingCartOptional.isPresent()) {
            // Lấy giỏ hàng
            ShoppingCart shoppingCart = shoppingCartOptional.get();

            // Xóa các đơn hàng liên quan đến giỏ hàng này
            orderService.deleteOrdersByShoppingCartCode(shoppingCartCode);

            // Xóa giỏ hàng
            shoppingCartService.delete(shoppingCart);
            redirectAttributes.addFlashAttribute("success", "Giỏ hàng đã được xóa thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng không tồn tại.");
        }

        return "redirect:/admin/admin-all-shoppingcarts";
    }


    //
@GetMapping("/hoa-don")
public String getOrdersWithDetails(Model model) {
    try {
        // Lấy danh sách các hóa đơn có status là true
        List<Order> orders = orderService.getOrdersWithStatusTrue();

        // Lấy chi tiết sản phẩm của mỗi hóa đơn
        Map<String, Object> orderDetails = new HashMap<>();
        for (Order order : orders) {
            String shoppingCartCode = order.getFromShoppingCartCode();
            List<ShoppingCartProducts> products = orderService.getProductsByShoppingCart(shoppingCartCode);

            // Tính tổng số tiền của các sản phẩm
            BigDecimal totalAmount = products.stream()
                    .map(product -> {
                        // Kiểm tra giá trị null của price
                        BigDecimal price = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
                        return price.multiply(BigDecimal.valueOf(product.getAmount()));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Lưu chi tiết hóa đơn vào map
            orderDetails.put(shoppingCartCode, Map.of(
                    "order", order,
                    "products", products,
                    "totalAmount", totalAmount
            ));
        }

        // Thêm danh sách hóa đơn và chi tiết vào model
        model.addAttribute("orders", orders);
        model.addAttribute("orderDetails", orderDetails);

        return "admin/shoppingcard/odersadmin"; // Trả về view
    } catch (Exception ex) {
        model.addAttribute("error", "Có lỗi xảy ra: " + ex.getMessage());
        return "error"; // Trả về view lỗi nếu có ngoại lệ
    }
}

}
