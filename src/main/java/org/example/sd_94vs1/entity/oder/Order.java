package org.example.sd_94vs1.entity.oder;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.sd_94vs1.entity.ShoppingCart;
import org.example.sd_94vs1.entity.User;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_code", length = 10, nullable = false)
    String orderCode;

    @Column(name = "user_code", length = 10, nullable = false)
    String userCode;

    @Column(name = "shipping_address", length = 255, nullable = false)
    String shippingAddress;



    @Column(name = "from_shopping_cart_code", length = 10)
    String fromShoppingCartCode;

    @Column(name = "total_amount_money", nullable = false)
    Double totalAmountMoney;


    @Column(nullable = false)
    Boolean status;

    @Column(name = "approved_at")
    Date approvedAt;

    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "updated_at")
    Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_code", referencedColumnName = "user_code", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "from_shopping_cart_code", referencedColumnName = "shopping_cart_code", insertable = false, updatable = false)
    private ShoppingCart shoppingCart;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }
    @Override
    public String toString() {
        return "Order{" +
                "orderCode='" + orderCode + '\'' +
                ", userCode='" + userCode + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", fromShoppingCartCode='" + fromShoppingCartCode + '\'' +
                ", totalAmountMoney=" + totalAmountMoney +
                ", status=" + status +
                ", approvedAt=" + approvedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", user=" + user +
                ", shoppingCart=" + shoppingCart +
                '}';
    }
}
