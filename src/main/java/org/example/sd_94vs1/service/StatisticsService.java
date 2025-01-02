package org.example.sd_94vs1.service;

import org.example.sd_94vs1.DTO.DailyStatisticsDTO;
import org.example.sd_94vs1.DTO.MonthlyStatisticsDTO;
import org.example.sd_94vs1.DTO.TopSellingProductDTO;
import org.example.sd_94vs1.DTO.YearlyStatisticsDTO;
import org.example.sd_94vs1.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public List<MonthlyStatisticsDTO> getMonthlyStatistics(int year) {
        List<Object[]> results = statisticsRepository.getMonthlyStatistics(year);
        List<MonthlyStatisticsDTO> monthlyStatistics = new ArrayList<>();

        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Integer yearResult = (Integer) result[1];
            BigDecimal totalRevenue = (BigDecimal) result[2];

            monthlyStatistics.add(new MonthlyStatisticsDTO(month, yearResult, totalRevenue));
        }

        return monthlyStatistics;
    }


    public List<YearlyStatisticsDTO> getYearlyStatistics() {
        List<Object[]> results = statisticsRepository.getYearlyStatistics();
        List<YearlyStatisticsDTO> yearlyStatistics = new ArrayList<>();

        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            BigDecimal totalRevenue = (BigDecimal) result[1];

            yearlyStatistics.add(new YearlyStatisticsDTO(year, totalRevenue));
        }

        return yearlyStatistics;
    }


    public List<TopSellingProductDTO> getTopSellingProducts(int limit) {
        // Lấy kết quả từ repository
        List<Object[]> results = statisticsRepository.getTopSellingProducts(limit);

        // Chuyển đổi kết quả thành danh sách DTO
        List<TopSellingProductDTO> topSellingProducts = results.stream()
                .map(result -> new TopSellingProductDTO(
                        (String) result[0],    // productCode
                        (BigDecimal) result[1]        // totalQuantity
                ))
                .limit(limit)  // Giới hạn số lượng sản phẩm nếu cần
                .collect(Collectors.toList());

        return topSellingProducts;
    }
    public List<DailyStatisticsDTO> getDailyStatistics(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = statisticsRepository.getDailyStatistics(startDate, endDate);
        List<DailyStatisticsDTO> dailyStatistics = new ArrayList<>();

        for (Object[] result : results) {
            java.sql.Date sqlDate = (java.sql.Date) result[0];
            LocalDate date = sqlDate.toLocalDate();  // Chuyển đổi từ java.sql.Date sang LocalDate

            BigDecimal totalRevenue = (BigDecimal) result[1];
            dailyStatistics.add(new DailyStatisticsDTO(date, totalRevenue));
        }

        return dailyStatistics;
    }


}
