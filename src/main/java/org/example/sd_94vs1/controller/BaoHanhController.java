package org.example.sd_94vs1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.sd_94vs1.entity.Inventory;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.entity.warranty.Warranty;
import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.example.sd_94vs1.repository.InventoryRepository;
import org.example.sd_94vs1.repository.WarrantyRepo;
import org.example.sd_94vs1.service.WarrantyClaimService;
import org.example.sd_94vs1.service.WarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/bao-hanh")
public class BaoHanhController {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    WarrantyRepo warrantyRepository;

    @Autowired
    private WarrantyClaimService warrantyClaimService;
    @Autowired
    private WarrantyService warrantyService;

    @GetMapping("/bao-hanh")
    public String getBaoHanhPage(Model model, HttpSession session) {
        return "web/bao-hanh";
    }

    @GetMapping("/validate-imei/{imei}")
    public ResponseEntity<Warranty> getWarrantyByImei(@PathVariable String imei) {
        try {
            Optional<Inventory> inventory = inventoryRepository.findByImei(imei);
            if (inventory.isPresent()) {
                Warranty warranty = warrantyRepository.findByInventory(inventory.get());
                if (warranty != null) {
                    return ResponseEntity.ok(warranty);
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @PostMapping("/yeu-cau-bao-hanh/{imei}")
//    public ResponseEntity<String> createWarrantyClaim(@RequestBody Map<String, Object> payload, @PathVariable String imei) {
//        try {
//            Optional<Inventory> inventory = inventoryRepository.findByImei(imei);
//            if (inventory.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IMEI không hợp lệ!");
//            }
//
//            Warranty warranty = warrantyRepository.findByInventory(inventory.get());
//            if (warranty == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy bảo hành!");
//            }
//
//            // Lấy danh sách lý do bảo hành
//            @SuppressWarnings("unchecked")
//            List<String> issues = (List<String>) payload.get("issues");
//            String resolution = (String) payload.get("resolution");
//            String claimStatus = (String) payload.get("claimStatus");
//
//            WarrantyClaim warrantyClaim = new WarrantyClaim();
//            warrantyClaim.setWarranty(warranty);
//            warrantyClaim.setDescription(String.join(", ", issues)); // Gộp danh sách lý do thành chuỗi
//            warrantyClaim.setResolution(resolution);
//            warrantyClaim.setClaimStatus(claimStatus);
//
//            warrantyClaimService.createWarrantyClaim(warrantyClaim);
//
//            return ResponseEntity.ok("Yêu cầu bảo hành đã được thêm thành công!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
//        }
//    }
//

    @PostMapping("/yeu-cau-bao-hanh/{imei}")
    public ResponseEntity<String> createWarrantyClaim(
            @RequestBody Map<String, Object> payload,
            @PathVariable String imei,
            HttpSession session) {
        try {
            // Lấy thông tin User từ session
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
            }

            Optional<Inventory> inventory = inventoryRepository.findByImei(imei);
            if (inventory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IMEI không hợp lệ!");
            }

            Warranty warranty = warrantyRepository.findByInventory(inventory.get());
            if (warranty == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy bảo hành!");
            }

            // Lấy danh sách lý do bảo hành từ payload
            @SuppressWarnings("unchecked")
            List<String> issues = (List<String>) payload.get("issues");
            String resolution = (String) payload.get("resolution");
            String claimStatus = (String) payload.get("claimStatus");

            // Tạo đối tượng WarrantyClaim và gán giá trị
            WarrantyClaim warrantyClaim = new WarrantyClaim();
            warrantyClaim.setWarranty(warranty);
            warrantyClaim.setDescription(String.join(", ", issues)); // Gộp danh sách lý do thành chuỗi
            warrantyClaim.setResolution(resolution);
            warrantyClaim.setClaimStatus(claimStatus);
            warrantyClaim.setUser(currentUser); // Gán thông tin User từ session

            // Lưu yêu cầu bảo hành
            warrantyClaimService.createWarrantyClaim(warrantyClaim);

            return ResponseEntity.ok("Yêu cầu bảo hành đã được thêm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }





    @GetMapping("/search")
    public String searchWarranty(@RequestParam("imei") String imei, Model model) {
        try {
            var warrantyInfo = warrantyService.getWarrantyInfoByImei(imei);

            model.addAttribute("productName", warrantyInfo.getProductName());
            model.addAttribute("warrantyStatus", warrantyInfo.getWarrantyStatus());
            model.addAttribute("endDate", warrantyInfo.getEndDate());
            model.addAttribute("productImage", warrantyInfo.getProductImage());
        } catch (RuntimeException e) {
            // Thêm thông báo lỗi vào model nếu không tìm thấy thông tin
            model.addAttribute("errorMessage", "Không tìm thấy thông tin bảo hành cho IMEI này.");
        }

        return "web/bao-hanh";
    }



}
//  return "web/bao-hanh";