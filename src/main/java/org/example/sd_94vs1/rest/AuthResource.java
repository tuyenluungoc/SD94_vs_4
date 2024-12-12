package org.example.sd_94vs1.rest;

import lombok.RequiredArgsConstructor;

import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.exception.BadRequestException;
import org.example.sd_94vs1.model.request.LoginRequest;
import org.example.sd_94vs1.model.request.RegisterRequest;
import org.example.sd_94vs1.repository.UserRepository;
import org.example.sd_94vs1.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthResource {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Kiểm tra xem email và password có hợp lệ không
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email và mật khẩu không được để trống"));
        }

        User user = authService.login(request); // Call login method

        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Tài khoản không tồn tại"
            ));
        }

        if (!user.getIsVerified()) { // Kiểm tra tài khoản đã được xác thực chưa
            String redirectUrl = "/xac-thuc-otp?email=" + user.getEmail();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "message", "Tài khoản chưa được xác thực",
                    "redirectUrl", redirectUrl
            ));
        }


        return ResponseEntity.ok(Map.of(
                "message", "Đăng nhập thành công",
                "user", user
        ));
    }






    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dang_ky")
    public ResponseEntity<?> dangKy(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("Đăng ký thành công");
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/xac-thuc-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String otp = request.get("otp");


        if (email == null || otp == null) {
            throw new BadRequestException("Email và mã OTP là bắt buộc");
        }

        try {
            authService.verifyOtp(email, otp);
            return ResponseEntity.ok("Tài khoản đã được xác thực thành công!");
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/gui-lai-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // Kiểm tra email có tồn tại trong request
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email là bắt buộc để gửi lại mã OTP");
        }

        try {
            // Tìm kiếm người dùng theo email
            User user = authService.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Email không tồn tại trong hệ thống"));

            // Kiểm tra tài khoản đã xác thực chưa
            if (Boolean.TRUE.equals(user.getIsVerified())) {
                return ResponseEntity.badRequest().body("Tài khoản đã được xác thực. Không cần gửi lại mã OTP.");
            }

            // Gửi lại OTP
            String otp = authService.resendOtp(email);

            return ResponseEntity.ok("Mã OTP đã được gửi lại thành công!");
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi gửi lại mã OTP. Vui lòng thử lại sau.");
        }
    }

    @PostMapping("/delete-expired-user")
    public ResponseEntity<?> deleteExpiredUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        authService.deleteExpiredUser(email);
        return ResponseEntity.ok(Map.of("message", "Tài khoản tạm thời đã bị xóa"));
    }



}
