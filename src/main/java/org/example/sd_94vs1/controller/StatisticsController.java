package org.example.sd_94vs1.controller;

import org.example.sd_94vs1.DTO.DailyStatisticsDTO;
import org.example.sd_94vs1.DTO.MonthlyStatisticsDTO;
import org.example.sd_94vs1.DTO.TopSellingProductDTO;
import org.example.sd_94vs1.DTO.YearlyStatisticsDTO;
import org.example.sd_94vs1.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/api/statistics/monthly")
    public List<MonthlyStatisticsDTO> getMonthlyStats(@RequestParam int year) {
        return statisticsService.getMonthlyStatistics(year);
    }

    @GetMapping("/api/statistics/yearly")
    public List<YearlyStatisticsDTO> getYearlyStats() {
        return statisticsService.getYearlyStatistics();
    }

    @GetMapping("/api/statistics/top-products")
    public List<TopSellingProductDTO> getTopSellingProducts(@RequestParam int limit) {
        return statisticsService.getTopSellingProducts(limit);
    }
    @GetMapping("/api/statistics/daily")
    public List<DailyStatisticsDTO> getDailyStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return statisticsService.getDailyStatistics(startDate, endDate);
    }

}