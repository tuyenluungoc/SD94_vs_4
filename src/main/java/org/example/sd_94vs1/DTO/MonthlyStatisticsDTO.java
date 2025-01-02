package org.example.sd_94vs1.DTO;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyStatisticsDTO {
    private Integer month;
    private Integer year;
    private BigDecimal totalRevenue;


}