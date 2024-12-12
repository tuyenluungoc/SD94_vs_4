package org.example.sd_94vs1.controller.payment;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import org.example.sd_94vs1.DTO.InventoryDTO;
import org.example.sd_94vs1.config.Config;
import org.example.sd_94vs1.entity.*;
import org.example.sd_94vs1.entity.oder.Order;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.entity.warranty.Warranty;
import org.example.sd_94vs1.repository.InventoryRepository;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.example.sd_94vs1.repository.Product.ProductRepository;
import org.example.sd_94vs1.repository.ShoppingCartRepository;
import org.example.sd_94vs1.repository.StatusRepository;
import org.example.sd_94vs1.repository.WarrantyRepo;
import org.example.sd_94vs1.repository.oder.OrderRepository;
import org.example.sd_94vs1.service.InventoryService;
import org.example.sd_94vs1.service.ShoppingCartProductsService;
import org.example.sd_94vs1.service.ShoppingCartService;
import org.example.sd_94vs1.service.email.EmailService;
import org.example.sd_94vs1.service.email.PdfInvoiceService;
import org.example.sd_94vs1.service.oder.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
public class PaymentController {
    //    private final CartService cartService;
    private final ShoppingCartService shoppingCartService;
    private final HttpSession session;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final ShoppingCartProductsService shoppingCartProductsService;
    private final EmailService emailService;
    private final OrderService orderService;
    private final PdfInvoiceService pdfInvoiceService;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final WarrantyRepo warrantyRepository;
    private final ProductRepository productRepository;
    private final DetailedProductRepository detailedProductRepository;
    private final StatusRepository statusRepository ;



