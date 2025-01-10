package org.example.sd_94vs1.service;

import com.github.slugify.Slugify;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.example.sd_94vs1.entity.Blog;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.exception.ResourceNotFoundException;
import org.example.sd_94vs1.model.request.UpsertBlogRequest;
import org.example.sd_94vs1.repository.BlogRepository;
import org.example.sd_94vs1.utils.FileUtils;
import org.example.sd_94vs1.utils.StringUtils;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final HttpSession session;
    private final FileService fileService;
    private final Slugify slugify;
    private final Random random = new Random();
    // Lấy tất cả bài viết sắp xếp theo createdAt giảm dần
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll(Sort.by("createdAt").descending());
    }

    // Lấy tất cả bài viết của user đang đăng nhập
    public List<Blog> getAllBlogOfCurrentUser() {
        User user = (User) session.getAttribute("currentUser");
        return blogRepository.findByUser_UserCode(user.getUserCode(), Sort.by("createdAt").descending());
    }

    // Lấy bài viết theo id
    public Blog getBlogById(String blogCode) { // Chuyển từ Integer sang String
        return blogRepository.findById(blogCode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết có mã: " + blogCode));
    }
    private String generateBlogCode() {
        // Tạo số ngẫu nhiên từ 0 đến 9999
        int number = random.nextInt(10000);
        // Định dạng số với 4 chữ số
        return String.format("bl%04d", number);
    }
    // Tạo bài viết
    public Blog createBlog(UpsertBlogRequest request) {
        User user = (User) session.getAttribute("currentUser");

        // Create blog
        Blog blog = Blog.builder()
                .blogCode(generateBlogCode())
                .title(request.getTitle())
                .slug(slugify.slugify(request.getTitle()))
                .content(request.getContent())
                .description(request.getDescription())
                .status(request.getStatus())
                .user(user)
                .thumbnail(StringUtils.generateLinkImage(request.getTitle()))
                .build();

        return blogRepository.save(blog);
    }

    // Cập nhật bài viết
    public Blog updateBlog(String blogCode, UpsertBlogRequest request) { // Chuyển từ Integer sang String
        // Tìm bài viết theo mã
        Blog blog = blogRepository.findById(blogCode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết với mã = " + blogCode));

        // Cập nhật thông tin bài viết
        blog.setTitle(request.getTitle());
        blog.setSlug(slugify.slugify(request.getTitle()));
        blog.setDescription(request.getDescription());
        blog.setContent(request.getContent());
        blog.setStatus(request.getStatus());

        return blogRepository.save(blog);
    }

    // Xóa bài viết
//    public void deleteBlog(String blogCode) { // Chuyển từ Integer sang String
//        Blog blog = blogRepository.findById(blogCode)
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết với mã = " + blogCode));
//
//        // Kiểm tra xem blog có thumbnail không. Nếu có thì xóa file thumbnail
//        if (blog.getThumbnail() != null) {
//            FileUtils.deleteFile(blog.getThumbnail());
//        }
//
//        blogRepository.delete(blog);
//    }
    public void deleteBlog(String blogCode) {
        Blog blog = blogRepository.findById(blogCode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết với mã = " + blogCode));

        // Kiểm tra xem blog có thumbnail không. Nếu có thì xóa file thumbnail
        if (blog.getThumbnail() != null) {
            // Kiểm tra xem thumbnail có phải là URL không
            String thumbnail = blog.getThumbnail();
            if (!(thumbnail.startsWith("http://") || thumbnail.startsWith("https://"))) {
                FileUtils.deleteFile(thumbnail);
            } else {
                System.out.println("Skipping deletion of URL thumbnail: " + thumbnail);
            }
        }

        blogRepository.delete(blog);
    }


    // Upload thumbnail
    public String uploadThumbnail(String blogCode, MultipartFile file) { // Chuyển từ Integer sang String
        // Kiểm tra xem bài viết có tồn tại không
        Blog blog = blogRepository.findById(blogCode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết có mã: " + blogCode));

        // Upload file vào trong thư mục trên server (image_uploads)
        String filePath = fileService.uploadFile(file);

        // Cập nhật đường dẫn thumbnail cho bài viết
        blog.setThumbnail(filePath);
        blogRepository.save(blog);

        return filePath;
    }
//    lấy ra 8 bài vieetsmoiws nhất
public List<Blog> getLatestBlogs(int limit) {
    return blogRepository.findTop8ByOrderByCreatedAtDesc(PageRequest.of(0, limit));
}
    public Optional<Blog> findByBlogCode(String blogCode) {
        return blogRepository.findByBlogCode(blogCode);
    }
}
