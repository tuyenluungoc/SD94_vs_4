package org.example.sd_94vs1.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product_types")
public class ProductType {
    @Id
    @Column(name = "product_type_code", length = 50, nullable = false)
    String productTypeCode;

    @Column(name = "product_type_name", length = 255, nullable = false)
    String productTypeName;

    @Column(name = "description", length = 255)
    String description;

    @Override
    public String toString() {
        return "ProductType{" +
                "productTypeCode='" + productTypeCode + '\'' +
                ", productTypeName='" + productTypeName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
