package org.example.sd_94vs1.entity.oder;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.sd_94vs1.entity.product.Product;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @Column(name = "order_line_code", length = 10, nullable = false)
    String orderLineCode;

    @Column(name = "order_code", length = 10, nullable = false)
    String orderCode;

    @Column(name = "product_code", length = 10, nullable = false)
    String productCode;

    @Column(nullable = false)
    Integer quantity;

    @Column(name = "price_at_order", nullable = false)
    Double priceAtOrder;

    @ManyToOne
    @JoinColumn(name = "order_code", referencedColumnName = "order_code", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_code", referencedColumnName = "product_code", insertable = false, updatable = false)
    private Product product;

    @PrePersist
    public void prePersist() {
        // Any pre-persist logic (if needed)
    }

    @PreUpdate
    public void preUpdate() {
        // Any pre-update logic (if needed)
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "orderLineCode='" + orderLineCode + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", productCode='" + productCode + '\'' +
                ", quantity=" + quantity +
                ", priceAtOrder=" + priceAtOrder +
                ", order=" + order +
                ", product=" + product +
                '}';
    }
}
