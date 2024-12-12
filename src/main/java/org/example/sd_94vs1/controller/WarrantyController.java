package org.example.sd_94vs1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.sd_94vs1.DTO.StatusDTO;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.example.sd_94vs1.repository.WarrantyClaimRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/warranty")
public class WarrantyController {

@Autowired
private WarrantyClaimRepo warrantyClaimRepo;
    @GetMapping("/doi-trang-thai/{claimCode}")
    public String doiTrangThai(@PathVariable String claimCode, HttpSession session, Model model) {
        // Tìm và cập nhật trạng thái yêu cầu bảo hành
        WarrantyClaim warrantyClaim = warrantyClaimRepo.findWarrantyClaimByClaimCode(claimCode);
        String notificationMessage = "";

        if (warrantyClaim != null) {
            warrantyClaim.setClaimStatus("Đã Duyệt");
            warrantyClaimRepo.save(warrantyClaim);

            // Tạo thông báo cho yêu cầu bảo hành
            notificationMessage = "Yêu cầu bảo hành " + claimCode + " đã được duyệt.";
            User user = warrantyClaim.getUser(); // Lấy thông tin user từ yêu cầu bảo hành

            // Thêm thông báo vào danh sách trong session
            List<StatusDTO> sessionNotifications = (List<StatusDTO>) session.getAttribute("notifications");
            if (sessionNotifications == null) {
                sessionNotifications = new ArrayList<>();
            }

            sessionNotifications.add(new StatusDTO(
                    claimCode,
                    notificationMessage,
                    null // Không có mã giỏ hàng
            ));
            session.setAttribute("notifications", sessionNotifications);
        } else {
            notificationMessage = "Không tìm thấy yêu cầu bảo hành với mã: " + claimCode;
        }

        // Cập nhật danh sách yêu cầu bảo hành và thông báo
        List<WarrantyClaim> warrantyClaims = warrantyClaimRepo.findAll();
        model.addAttribute("warrantyClaims", warrantyClaims);
        model.addAttribute("notifications", session.getAttribute("notifications"));

        return "admin/warranty/yc-warranty"; // Trả về lại trang yêu cầu bảo hành
    }


//    @GetMapping("/doi-trang-thai/{claimCode}")
//    public String doiTrangThai(@PathVariable String claimCode, Model model, HttpSession session) {
//        WarrantyClaim warrantyClaim = warrantyClaimRepo.findWarrantyClaimByClaimCode(claimCode);
//        if (warrantyClaim != null) {
//            // Cập nhật trạng thái
//            warrantyClaim.setClaimStatus("Đã Duyệt");
//            warrantyClaimRepo.save(warrantyClaim);
//
//            // Thêm thông báo vào session
//            List<String> notifications = (List<String>) session.getAttribute("notifications");
//            if (notifications == null) {
//                notifications = new ArrayList<>();
//            }
//            notifications.add("Yêu cầu bảo hành " + claimCode + " đã được duyệt thành công.");
//            session.setAttribute("notifications", notifications);
//        }
//
//        // Lấy danh sách các yêu cầu bảo hành
//        List<WarrantyClaim> warrantyClaims = warrantyClaimRepo.findAll();
//        model.addAttribute("warrantyClaims", warrantyClaims);
//
//        return "admin/warranty/yc-warranty";


}
