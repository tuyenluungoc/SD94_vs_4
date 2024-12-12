package org.example.sd_94vs1.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

// ShoppingCart.java

// ShoppingCartProducts.java

@Table(name = "shopping_cart_products")
public class ShoppingCartProducts {

    @Id
    @Column(name = "shopping_cart_product_code", length = 50, nullable = false)
    String shoppingCartProductCode;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_code", referencedColumnName = "shopping_cart_code", nullable = false)
    private ShoppingCart shoppingCart;  // Liên kết với ShoppingCart

    @ManyToOne
    @JoinColumn(name = "product_code", referencedColumnName = "product_code")
    private Product product;

    @Column(name = "amount")
    private int amount;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Transient // Đánh dấu là transient vì trường này không được lưu vào DB
    private BigDecimal price;

    @Override
    public String toString() {
        return "ShoppingCartProducts{" +
                "shoppingCartProductCode='" + shoppingCartProductCode + '\'' +
                ", shoppingCart=" + shoppingCart +
                ", product=" + product +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", price=" + price +
                '}';
    }
//    @OneToMany(mappedBy = "product")
//    private List<Inventory> inventories;
//
//    public List<String> getImeis() {
//        return inventories.stream()
//                .filter(inventory -> inventory.getImei() != null) // Lọc các Inventory có IMEI
//                .map(Inventory::getImei) // Lấy IMEI
//                .collect(Collectors.toList());
//    }
}
