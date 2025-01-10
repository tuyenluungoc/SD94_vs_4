package org.example.sd_94vs1.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private String inventoryCode;
    private String productCode;
    private String status;
    private Date dateReceived;
    private String imei;
    private int quantity;
    private String idDetailedProduct;
    private String productTypeCode;
}
