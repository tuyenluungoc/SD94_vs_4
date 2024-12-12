package org.example.sd_94vs1.rest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String userCode;
    private String shoppingCartCode;
    private String shippingAddress;

    private Double totalAmountMoney;
    private Boolean status;         // Trạng thái đơn hàng (ví dụ: đang chờ, đã hoàn thành, v.v.)

}
