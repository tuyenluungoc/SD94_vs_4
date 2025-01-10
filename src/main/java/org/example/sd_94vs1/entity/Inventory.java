package org.example.sd_94vs1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;

import java.util.Calendar;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "inventory_code", nullable = false, length = 50)
    private String inventoryCode;

    @ManyToOne
    @JoinColumn(name = "product_code", referencedColumnName = "product_code")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "id_detailed_product", referencedColumnName = "detailed_product_code")
    private DetailedProduct detailedProduct;

    @Column(name = "imei", unique = true, length = 15)
    private String imei;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "date_received")
    @Temporal(TemporalType.DATE)
    private Date dateReceived;

    @Column(name = "date_updated")
    @Temporal(TemporalType.DATE)
    private Date dateUpdated;
    @PrePersist
    protected void generateImei() {
        if (imei == null || imei.isEmpty()) {
            // Lấy TAC từ productCode
            String tac = String.format("%06d", product.getProductCode().hashCode() % 1000000);

            // Lấy FAC từ mã chi tiết sản phẩm
            String fac = detailedProduct.getDetailedProductCode().substring(0, 2);

            // Lấy SNR từ detailedProductCode
            String snr = String.format("%06d", detailedProduct.getDetailedProductCode().hashCode() % 1000000);

            // Tạo check digit ngẫu nhiên (hoặc có thể sử dụng thuật toán tính toán)
            int checkDigit = (int) (Math.random() * 10);

            // Kết hợp các phần lại để tạo IMEI
            imei = tac + fac + snr + checkDigit;
        }
    }


}