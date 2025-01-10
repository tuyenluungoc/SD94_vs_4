package org.example.sd_94vs1.controller;


import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.entity.product.Product;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.example.sd_94vs1.repository.Product.ProductRepository;
import org.example.sd_94vs1.service.DetailedProductService;
import org.example.sd_94vs1.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DetailProductController {
    @Autowired
    DetailedProductRepository detailedProductRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private DetailedProductService detailedProductService;

    @GetMapping("/detailproduct")
    public String getDetailProductPage(Model model) {
        // Lấy danh sách chi tiết sản phẩm từ cơ sở dữ liệu
        List<DetailedProduct> detailProducts = detailedProductRepository.findAll();
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("detailProducts", detailProducts);
        model.addAttribute("productList", productList);
        model.addAttribute("detailProduct", new DetailedProduct()); // Thêm đối tượng DetailProduct cho form thêm mới
        return "admin/user/detailproduct"; // Trang HTML hiển thị danh sách chi tiết sản phẩm
    }

    @PostMapping("/add-detailproduct")
    public String saveDetailProduct(
            @ModelAttribute DetailedProduct detailedProduct,
            @RequestParam("productCode") String productCode,
            @RequestParam("description") String description,
            @RequestParam("quantity") int quantity,
            @RequestParam("priceVND") String priceVND,  // Receive as String
            RedirectAttributes redirectAttributes) {

        // Convert priceVND String to BigDecimal
        BigDecimal price = new BigDecimal(priceVND);

        // Lấy sản phẩm theo productCode
        Optional<Product> productOptional = productService.getProductByCode(productCode);

        String detailedProductCode = "dtfpv" + String.format("%05d", (int)(Math.random() * 100000));
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Gán thông tin vào DetailProduct
            detailedProduct.setDetailedProductCode(detailedProductCode);
            detailedProduct.setDescription(description);
            detailedProduct.setQuantity(quantity);
            detailedProduct.setPriceVND(price);  // Set the BigDecimal value
            detailedProduct.setProduct(product); // Gắn sản phẩm vào chi tiết sản phẩm

            // Lưu DetailProduct vào cơ sở dữ liệu
            detailedProductService.save(detailedProduct);

            // Thêm thông báo thành công
            redirectAttributes.addFlashAttribute("success", "Thêm chi tiết sản phẩm thành công!");
        } else {
            // Thêm thông báo lỗi nếu không tìm thấy sản phẩm
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
        }

        // Quay lại danh sách chi tiết sản phẩm
        return "redirect:/admin/detailproduct";
    }

    @PostMapping("/delete-detailproduct/{detailedProductCode}")
    public String deleteDetailProduct(@PathVariable String detailedProductCode, RedirectAttributes redirectAttributes) {

        try {
            DetailedProduct detailedProduct = detailedProductRepository.findByDetailedProductCode(detailedProductCode);
            detailedProductRepository.delete(detailedProduct);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa chi tiết SP thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa chi tiết SP!");
        }
        return "redirect:/admin/detailproduct";
    }


    @PostMapping("/update-detailproduct")
    public String updateProductDetail(
            @RequestParam String productDetailCode,
            @RequestParam String productCode,
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam String desc,
            RedirectAttributes redirectAttributes) {

        DetailedProduct detailedProduct = detailedProductRepository.findByDetailedProductCode(productDetailCode);

        Product product = productRepository.findByProductCode(productCode).get();

        detailedProduct.setProduct(product);
        detailedProduct.setPriceVND(new BigDecimal(price));
        detailedProduct.setQuantity(quantity);
        detailedProduct.setDescription(desc);
        detailedProduct.setEditDate(new Date());

        detailedProductRepository.save(detailedProduct);


        // Gửi thông báo thành công
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật chi tiết sản phẩm thành công!");

        return "redirect:/admin/detailproduct";
    }


}