    @GetMapping("/create_payment")
    public ResponseEntity<String> createPayment(@RequestParam long amount, @RequestParam String id) throws Exception {

        // Nếu đã đăng nhập, tiếp tục với logic thanh toán
        String vnp_Version = Config.vnp_Version;
        String vnp_Command = Config.vnp_Command;
        String vnp_OrderInfo = Config.getRandomNumber(8);
        String orderType = Config.vnp_OrderType;
        String vnp_TxnRef = "thanh toan don hang: " + id;
        String vnp_IpAddr = "14.248.82.236";
        String vnp_TmnCode = Config.vnp_TmnCode;
        long amount_a = amount * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount_a));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        String htmlResponse = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Redirecting...</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Redirecting to payment gateway...</p>\n" +
                "    <script type=\"text/javascript\">\n" +
                "        window.location.href = \"" + paymentUrl + "\";\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlResponse);
    }

    public Inventory convertToEntity(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();

        // Gán Inventory Code
        inventory.setInventoryCode(inventoryDTO.getInventoryCode());

        // Lấy Product từ ProductRepository
        Product product = productRepository.findByProductCodeAndProductType_ProductTypeCode(
                inventoryDTO.getProductCode(),
                inventoryDTO.getProductTypeCode()
        );
        inventory.setProduct(product);

        // Gán các giá trị khác
        inventory.setStatus("sold");
        inventory.setDateReceived(inventoryDTO.getDateReceived());
        inventory.setDateUpdated(new Date());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setImei(inventoryDTO.getImei());
        if (inventoryDTO.getIdDetailedProduct() != null) {
            DetailedProduct detailedProduct = detailedProductRepository.findById(inventoryDTO.getIdDetailedProduct())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy DetailedProduct với ID: " + inventoryDTO.getIdDetailedProduct()));
            inventory.setDetailedProduct(detailedProduct);
        }

        return inventory;
    }

    public List<InventoryDTO> mapToInventoryDTOList(List<Object[]> queryResults) {
        List<InventoryDTO> inventoryDTOList = new ArrayList<>();
        for (Object[] result : queryResults) {
            InventoryDTO dto = new InventoryDTO();

            // Gán các giá trị với ép kiểu chính xác
            dto.setInventoryCode(result[0].toString()); // inventoryCode
            dto.setProductCode(result[1].toString());   // productCode
            dto.setStatus(result[2].toString());        // status
            dto.setDateReceived((Date) result[3]);      // dateReceived
            dto.setProductTypeCode(result[4].toString()); // productTypeCode

            // Xử lý thêm các trường tùy chọn nếu có
            if (result.length > 5 && result[5] != null) {
                dto.setImei(result[5].toString()); // imei
            }
            if (result.length > 6 && result[6] != null) {
                dto.setQuantity((Integer) result[6]); // quantity
            }
            if (result.length > 7 && result[7] != null) {
                dto.setIdDetailedProduct(result[7].toString()); // idDetailedProduct
            }

            inventoryDTOList.add(dto);
        }
        return inventoryDTOList;
    }


    @GetMapping("/vnpay_return")
    public ResponseEntity<byte[]>  vnpayReturn(HttpServletRequest request) {
        Map<String, String> vnp_Params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        // Lấy các tham số từ request và đưa vào map
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            vnp_Params.put(entry.getKey(), entry.getValue()[0]);
        }
        // Lấy vnp_SecureHash từ request và loại bỏ khỏi map
        String vnp_SecureHash = vnp_Params.remove("vnp_SecureHash");
        // Tạo chuỗi hash data từ các tham số đã sắp xếp
        StringBuilder hashData = new StringBuilder();
        vnp_Params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value != null && !value.isEmpty()) {
                        try {
                            hashData.append(key).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        String hashString = hashData.toString();
        if (hashString.endsWith("&")) {
            hashString = hashString.substring(0, hashString.length() - 1);
        }

        // Tính toán Secure Hash để xác minh
        String computedSecureHash = Config.hmacSHA512(Config.secretKey, hashString);

        HttpHeaders headers = new HttpHeaders();

        if (computedSecureHash.equals(vnp_SecureHash)) {
            // Lấy trạng thái giao dịch
            String transactionStatus = vnp_Params.get("vnp_TransactionStatus");
            String rawShoppingCartCode = vnp_Params.get("vnp_TxnRef");
            String shoppingCartCode;

            if (rawShoppingCartCode != null && rawShoppingCartCode.contains(":")) {
                shoppingCartCode = rawShoppingCartCode.split(":")[1].trim();
            } else {
                shoppingCartCode = rawShoppingCartCode;
            }

            System.out.println("Mã giỏ hàng sau khi xử lý: " + shoppingCartCode);

            if (shoppingCartCode == null || shoppingCartCode.isEmpty()) {
                System.out.println("Mã giỏ hàng không hợp lệ.");
                headers.setLocation(URI.create("http://localhost:8080/"));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }

            if ("00".equals(transactionStatus)) {
                try {
                    List<ShoppingCartProducts> shoppingCartProducts = shoppingCartProductsService.getShoppingCartProductsByCode(shoppingCartCode);
// gọi trừ
                    shoppingCartProductsService.updateDetailedProductQuantitiesAfterPayment(shoppingCartCode);
// tìm
                    Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartCode);
                    if (optionalShoppingCart.isPresent()) {
                        ShoppingCart shoppingCart = optionalShoppingCart.get();
// cập nhật trạng thái giỏ hàng
                        shoppingCart.setStatus(true);
                        shoppingCart.setUpdatedAt(new Date());
                        shoppingCartRepository.save(shoppingCart);
                        // Lấy email từ giỏ hàng
                        List<Order> orders = orderService.findOrdersByShoppingCartCode(shoppingCartCode);

                        for (ShoppingCartProducts scProduct : shoppingCartProducts) {
                            String productCode = scProduct.getProduct().getProductCode();
                            String productTypeCode = scProduct.getProduct().getProductType().getProductTypeCode();
                            int quantity = scProduct.getAmount();

                            List<Object[]> queryResults = inventoryRepository.findAvailableInventories(productCode, productTypeCode, quantity);

// Chuyển đổi từ Object[] thành InventoryDTO
                            List<InventoryDTO> availableInventories = mapToInventoryDTOList(queryResults);

                            // Kiểm tra số lượng sản phẩm có sẵn
                            if (availableInventories.size() < quantity) {
                                throw new IllegalStateException("Không đủ sản phẩm trong kho để xử lý.");
                            }

                            // Cập nhật trạng thái của các Inventory và tạo Warranty
                            List<Warranty> warranties = new ArrayList<>();
                            for (int i = 0; i < quantity; i++) {
                                InventoryDTO inventoryDTO = availableInventories.get(i);

                                // Chuyển InventoryDTO thành Inventory Entity bằng phương thức convertToEntity
                                Inventory inventory = convertToEntity(inventoryDTO);

                                // Lưu Inventory Entity vào cơ sở dữ liệu
                                inventoryRepository.save(inventory);

                                // Tạo mới Warranty
                                Warranty warranty = new Warranty();
                                warranty.setInventory(inventory);
                                warranty.setStartDate(new Date());
                                warranty.setTerms("Bảo hành có hiệu lực từ ngày bán , hiệu lực 12 tháng ");
                                warranty.setEndDate(warranty.getWarrantyEndDate());
                                warranty.setWarrantyStatus("active");
                                warranties.add(warranty);
                            }

// Lưu tất cả warranty
                            warrantyRepository.saveAll(warranties);

                        }

                        // Lấy thông tin người dùng từ session
                        HttpSession session = request.getSession();
                        User user = (User) session.getAttribute("currentUser");

                        Optional<ShoppingCart> optionalShoppingCart2 = shoppingCartRepository.findByShoppingCartCode(shoppingCartCode);
//
                        if (optionalShoppingCart2.isPresent()) {
                            ShoppingCart shoppingCart2 = optionalShoppingCart2.get();
//
                            // Tạo mới Status
                            Status newStatus = new Status();
                            newStatus.setStatusCode("stt" + UUID.randomUUID().toString().substring(0, 8));
                            newStatus.setNameStatus("Đang được chuẩn bị");
                            newStatus.setUser(user);
                            newStatus.setShoppingCart(shoppingCart2);
                            System.out.println("Đã tạo Status: " + newStatus);

                            // Lưu Status vào cơ sở dữ liệu
                            statusRepository.save(newStatus);
                            System.out.println("Lưu Status thành công!");
                        } else {
                            System.out.println("Không tìm thấy ShoppingCart với mã: " );
                        }

// Duyệt qua từng đơn hàng và gửi email
                        for (Order order : orders) {
                            String customerEmail = order.getUser().getEmail(); // Lấy email của khách hàng từ đơn hàng
                            String orderCode = order.getOrderCode(); // Lấy mã hóa đơn

                            String subject = "Xác nhận thanh toán thành công";
                            String content = String.format(
                                    "Xin chào %s,\n\nCảm ơn bạn đã mua hàng tại cửa hàng 2tcamera.\n"
                                            + "Đơn hàng của bạn với mã giỏ hàng %s và mã hóa đơn %s đã được thanh toán thành công.\n\n"
                                            + "Đội ngũ chăm sóc khách hàng sẽ liên hệ với bạn sớm nhất.\n\n"
                                            + "Mọi thắc mắc xin liên hệ với đội ngũ tư vấn: 093431055.\n\n"
                                            + "Chúc bạn một ngày vui vẻ!\nĐội ngũ hỗ trợ khách hàng.",
                                    order.getUser().getName(), shoppingCartCode, orderCode
                            );
                            emailService.sendEmail(customerEmail, subject, content);
                        }



                        System.out.println("Cập nhật trạng thái thành công cho giỏ hàng: " + shoppingCartCode);
                    } else {
                        System.out.println("Không tìm thấy giỏ hàng với mã: " + shoppingCartCode);
                    }

                    int updatedRows = orderRepository.updateOrderStatus(shoppingCartCode);
                    if (updatedRows > 0) {
                        System.out.println("Cập nhật trạng thái đơn hàng thành công.");
                    } else {
                        System.out.println("Không tìm thấy đơn hàng cần cập nhật.");
                    }
                } catch (Exception e) {
                    System.out.println("Lỗi khi xử lý thanh toán: " + e.getMessage());
                    headers.setLocation(URI.create("http://localhost:8080/error"));
                    return new ResponseEntity<>(headers, HttpStatus.FOUND);
                }
            } else { // Giao dịch thất bại
                System.out.println("Giao dịch bị hủy. Không thay đổi trạng thái của giỏ hàng và đơn hàng.");
            }

            // Chuyển hướng về trang chủ
            headers.setLocation(URI.create("http://localhost:8080/"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            // Trường hợp xác minh hash thất bại
            System.out.println("Hash validation failed.");
            headers.setLocation(URI.create("http://localhost:8080/"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }







}
