package org.example.sd_94vs1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.sd_94vs1.DTO.StatusDTO;
import org.example.sd_94vs1.entity.Status;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.entity.warranty.WarrantyClaim;
import org.example.sd_94vs1.repository.StatusRepository;
import org.example.sd_94vs1.repository.WarrantyClaimRepo;
import org.example.sd_94vs1.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notifications")
public class ThongBaoController {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private WarrantyClaimRepo warrantyClaimRepo;

    @GetMapping()
    public String getNotifications(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/dang-nhap"; // Chuyển hướng nếu chưa đăng nhập
        }

        // Lấy danh sách trạng thái đơn hàng của người dùng
        List<Status> statuses = statusRepository.findByUser_UserCode(currentUser.getUserCode());
        List<StatusDTO> statusDTOs = statuses.stream()
                .map(status -> new StatusDTO(
                        status.getStatusCode(),
                        status.getNameStatus(),
                        status.getShoppingCart().getShoppingCartCode()))
                .collect(Collectors.toList());

        // Lấy danh sách thông báo từ session
        List<StatusDTO> sessionNotifications = (List<StatusDTO>) session.getAttribute("notifications");
        if (sessionNotifications == null) {
            sessionNotifications = new ArrayList<>();
        }

        // Gộp tất cả thông báo
        List<StatusDTO> allNotifications = new ArrayList<>();
        allNotifications.addAll(statusDTOs);
        allNotifications.addAll(sessionNotifications);

        model.addAttribute("notifications", allNotifications);

        return "web/thong-bao";
    }





    @PostMapping("/update-status/{statusCode}")
    public String updateStatus(@PathVariable("statusCode") String statusCode,
                               @RequestParam("newStatus") String newStatus,
                               Model model) {
        try {
            // Cập nhật trạng thái
            statusService.updateStatus(statusCode, newStatus);
            System.out.println("thành công");
            model.addAttribute("message", "Trạng thái đơn hàng đã được cập nhật thành công!");
        } catch (Exception e) {
            model.addAttribute("message", "Có lỗi khi cập nhật trạng thái.");
        }

        // Trả về trang danh sách trạng thái
        return "admin/trang-thai-don-hang";
    }


}
