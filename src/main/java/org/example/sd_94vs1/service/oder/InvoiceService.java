package org.example.sd_94vs1.service.oder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import org.example.sd_94vs1.entity.ShoppingCart;
import org.example.sd_94vs1.entity.ShoppingCartProducts;
import org.example.sd_94vs1.entity.oder.Order;


import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceService {

    public String createInvoicePDF(Order order, ShoppingCart shoppingCart, List<ShoppingCartProducts> products) throws Exception {
        // Tạo đối tượng Document
        Document document = new Document();
        String invoiceFileName = "invoice_" + order.getOrderCode() + ".pdf";
        String filePath = "/uploads/invoices/" + invoiceFileName;  // Đường dẫn đến file PDF

        // Tạo PdfWriter để xuất ra file
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Thêm tiêu đề vào PDF
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 12);

        document.add(new Paragraph("Hóa Đơn Thanh Toán", titleFont));
        document.add(new Paragraph("Mã đơn hàng: " + order.getOrderCode(), contentFont));
        document.add(new Paragraph("Mã giỏ hàng: " + shoppingCart.getShoppingCartCode(), contentFont));
        document.add(new Paragraph("Tên khách hàng: " + order.getUser().getName(), contentFont));
        document.add(new Paragraph("Địa chỉ giao hàng: " + order.getShippingAddress(), contentFont));
        document.add(new Paragraph("Ngày tạo đơn: " + order.getCreatedAt(), contentFont));

        // Thêm chi tiết sản phẩm vào hóa đơn
        document.add(new Paragraph("Chi tiết sản phẩm:", contentFont));
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ShoppingCartProducts cartProduct : products) {
            String productName = cartProduct.getProduct().getName();
            int quantity = cartProduct.getAmount();
            BigDecimal price = cartProduct.getPrice();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(itemTotal);

            document.add(new Paragraph(String.format("- %s: %d x %.2f VND = %.2f VND",
                    productName, quantity, price, itemTotal), contentFont));
        }

        // Thêm tổng tiền vào hóa đơn
        document.add(new Paragraph("Tổng số tiền: " + totalAmount + " VND", contentFont));
        document.add(new Paragraph("Cảm ơn bạn đã mua sắm tại cửa hàng chúng tôi!", contentFont));

        document.close();

        return invoiceFileName;  // Trả về tên file PDF đã tạo
    }
}