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
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @Column(name = "manufacturer_code", length = 50, nullable = false)
    String manufacturerCode;

    @Column(name = "name", length = 255, nullable = false)
    String name;

    @Column(name = "country", length = 100)
    String country;

    @Override
    public String toString() {
        return "Manufacturer{" +
                "manufacturerCode='" + manufacturerCode + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
