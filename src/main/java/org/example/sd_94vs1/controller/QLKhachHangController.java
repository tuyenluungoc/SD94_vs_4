package org.example.sd_94vs1.controller;

import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.model.request.UserRequest;
import org.example.sd_94vs1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/custumer")
public class QLKhachHangController {
    @Autowired
    private UserService userService;
    @GetMapping("/admin/custumer")
    public String showCustomers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/custumer";
    }

    @PostMapping("/them-khach-hang")
    public String themKhachHang(UserRequest userDTO, Model model) {
        try {
            User newUser = userService.createUser(userDTO);
            model.addAttribute("user", newUser);
            return "redirect:/admin/custumer";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Lỗi thêm khách hàng: " + e.getMessage());
            return "admin/user/error";
        }
    }
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userCode") String userCode) {
        System.out.println("UserCode nhận được: " + userCode);
        userService.deleteUserByCode(userCode);
        return "redirect:/admin/custumer";
    }


    @GetMapping("/detail/{userCode}")
    @ResponseBody
    public ResponseEntity<User> getUserDetail(@PathVariable String userCode) {
        Optional<User> userOptional = userService.findByUserCode(userCode);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/update/{userCode}")
    public String updateUser(@PathVariable String userCode, @ModelAttribute User user, Model model) {
        User updatedUser = userService.updateUser(userCode, user);

        if (updatedUser != null) {
            model.addAttribute("message", "Cập nhật thành công!");
        } else {
            model.addAttribute("message", "Không tìm thấy người dùng.");
        }
        // Load lại danh sách người dùng sau khi cập nhật
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "admin/user/custumer"; // Trả về view thay vì redirect để cập nhật ngay lập tức
    }



}
