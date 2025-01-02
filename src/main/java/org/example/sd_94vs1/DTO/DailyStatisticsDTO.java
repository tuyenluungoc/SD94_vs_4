package org.example.sd_94vs1.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatisticsDTO {
    private LocalDate date;
    private BigDecimal totalRevenue;
}
