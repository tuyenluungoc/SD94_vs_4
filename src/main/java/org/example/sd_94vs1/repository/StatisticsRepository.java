package org.example.sd_94vs1.repository;

import org.example.sd_94vs1.entity.oder.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT MONTH(o.created_at) AS month, YEAR(o.created_at) AS year, SUM(ol.quantity * ol.price_at_order) AS totalRevenue " +
            "FROM orders o " +
            "JOIN order_lines ol ON o.order_code = ol.order_code " +
            "WHERE YEAR(o.created_at) = :year " +
            "GROUP BY YEAR(o.created_at), MONTH(o.created_at) " +
            "ORDER BY YEAR(o.created_at), MONTH(o.created_at)", nativeQuery = true)
    List<Object[]> getMonthlyStatistics(@Param("year") int year);

    @Query(value = "SELECT YEAR(o.created_at) AS year, SUM(ol.quantity * ol.price_at_order) AS totalRevenue " +
            "FROM orders o " +
            "JOIN order_lines ol ON o.order_code = ol.order_code " +
            "GROUP BY YEAR(o.created_at) " +
            "ORDER BY YEAR(o.created_at)", nativeQuery = true)
    List<Object[]> getYearlyStatistics();


    @Query(value = "SELECT ol.product_code AS productCode, SUM(ol.quantity) AS totalQuantity " +
            "FROM order_lines ol " +
            "GROUP BY ol.product_code " +
            "ORDER BY SUM(ol.quantity) DESC", nativeQuery = true)
    List<Object[]> getTopSellingProducts(@Param("limit") int limit);

    @Query(value = "SELECT DATE(o.created_at) AS date, SUM(ol.quantity * ol.price_at_order) AS totalRevenue\n" +
            "FROM orders o\n" +
            "JOIN order_lines ol ON o.order_code = ol.order_code\n" +
            "WHERE o.created_at BETWEEN ? AND ?\n" +
            "GROUP BY DATE(o.created_at)\n" +
            "ORDER BY DATE(o.created_at)", nativeQuery = true)
    List<Object[]> getDailyStatistics(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


}