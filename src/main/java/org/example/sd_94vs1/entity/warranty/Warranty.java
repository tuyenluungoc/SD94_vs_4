package org.example.sd_94vs1.entity.warranty;

import jakarta.persistence.*;
import lombok.*;
import org.example.sd_94vs1.entity.Inventory;

import java.util.Calendar;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "warranty")
public class Warranty {

    @Id
    @Column(name = "warranty_code", nullable = false, length = 50)
    private String warrantyCode = "wc" + String.format("%05d", (int)(Math.random() * 100000));

    @ManyToOne
    @JoinColumn(name = "inventory_code", referencedColumnName = "inventory_code")
    private Inventory inventory; // Liên kết với Inventory để tra cứu IMEI

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "warranty_status", length = 50)
    private String warrantyStatus;

    @Column(name = "terms", length = 255)
    private String terms;

    // Bạn có thể tạo phương thức giúp lấy IMEI từ Inventory
    public String getImei() {
        if (inventory != null) {
            return inventory.getImei();
        }
        return null;
    }

    public Date getWarrantyEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 12); // Thêm 12 tháng
        return calendar.getTime();
    }


}