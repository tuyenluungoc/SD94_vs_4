package org.example.sd_94vs1.service;

import org.example.sd_94vs1.entity.Inventory;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.repository.InventoryRepository;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.example.sd_94vs1.repository.ShoppingCartProductsRepository;
import org.example.sd_94vs1.repository.ShoppingCartRepository;
import org.example.sd_94vs1.repository.WarrantyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private WarrantyRepo warrantyRepository;
    @Autowired
    private DetailedProductRepository detailedProductRepository;

    @Autowired
    private ShoppingCartProductsRepository shoppingCartProductsRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    public void generateAndSaveInventoryFromDetailedProduct() {
        // Lấy tất cả các bản ghi DetailedProduct
        List<DetailedProduct> detailedProducts = detailedProductRepository.findAll();
        if (detailedProducts.isEmpty()) {
            System.out.println("Không tìm thấy DetailedProduct với mã mn0001.");
            return;  // Kết thúc nếu không có bản ghi
        }
        // Duyệt qua từng DetailedProduct
        for (DetailedProduct detailedProduct : detailedProducts) {
            // Lấy thông tin Product từ DetailedProduct
            Product product = detailedProduct.getProduct();  // Giả sử là đã có quan hệ giữa DetailedProduct và Product

            // Tạo nhiều bản ghi Inventory từ mỗi DetailedProduct
            for (int i = 0; i < detailedProduct.getQuantity(); i++) {
                Inventory inventory = new Inventory();
                inventory.setInventoryCode(generateInventoryCode());  // Tạo mã inventoryCode ngẫu nhiên hoặc theo quy tắc của bạn
                inventory.setDetailedProduct(detailedProduct);  // Gắn DetailedProduct
                inventory.setProduct(product);  // Gắn Product
                inventory.setQuantity(1);  // Mỗi bản ghi đại diện cho một sản phẩm
                inventory.setStatus("available");  // Trạng thái mặc định là có sẵn
                inventory.setDateReceived(new Date());  // Ngày nhận
                inventory.setDateUpdated(new Date());  // Ngày cập nhật

                // Sinh IMEI cho Inventory
                String imei = generateUniqueImei(product, detailedProduct);
                inventory.setImei(imei);

                // Lưu Inventory vào cơ sở dữ liệu
                inventoryRepository.save(inventory);
            }
        }
    }






    // Phương thức tạo IMEI duy nhất
    private static int counter = 1;  // Biến static để lưu số thứ tự, bắt đầu từ 1

    private String generateUniqueImei(Product product, DetailedProduct detailedProduct) {
        // TAC: 6 ký tự từ productCode, đảm bảo bao gồm cả chữ và số
        String tac = generateAlphanumericCode(3, product.getProductCode());

        // FAC: Lấy 2 ký tự đầu từ detailedProductCode (bao gồm cả chữ và số)
        String fac = generateAlphanumericCode(2, detailedProduct.getDetailedProductCode());

        // SNR: 6 ký tự từ detailedProductCode (cũng bao gồm cả chữ và số)
        String snr = generateAlphanumericCode(2, detailedProduct.getDetailedProductCode());

        // Check digit: 1 ký tự ngẫu nhiên từ a-z hoặc 0-9
        String checkDigit = generateRandomCharOrDigit();

        // Kết hợp để tạo IMEI base
        String imeiBase = tac + fac + snr + checkDigit;

        // Thêm UUID để đảm bảo tính duy nhất, chỉ lấy phần đầu của UUID để không vượt quá độ dài 15 ký tự
        String uniqueImei = imeiBase + UUID.randomUUID().toString().replaceAll("-", "").substring(0, Math.max(0, 15 - imeiBase.length()));

        // Nếu IMEI vẫn dài hơn 15 ký tự, cắt lại
        if (uniqueImei.length() > 15) {
            uniqueImei = uniqueImei.substring(0, 15);
        }

        // Thêm STT vào cuối mã IMEI (Số thứ tự tăng dần)
        String stt = String.format("%04d", counter);  // Đảm bảo STT có 4 chữ số
        uniqueImei = uniqueImei + stt;

        // Tăng counter lên 1 cho lần gọi tiếp theo
        counter++;

        // Đảm bảo độ dài IMEI không vượt quá giới hạn của cột trong cơ sở dữ liệu
        if (uniqueImei.length() > 15) {
            uniqueImei = uniqueImei.substring(0, 15);  // Cắt lại nếu cần
        }

        return uniqueImei;
    }


    // Hàm tạo số ngẫu nhiên với độ dài nhất định (STT)
    private String generateRandomNumber(int length) {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < length; i++) {
            number.append((int) (Math.random() * 10));  // Tạo số ngẫu nhiên từ 0-9
        }
        return number.toString();
    }

    // Hàm tạo chuỗi alphanumeric từ hashCode
    private String generateAlphanumericCode(int length, String input) {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();

        // Sử dụng hashCode của input để tạo alphanumeric
        int hash = Math.abs(input.hashCode());
        for (int i = 0; i < length; i++) {
            int index = hash % alphanumeric.length();
            result.append(alphanumeric.charAt(index));
            hash /= alphanumeric.length();
        }
        return result.toString();
    }

    // Hàm tạo ký tự hoặc số ngẫu nhiên
    private String generateRandomCharOrDigit() {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int randomIndex = (int) (Math.random() * alphanumeric.length());
        return String.valueOf(alphanumeric.charAt(randomIndex));
    }




    // Phương thức tạo mã InventoryCode ngẫu nhiên hoặc theo quy tắc của bạn
    private String generateInventoryCode() {
        return "iv" + (int) (Math.random() * 1000000);
    }






}