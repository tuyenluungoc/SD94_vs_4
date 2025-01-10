package org.example.sd_94vs1.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.sd_94vs1.entity.product.Product;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "detailed_products")
public class DetailedProduct {
    @Id
    @Column(name = "detailed_product_code", length = 50, nullable = false)
    String detailedProductCode;

    @ManyToOne
    @JoinColumn(name = "product_code", referencedColumnName = "product_code", nullable = false)
    Product product;

    @Column(name = "description", columnDefinition = "TEXT") // Đổi từ 'describe' thành 'description'
    String description;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "price_vnd", precision = 10, scale = 2)
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal priceVND;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    Date date;

    @Column(name = "edit_date")
    @Temporal(TemporalType.DATE)
    Date editDate;

    @PrePersist
    protected void onCreate() {
        date = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        editDate = new Date();
    }

    @Override
    public String toString() {
        return "DetailedProduct{" +
                "detailedProductCode='" + detailedProductCode + '\'' +
                ", product=" + product +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", priceVND=" + priceVND +
                ", date=" + date +
                ", editDate=" + editDate +
                '}';


    }



//    public Date getWarrantyEndDate() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.MONTH, 12); // Thêm 12 tháng
//        return calendar.getTime();
//    }
}