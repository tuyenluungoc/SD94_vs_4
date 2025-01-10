package org.example.sd_94vs1.controller;

import org.example.sd_94vs1.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // Endpoint để tạo Inventory và IMEI từ DetailedProduct
    @GetMapping("/generate-inventory")
    public String generateInventory() {
        inventoryService.generateAndSaveInventoryFromDetailedProduct();
        return "Inventory và IMEI đã được tạo và lưu vào cơ sở dữ liệu!";
    }
}
