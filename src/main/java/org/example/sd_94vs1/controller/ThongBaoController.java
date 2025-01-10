package org.example.sd_94vs1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.sd_94vs1.DTO.StatusDTO;
import org.example.sd_94vs1.entity.*;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.example.sd_94vs1.repository.InventoryRepository;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.example.sd_94vs1.repository.ShoppingCartProductsRepository;
import org.example.sd_94vs1.repository.StatusRepository;
import org.example.sd_94vs1.repository.WarrantyClaimRepo;
import org.example.sd_94vs1.service.DetailedProductService;
import org.example.sd_94vs1.service.ShoppingCartProductsService;
import org.example.sd_94vs1.service.ShoppingCartService;
import org.example.sd_94vs1.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notifications")
public class ThongBaoController {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private WarrantyClaimRepo warrantyClaimRepo;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private DetailedProductService detailedProductService;
    @Autowired
    private ShoppingCartProductsService shoppingCartProductsService;
    @Autowired
    private ShoppingCartProductsRepository shoppingCartProductsRepository;
    @Autowired
    private DetailedProductRepository detailedProductRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping()
    public String getNotifications(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/dang-nhap"; // Chuyển hướng nếu chưa đăng nhập
        }

        // Lấy danh sách trạng thái đơn hàng của người dùng
        List<Status> statuses = statusRepository.findByUser_UserCode(currentUser.getUserCode());
        List<StatusDTO> statusDTOs = statuses.stream()
                .map(status -> new StatusDTO(
                        status.getStatusCode(),
                        status.getNameStatus(),
                        status.getShoppingCart().getShoppingCartCode()))
                .collect(Collectors.toList());

        // Lấy danh sách thông báo từ session
        List<StatusDTO> sessionNotifications = (List<StatusDTO>) session.getAttribute("notifications");
        if (sessionNotifications == null) {
            sessionNotifications = new ArrayList<>();
        }

        // Gộp tất cả thông báo
        List<StatusDTO> allNotifications = new ArrayList<>();
        allNotifications.addAll(statusDTOs);
        allNotifications.addAll(sessionNotifications);

        model.addAttribute("notifications", allNotifications);

