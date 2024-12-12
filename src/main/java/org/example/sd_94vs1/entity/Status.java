package org.example.sd_94vs1.entity;

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
@Table(name = "status")
public class Status {
    @Id
    @Column(name = "status_code", nullable = false, length = 50)
    private String statusCode = "stt" + String.format("%05d", (int)(Math.random() * 100000));
    @Column(name = "name_status", length = 50)
    private String nameStatus;
    @ManyToOne
    @JoinColumn(name = "user_code", referencedColumnName = "user_code")
    private User user;
    @ManyToOne
    @JoinColumn(name = "from_shopping_cart_code", referencedColumnName = "shopping_cart_code", nullable = false)
    private ShoppingCart shoppingCart;
}
