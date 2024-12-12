package org.example.sd_94vs1.entity.warranty;

import jakarta.persistence.*;
import lombok.*;
import org.example.sd_94vs1.entity.User;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "warranty_claim")
public class WarrantyClaim {

    @Id
    @Column(name = "claim_code", nullable = false, length = 50)
    private String claimCode = "cl" + String.format("%05d", (int)(Math.random() * 100000));

    @ManyToOne
    @JoinColumn(name = "warranty_code", referencedColumnName = "warranty_code")
    private Warranty warranty;

    @Column(name = "claim_date")
    @Temporal(TemporalType.DATE)
    private Date claimDate;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "resolution", length = 255)
    private String resolution;

    @Column(name = "claim_status", length = 50)
    private String claimStatus;

    @ManyToOne
    @JoinColumn(name = "user_code", referencedColumnName = "user_code")
    private User user;
}