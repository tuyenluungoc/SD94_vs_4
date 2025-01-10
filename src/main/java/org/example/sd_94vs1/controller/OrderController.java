package org.example.sd_94vs1.controller;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.example.sd_94vs1.entity.oder.Order;
import org.example.sd_94vs1.entity.oder.OrderLine;
import org.example.sd_94vs1.service.oder.OrderLineService;
import org.example.sd_94vs1.service.oder.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/admin/orders")
public class OrderController {
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private OrderLineService orderLineService;
    @Autowired
    private OrderService orderService;
//    @Autowired
//    private PdfExportService pdfExportService;

    @GetMapping("/details/{orderCode}")
    public String getOrderDetails(@PathVariable String orderCode, Model model) {
        try {
            Order orders = orderService.findOrderByOrderCodeAndStatusTrue(orderCode);
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedTotalAmount = formatter.format(orders.getTotalAmountMoney());


            // Thêm danh sách hóa đơn và chi tiết vào model
            model.addAttribute("formattedTotalAmount", formattedTotalAmount);
            model.addAttribute("orders", orders);
            List<OrderLine> orderLines = orderLineService.getOrderLinesByOrderCode(orderCode);
            model.addAttribute("orderLines", orderLines);
            model.addAttribute("orderCode", orderCode);
            return "admin/order/orderLine";
        } catch (Exception ex) {
            model.addAttribute("error", "Có lỗi xảy ra: " + ex.getMessage());
            return "error"; // Trả về view lỗi nếu có ngoại lệ
        }
    }

    @GetMapping("/export-pdf/{orderCode}")
    public void exportOrderToPdf(@PathVariable("orderCode") String orderCode, HttpServletResponse response) {
        try {
            // Set response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=order_" + orderCode + ".pdf");
            DecimalFormat formatter = new DecimalFormat("#,###");

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("orderCode", orderCode);

// Thay thế dữ liệu giả bằng dữ liệu thực từ cơ sở dữ liệu
            Order order = orderService.findOrderByOrderCodeAndStatusTrue(orderCode); // Lấy từ service
            String formattedTotalAmount = formatter.format(order.getTotalAmountMoney());
            context.setVariable("orders", List.of(order));
            context.setVariable("orderLines", orderLineService.getOrderLinesByOrderCode(orderCode));
            context.setVariable("formattedTotalAmount",formattedTotalAmount);
            // Render Thymeleaf template to HTML
            String htmlContent = templateEngine.process("admin/order/orderLine", context);

            // Convert HTML to PDF using Flying Saucer
            try (OutputStream os = response.getOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(htmlContent, "");
                builder.toStream(os);
//                builder.withFont("/path/to/your/Roboto-Regular.ttf", "Roboto");
                builder.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//@GetMapping("/export-pdf/{orderCode}")
//public void exportOrderToPdf(@PathVariable("orderCode") String orderCode, HttpServletResponse response) {
//    try {
//        // Set response headers
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=order_" + orderCode + ".pdf");
//        DecimalFormat formatter = new DecimalFormat("#,###");
//
//        // Prepare Thymeleaf context
//        Context context = new Context();
//        context.setVariable("orderCode", orderCode);
//
//        // Fetch order data
//        Order order = orderService.findOrderByOrderCodeAndStatusTrue(orderCode); // Lấy từ service
//        String formattedTotalAmount = formatter.format(order.getTotalAmountMoney());
//        context.setVariable("orders", List.of(order));  // Add order data
//        List<OrderLine> orderLines = orderLineService.getOrderLinesByOrderCode(orderCode); // Fetch order lines
//        context.setVariable("orderLines", orderLines);
//        context.setVariable("formattedTotalAmount", formattedTotalAmount);
//
//        // Convert logo image to Base64
//        String logoBase64 = convertImageToBase64("path/to/logodji.png"); // Update with actual logo path
//        context.setVariable("logoBase64", logoBase64);
//
//        // Convert product images to Base64
//        List<String> productImagesBase64 = orderLines.stream()
//                .map(line -> {
//                    try {
//                        return convertImageToBase64(line.getProduct().getImage()); // Ensure correct image path
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                })
//                .filter(image -> image != null) // Remove null entries
//                .collect(Collectors.toList());
//
//        // Check if the images list is empty or has the wrong size
//        if (productImagesBase64.isEmpty() || productImagesBase64.size() != orderLines.size()) {
//            System.out.println("Warning: Mismatch in number of product images and order lines.");
//        }
//
//        context.setVariable("productImagesBase64", productImagesBase64);
//
//        // Render Thymeleaf template to HTML
//        String htmlContent = templateEngine.process("admin/order/orderLine", context);
//
//        // Convert HTML to PDF using Flying Saucer
//        try (OutputStream os = response.getOutputStream()) {
//            PdfRendererBuilder builder = new PdfRendererBuilder();
//            builder.useFastMode();
//            builder.withHtmlContent(htmlContent, "");
//            builder.toStream(os);
//            builder.run();
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}


    public String convertImageToBase64(String imagePath) throws IOException {
        File file = new File(imagePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
