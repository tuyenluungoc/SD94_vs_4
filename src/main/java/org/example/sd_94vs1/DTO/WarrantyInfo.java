package org.example.sd_94vs1.DTO;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WarrantyInfo {
    private String productName;
    private String warrantyStatus;
    private Date endDate;
    private String productImage;
}
