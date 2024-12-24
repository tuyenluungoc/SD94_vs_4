package org.example.sd_94vs1.service;

import org.example.sd_94vs1.entity.product.DetailedProduct;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetailedProductService {
    @Autowired
    private DetailedProductRepository detailedProductRepository;

    // Lấy giá của sản phẩm theo mã sản phẩm
//    public BigDecimal getProductPriceByCode(String productCode) {
//        return detailedProductRepository.findPriceVNDByProductCode(productCode);
//    }
    public BigDecimal getProductPriceByCode(String productCode) {
        DetailedProduct detailedProduct = detailedProductRepository.findByProduct_ProductCode(productCode);
        if (detailedProduct != null && detailedProduct.getPriceVND() != null) {
            return detailedProduct.getPriceVND();  // Trả về giá từ database
        }
        return BigDecimal.ZERO;
    }

    public DetailedProductService(DetailedProductRepository detailProductRepository) {
        this.detailedProductRepository = detailProductRepository;
    }

    public void save(DetailedProduct detailProduct) {
        detailedProductRepository.save(detailProduct);
    }

    // Lấy tất cả sản phẩm chi tiết
    public List<DetailedProduct> getAll() {
        return detailedProductRepository.findAll();
    }


    // Lưu hoặc cập nhật sản phẩm chi tiết
    public DetailedProduct saveDetailProduct(DetailedProduct detailProduct) {
        return detailedProductRepository.save(detailProduct);
    }


}




