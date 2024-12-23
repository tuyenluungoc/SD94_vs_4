package org.example.sd_94vs1.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class YearlyStatisticsDTO {
    private int year;
    private BigDecimal totalRevenue;
}
