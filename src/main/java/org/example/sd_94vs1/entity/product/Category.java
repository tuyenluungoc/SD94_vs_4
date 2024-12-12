package org.example.sd_94vs1.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "category_code", length = 50, nullable = false)
    String categoryCode;

    @Column(name = "name", length = 255, nullable = false)
    String name;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    Date date;

    @Column(name = "edit_date")
    @Temporal(TemporalType.DATE)
    Date editDate;

    @Column(name = "status", length = 50)
    String status;
    String img;

    @Override
    public String toString() {
        return "Category{" +
                "categoryCode='" + categoryCode + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", editDate=" + editDate +
                ", status='" + status + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
