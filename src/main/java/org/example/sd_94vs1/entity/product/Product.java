package org.example.sd_94vs1.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_code", length = 50, nullable = false)
    String productCode;

    @Column(name = "name", length = 255)
    String name;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @ManyToOne
    @JoinColumn(name = "manufacturer_code", referencedColumnName = "manufacturer_code")
    Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "category_code", referencedColumnName = "category_code")
    Category category;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    Date date;

    @Column(name = "edit_date")
    @Temporal(TemporalType.DATE)
    Date editDate;

    @Column(name = "status", length = 50)
    String status;

    @Column(name = "image", length = 255)
    String image;

    @ManyToOne
    @JoinColumn(name = "product_type_code", referencedColumnName = "product_type_code")
    ProductType productType;

    @PrePersist
    protected void onCreate() {
        date = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        editDate = new Date();
    }
    // Thêm trường priceVND
    @Transient
    private BigDecimal priceVND;

    @Override
    public String toString() {
        return "Product{" +
                "productCode='" + productCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manufacturer=" + manufacturer +
                ", category=" + category +
                ", date=" + date +
                ", editDate=" + editDate +
                ", status='" + status + '\'' +
                ", image='" + image + '\'' +
                ", productType=" + productType +
                ", priceVND=" + priceVND +
                '}';
    }
}