        return "web/thong-bao";
    }





    @PostMapping("/update-status/{statusCode}")
    public String updateStatus(@PathVariable("statusCode") String statusCode,
                               @RequestParam("newStatus") String newStatus,
                               Model model) {
        try {
            // Cập nhật trạng thái
            statusService.updateStatus(statusCode, newStatus);
            System.out.println("thành công");
            model.addAttribute("message", "Trạng thái đơn hàng đã được cập nhật thành công!");
        } catch (Exception e) {
            model.addAttribute("message", "Có lỗi khi cập nhật trạng thái.");
        }

        // Trả về trang danh sách trạng thái
        return "admin/trang-thai-don-hang";
    }

    @PostMapping("/cancel-order/{shoppingCartCode}")
    public String cancelOrder1(@PathVariable String shoppingCartCode, Model model) {

        try {
            // Tìm giỏ hàng từ shoppingCartCode
            ShoppingCart shoppingCart = shoppingCartService.findByShoppingCartCode(shoppingCartCode);

            if (shoppingCart == null) {
                model.addAttribute("shoppingCartCode", shoppingCartCode);
                model.addAttribute("message", "Không tìm thấy giỏ hàng với mã: " + shoppingCartCode);
                return "error"; // Trả về trang lỗi
            }

            // Tìm trạng thái từ giỏ hàng
            Status status = statusRepository.findByShoppingCart_ShoppingCartCode(shoppingCartCode);

            if (status == null) {
                model.addAttribute("shoppingCartCode", shoppingCartCode);
                model.addAttribute("message", "Không tìm thấy trạng thái cho giỏ hàng: " + shoppingCartCode);
                return "error"; // Trả về trang lỗi
            }

            // Cập nhật trạng thái
            status.setNameStatus("Chờ xử lý");
            statusRepository.save(status);

            shoppingCart.setStatus(false);
            shoppingCartService.save(shoppingCart);

            model.addAttribute("message", "Giỏ hàng yêu cầu hủy thành công.");
            return "redirect:/notifications";
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/revoke-request/{shoppingCartCode}")
    public String revokeRequest(@PathVariable String shoppingCartCode, Model model) {
        ShoppingCart shoppingCart = shoppingCartService.findByShoppingCartCode(shoppingCartCode);
        Status status = statusRepository.findByShoppingCart_ShoppingCartCode(shoppingCartCode);

        if (shoppingCart == null || status == null) {
            model.addAttribute("message", "Không tìm thấy dữ liệu.");
            return "error";
        }

        status.setNameStatus("Đang được chuẩn bị");
        statusRepository.save(status);

        return "redirect:/admin/trang-thai-don-hang";
    }


//    @PostMapping("/put-cancel-order/{shoppingCartCode}")
//    public String cancelOrder(@PathVariable String shoppingCartCode, Model model) {
//        ShoppingCart shoppingCart = shoppingCartService.findByShoppingCartCode(shoppingCartCode);
//        Status status = statusRepository.findByShoppingCart_ShoppingCartCode(shoppingCartCode);
//
//        if (shoppingCart == null || status == null) {
//            model.addAttribute("message", "Không tìm thấy dữ liệu.");
//            return "error";
//        }
//
//        status.setNameStatus("Hủy");
//        statusRepository.save(status);
//
//        shoppingCart.setStatus(false);
//        shoppingCartService.save(shoppingCart);
//
//        return "redirect:/admin/trang-thai-don-hang";
//    }

    @PostMapping("/put-cancel-order/{shoppingCartCode}")
    public String cancelOrder(@PathVariable String shoppingCartCode, Model model) {
        ShoppingCart shoppingCart = shoppingCartService.findByShoppingCartCode(shoppingCartCode);
        Status status = statusRepository.findByShoppingCart_ShoppingCartCode(shoppingCartCode);

        if (shoppingCart == null || status == null) {
            model.addAttribute("message", "Không tìm thấy dữ liệu.");
            return "error";
        }

        // Lấy danh sách sản phẩm trong giỏ hàng
        List<ShoppingCartProducts> cartProducts = shoppingCartProductsRepository.findAllByShoppingCart_ShoppingCartCode(shoppingCartCode);

        if (cartProducts != null && !cartProducts.isEmpty()) {
            for (ShoppingCartProducts cartProduct : cartProducts) {
                Product product = cartProduct.getProduct();

                // Kiểm tra null
                if (product == null) {
                    continue; // Bỏ qua sản phẩm không hợp lệ
                }

                // Tìm sản phẩm chi tiết trong DetailedProduct dựa trên Product
                DetailedProduct detailedProduct = detailedProductRepository.findByProduct_ProductCode(product.getProductCode());

                if (detailedProduct != null) {
                    // Cộng lại số lượng từ giỏ hàng vào DetailedProduct
                    detailedProduct.setQuantity(detailedProduct.getQuantity() + cartProduct.getAmount());
                    detailedProductService.save(detailedProduct); // Lưu cập nhật vào DB

                    // Lấy danh sách Inventory liên quan đến sản phẩm này
                    List<Inventory> inventories = inventoryRepository.findAllByDetailedProduct(detailedProduct);

                    if (inventories != null && !inventories.isEmpty()) {
                        for (Inventory inventory : inventories) {
                            if ("sold".equalsIgnoreCase(inventory.getStatus())) {
                                inventory.setStatus("available"); // Cập nhật trạng thái
                                inventoryRepository.save(inventory); // Lưu cập nhật vào DB
                            }
                        }
                    }
                }
            }
        }

        // Cập nhật trạng thái đơn hàng và giỏ hàng
        status.setNameStatus("Hủy");
        statusRepository.save(status);

        shoppingCart.setStatus(false);
        shoppingCartService.save(shoppingCart);

        return "redirect:/admin/trang-thai-don-hang";
    }



    //@PostMapping("/put-cancel-order/{shoppingCartCode}")
//public String cancelOrder(@PathVariable String shoppingCartCode, Model model) {
//    ShoppingCart shoppingCart = shoppingCartService.findByShoppingCartCode(shoppingCartCode);
//    Status status = statusRepository.findByShoppingCart_ShoppingCartCode(shoppingCartCode);
//
//    if (shoppingCart == null || status == null) {
//        model.addAttribute("message", "Không tìm thấy dữ liệu.");
//        return "error";
//    }
//
//    // Lấy danh sách sản phẩm trong giỏ hàng
//    List<ShoppingCartProducts> cartProducts = shoppingCartProductsRepository.findAllByShoppingCart_ShoppingCartCode(shoppingCartCode);
//
//    if (cartProducts != null && !cartProducts.isEmpty()) {
//        for (ShoppingCartProducts cartProduct : cartProducts) {
//            Product product = cartProduct.getProduct();
//
//            // Kiểm tra null
//            if (product == null) {
//                continue; // Bỏ qua sản phẩm không hợp lệ
//            }
//
//            // Tìm sản phẩm chi tiết trong DetailedProduct dựa trên Product
//            DetailedProduct detailedProduct = detailedProductRepository.findByProduct_ProductCode(product.getProductCode());
//
//            if (detailedProduct != null) {
//                // Cộng lại số lượng từ giỏ hàng vào DetailedProduct
//                detailedProduct.setQuantity(detailedProduct.getQuantity() + cartProduct.getAmount());
//                detailedProductService.save(detailedProduct); // Lưu cập nhật vào DB
//            }
//        }
//    }
//
//    // Cập nhật trạng thái đơn hàng và giỏ hàng
//    status.setNameStatus("Hủy");
//    statusRepository.save(status);
//
//    shoppingCart.setStatus(false);
//    shoppingCartService.save(shoppingCart);
//
//    return "redirect:/admin/trang-thai-don-hang";
//}
    @GetMapping("/detail/{cartCode}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartDetail(@PathVariable String cartCode) {
        System.out.println("Received cartCode: " + cartCode);  // In ra cartCode nhận được từ client

        ShoppingCart cart = shoppingCartService.findByShoppingCartCode(cartCode);  // Trả về null nếu không tìm thấy

        if (cart != null) {
            System.out.println("Found cart with code: " + cart.getShoppingCartCode());  // In ra thông tin giỏ hàng nếu tìm thấy

            List<Map<String, Object>> items = cart.getShoppingCartProducts().stream().map(product -> {
                Map<String, Object> itemDetails = new HashMap<>();
                itemDetails.put("image", product.getProduct().getImage());
                itemDetails.put("quantity", product.getAmount());
//                itemDetails.put("price", product.getPrice());
                return itemDetails;
            }).collect(Collectors.toList());

            System.out.println("Cart items: " + items);  // In ra danh sách sản phẩm trong giỏ hàng

            Map<String, Object> response = new HashMap<>();
            response.put("cartCode", cart.getShoppingCartCode());
            response.put("items", items);

            return ResponseEntity.ok(response);
        } else {
            System.out.println("Cart with code " + cartCode + " not found.");  // In ra thông báo nếu không tìm thấy giỏ hàng
            return ResponseEntity.notFound().build();
        }
    }





}
