package org.example.sd_94vs1.rest;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.sd_94vs1.entity.Review;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.model.request.UpsertReviewRequest;
import org.example.sd_94vs1.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewResource {

    private final ReviewService reviewService;

    // Tạo review - POST
    @PostMapping("/{blogCode}")
    public ResponseEntity<?> createReview(@RequestBody UpsertReviewRequest request,
                                          @PathVariable String blogCode,
                                          HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập để viết đánh giá.");
        }

        try {
            // Cập nhật userCode trong request nếu chưa có
            request.setUserCode(currentUser.getUserCode());

            // Gọi service để tạo review
            Review review = reviewService.createReview(request, blogCode, currentUser.getUserCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi tạo đánh giá: " + e.getMessage());
        }
    }
    // Cập nhật review - PUT
    @PutMapping("/{reviewCode}/{blogCode}")
    public ResponseEntity<?> updateReview(@RequestBody UpsertReviewRequest request,
                                          @PathVariable String reviewCode,
                                          @PathVariable String blogCode) {
        Review review = reviewService.updateReview(reviewCode, request, blogCode);
        return ResponseEntity.ok(review); // status code 200
    }

    // Xóa review - DELETE
    @DeleteMapping("/{reviewCode}")
    public ResponseEntity<?> deleteReview(@PathVariable String reviewCode) {
        reviewService.deleteReview(reviewCode);
        return ResponseEntity.noContent().build(); // status code 204
    }
}

