package org.example.sd_94vs1.service;

import org.example.sd_94vs1.DTO.WarrantyInfo;
import org.example.sd_94vs1.entity.Inventory;
import org.example.sd_94vs1.entity.warranty.Warranty;
import org.example.sd_94vs1.repository.InventoryRepository;
import org.example.sd_94vs1.repository.WarrantyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarrantyService {
    @Autowired
    WarrantyRepo warrantyRepository;
    @Autowired
    InventoryRepository inventoryRepository;


//    public Map<String, Object> findWarrantyByImei(String imei) {
//        Map<String, Object> result = new HashMap<>();
//
//        // Tìm Inventory dựa trên IMEI
//        Optional<Inventory> inventoryOpt = inventoryRepository.findByImei(imei);
//        if (inventoryOpt.isEmpty()) {
//            result.put("errorMessage", "IMEI không tồn tại.");
//            return result;
//        }
//
//        Inventory inventory = inventoryOpt.get();
//
//        // Tìm Warranty dựa trên Inventory
//        Optional<Warranty> warrantyOpt = warrantyRepository.findByInventory_InventoryCode(inventory.getInventoryCode());
//        if (warrantyOpt.isEmpty()) {
//            result.put("errorMessage", "Không có thông tin bảo hành cho sản phẩm này.");
//            return result;
//        }
//
//        Warranty warranty = warrantyOpt.get();
//
//        // Kiểm tra thời gian bảo hành
//        Date today = new Date();
//        if (warranty.getEndDate().before(today)) {
//            result.put("errorMessage", "Sản phẩm đã hết hạn bảo hành.");
//        } else {
//            result.put("productName", inventory.getProduct().getName()); // Tên sản phẩm
//            result.put("warrantyStatus", warranty.getWarrantyStatus());         // Trạng thái bảo hành
//            result.put("endDate", warranty.getEndDate());                      // Ngày kết thúc bảo hành
//        }
//
//        return result;
//    }






    public WarrantyInfo getWarrantyInfoByImei(String imei) {
        // Tìm Inventory dựa trên IMEI
        Optional<Inventory> inventory = inventoryRepository.findByImei(imei);

        if (inventory.isPresent()) {
            // Tìm Warranty liên quan đến Inventory
            Optional<Warranty> warranty = warrantyRepository.findByInventory_InventoryCode(inventory.get().getInventoryCode());

            if (warranty.isPresent()) {
                return new WarrantyInfo(
                        inventory.get().getProduct().getName(),          // Tên sản phẩm
                        warranty.get().getWarrantyStatus(),              // Trạng thái bảo hành
                        warranty.get().getEndDate(),                     // Ngày hết hạn bảo hành
                        inventory.get().getProduct().getImage()          // Hình ảnh sản phẩm
                );
            } else {
                throw new RuntimeException("Không tìm thấy bảo hành cho sản phẩm này.");
            }
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm với IMEI này.");
        }
    }



}
