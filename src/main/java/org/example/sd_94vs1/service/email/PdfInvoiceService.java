package org.example.sd_94vs1.service.email;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.sd_94vs1.entity.ShoppingCartProducts;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PdfInvoiceService {

    public byte[] generateInvoicePdf(List<ShoppingCartProducts> shoppingCartProducts) throws Exception {
        // Tạo một tài liệu PDF
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Khởi tạo PdfWriter để ghi vào byteArrayOutputStream
        PdfWriter.getInstance(document, byteArrayOutputStream);

        // Mở tài liệu
        document.open();

        // Tiêu đề hóa đơn
        Paragraph title = new Paragraph("HÓA ĐƠN MUA HÀNG", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Khoảng cách
        document.add(new Paragraph(" "));

        // Tạo bảng chi tiết sản phẩm trong giỏ hàng
        PdfPTable table = new PdfPTable(4); // 4 cột: Sản phẩm, Giá, Số lượng, Tổng cộng
        table.setWidthPercentage(100);

        // Tiêu đề bảng
        table.addCell("Sản Phẩm");
        table.addCell("Giá");
        table.addCell("Số Lượng");
        table.addCell("Tổng Cộng");

        // Duyệt qua các sản phẩm và thêm vào bảng
        for (ShoppingCartProducts product : shoppingCartProducts) {
            String productName = product.getProduct().getName();
            BigDecimal productPrice = product.getPrice();
            int quantity = product.getAmount();
            BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));

            // Đảm bảo rằng giá trị không bị null
            table.addCell(productName != null ? productName : "Không có tên sản phẩm");
            table.addCell(productPrice != null ? productPrice.toString() + " VND" : "Không có giá");
            table.addCell(String.valueOf(quantity));
            table.addCell(totalPrice != null ? totalPrice.toString() + " VND" : "Không có tổng cộng");
        }

        // Thêm bảng vào tài liệu
        document.add(table);

        // Thêm tổng cộng
        BigDecimal totalAmount = shoppingCartProducts.stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Paragraph totalParagraph = new Paragraph("Tổng cộng: " + totalAmount + " VND", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        // Đóng tài liệu
        document.close();

        // Trả về byte array của PDF
        return byteArrayOutputStream.toByteArray();
    }

}
