package org.example.sd_94vs1.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

// ShoppingCart.java

@Table(name = "ShoppingCart")
public class ShoppingCart {

    @Id
    @Column(name = "shopping_cart_code", length = 50, nullable = false)
    String shoppingCartCode;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "authorAdmin_id")
    private User useradmin;

    @Column(name = "status", length = 25)
    Boolean status;
    Date createdAt;
    Date updatedAt;
    Date publishedAt;
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCartProducts> shoppingCartProducts; // Chú ý thuộc tính này





    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = new Date();
        if (status) {
            publishedAt = new Date();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
        if (status) {
            publishedAt = new Date();
        } else {
            publishedAt = null;
        }
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "shoppingCartCode='" + shoppingCartCode + '\'' +
                ", user=" + user +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", publishedAt=" + publishedAt +
                ", shoppingCartProducts=" + shoppingCartProducts +
                '}';
    }
}


