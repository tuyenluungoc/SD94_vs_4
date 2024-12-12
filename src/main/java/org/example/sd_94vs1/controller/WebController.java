package org.example.sd_94vs1.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.sd_94vs1.entity.*;
import org.example.sd_94vs1.entity.oder.Order;
import org.example.sd_94vs1.entity.product.*;
import org.example.sd_94vs1.exception.ResourceNotFoundException;
import org.example.sd_94vs1.model.enums.UserRole;
import org.example.sd_94vs1.model.request.UpsertReviewRequest;
import org.example.sd_94vs1.repository.Product.DetailedProductRepository;
import org.example.sd_94vs1.repository.Product.ProductRepository;
import org.example.sd_94vs1.repository.ShoppingCartProductsRepository;
import org.example.sd_94vs1.service.MessageService;

import org.example.sd_94vs1.repository.UserRepository;
import org.example.sd_94vs1.repository.oder.OrderRepository;
import org.example.sd_94vs1.service.*;
import org.example.sd_94vs1.service.oder.OrderService;
import org.example.sd_94vs1.service.product.CategoryService;
import org.example.sd_94vs1.service.product.ManufacturerService;
import org.example.sd_94vs1.service.product.ProductService;
import org.example.sd_94vs1.service.product.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

 // Ensure this is correct


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class WebController {

    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;
    private final HttpSession session;
    private final BlogService blogService;
    private final ProductRepository productRepository;
    private final DetailedProductRepository detailedProductRepository;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartProductsService shoppingCartProductsService;
    private final DetailedProductService detailedProductService;
    private final ShoppingCartProductsRepository shoppingCartProductsRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final OrderRepository orderRepository;
    private final MessageService messageService;

    @GetMapping("/")
    public String getHomePage(Model model) {
//        Phân danh mục cớ bản không theo enum
        List<Product> productsmini = productService.findProductsByCodeAndType("mn","bn001");
        List<Product> productsmavic = productService.findProductsByCodeAndType("mv","bn001");
        List<Product> productsfpv = productService.findProductsByCodeAndType("fpv","bn005");
        List<Product>productsins= productService.findProductsByCodeAndType("ins","bn001");
        List<Product>productsmar= productService.findProductsByCodeAndType("mar","bn001");
        List<Product>productsmas= productService.findProductsByCodeAndType("mas","bn001");
        List<Product>productmic= productService.findProductsByCodeAndType("mic","bn005");
        List<Product>productsom= productService.findProductsByCodeAndType("om","bn005");
        List<Product>productsomm= productService.findProductsByCodeAndType("omm","bn005");
        List<Product>productop= productService.findProductsByCodeAndType("op","bn005");
        List<Product>productpt= productService.findProductsByCodeAndType("pt","bn001");
        List<Product>productrn= productService.findProductsByCodeAndType("rn","bn005");

        List<ProductType> productTypes = productTypeService.findAll();
        List<Category> categories = categoryService.findAll();
        List<Manufacturer>manufacturers=manufacturerService.getAll();
        // Lấy 4 bài viết mới nhất
        List<Blog> latestBlogs = blogService.getLatestBlogs(4);


        model.addAttribute("productsmini", productsmini);
        System.out.println(productsmini);
        model.addAttribute("productsmavic", productsmavic);
        model.addAttribute("productsfpv", productsfpv);
        model.addAttribute("productsins", productsins);
        model.addAttribute("productsmar", productsmar);
        model.addAttribute("productsmas", productsmas);
        model.addAttribute("productmic", productmic);
        model.addAttribute("productsom", productsom);
        model.addAttribute("productsomm", productsomm);
        model.addAttribute("productop", productop);
        model.addAttribute("productpt", productpt);
        model.addAttribute("productrn", productrn);
        model.addAttribute("productTypes", productTypes);
        model.addAttribute("categories", categories);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("latestBlogs", latestBlogs);
        //xoa list chat
        session.removeAttribute("messages");
        messages.clear();
        return "web/index";
    }
    @GetMapping("/san-pham")
    public String getHomePageSanPham(Model model,@RequestParam(required = false) String search) {
//        Phân danh mục cớ bản không theo enum
        List<Product> productsmini = productService.findProductsByCodeAndType("mn", "bn001");
        List<Product> productsmavic = productService.findProductsByCodeAndType("mv", "bn001");
        List<Product> productsfpv = productService.findProductsByCodeAndType("fpv", "bn001");
        List<Product> productsins = productService.findProductsByCodeAndType("ins", "bn001");
        List<Product> productsmar = productService.findProductsByCodeAndType("mar", "bn001");
        List<Product> productsmas = productService.findProductsByCodeAndType("mas", "bn001");
        List<Product> productmic = productService.findProductsByCodeAndType("mic", "bn005");
        List<Product> productsom = productService.findProductsByCodeAndType("om", "bn005");
        List<Product> productsomm = productService.findProductsByCodeAndType("omm", "bn005");
        List<Product> productop = productService.findProductsByCodeAndType("op", "bn005");
        List<Product> productpt = productService.findProductsByCodeAndType("pt", "bn001");
        List<Product> productrn = productService.findProductsByCodeAndType("rn", "bn005");

        List<ProductType> productTypes = productTypeService.findAll();
        List<Category> categories = categoryService.findAll();
        List<Manufacturer> manufacturers = manufacturerService.getAll();
        // Lấy 4 bài viết mới nhất
        List<Blog> latestBlogs = blogService.getLatestBlogs(4);

        model.addAttribute("productsmini", productsmini);
        System.out.println(productsmini);
        if (search != null && !search.trim().isEmpty()) {
            List<Product> searchResults = productService.searchProductsByName(search.trim());
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("searchKeyword", search.trim());

        } else {

            model.addAttribute("productsmavic", productsmavic);
            model.addAttribute("productsfpv", productsfpv);
            model.addAttribute("productsins", productsins);
            model.addAttribute("productsmar", productsmar);
            model.addAttribute("productsmas", productsmas);
            model.addAttribute("productmic", productmic);
            model.addAttribute("productsom", productsom);
            model.addAttribute("productsomm", productsomm);
            model.addAttribute("productop", productop);
            model.addAttribute("productpt", productpt);
            model.addAttribute("productrn", productrn);
            model.addAttribute("productTypes", productTypes);
            model.addAttribute("categories", categories);
            model.addAttribute("manufacturers", manufacturers);
            model.addAttribute("latestBlogs", latestBlogs);
            return "web/san-pham/san-pham";
        }

        return "web/san-pham/san-pham";
    }
    //    detail
    @GetMapping("/products/{productCode}")
    public String getDetailedProduct(@PathVariable("productCode") String productCode, Model model) {
        // Tìm sản phẩm theo productCode
        Optional<Product> productOpt = productRepository.findByProductCode(productCode);

        if (productOpt.isPresent()) {
            // Tìm chi tiết sản phẩm theo mã sản phẩm
            Optional<DetailedProduct> detailedProductOpt = detailedProductRepository.findByProduct(productOpt.get());

            if (detailedProductOpt.isPresent()) {
                model.addAttribute("detailedProduct", detailedProductOpt.get());
                model.addAttribute("product", productOpt.get());
                return "web/detailproduct"; // Trả về view chi tiết sản phẩm
            } else {
                model.addAttribute("message", "Không tìm thấy chi tiết sản phẩm.");
            }
        } else {
            model.addAttribute("message", "Sản phẩm không tồn tại.");
        }
        return "error"; // Trả về trang lỗi nếu không tìm thấy sản phẩm hoặc chi tiết sản phẩm
    }
    @GetMapping("/dang-ky")
    public String getDangKyPage() {
        User user = (User) session.getAttribute("currentUser"); // Lấy thông tin người dùng trong session
        if (user != null) { // Nếu đăng nhập thì chuyển hướng về trang chủ
            return "redirect:/";
        }
        return "web/dang-ky"; // Nếu chưa đăng nhập thì hiển thị trang đăng ký
    }
    @GetMapping("/clear-cart")
    public String clearCart(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Lấy người dùng từ session
        User user = (User) request.getSession().getAttribute("currentUser");

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thực hiện thao tác này");
            return "redirect:/dang-nhap";
        }

        // Tìm giỏ hàng hiện tại của người dùng
        Optional<ShoppingCart> shoppingCart = shoppingCartService.findCurrentCart(user);

        if (shoppingCart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy giỏ hàng để xóa");
            return "redirect:/own-shoppingcart";
        }

        // Xóa tất cả sản phẩm trong giỏ hàng
        shoppingCartProductsService.clearCart(shoppingCart.orElse(null));

        // Cập nhật thông báo thành công
        redirectAttributes.addFlashAttribute("message", "Đã xóa toàn bộ sản phẩm trong giỏ hàng");
        return "redirect:/own-shoppingcart";
    }

    @PostMapping("/delete-cart-item")
    public String deleteCartItem(@RequestParam("productCode") String productCode,  // Sử dụng productCode
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        // Lấy người dùng từ session
        User user = (User) request.getSession().getAttribute("currentUser");

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thực hiện thao tác này");
            return "redirect:/dang-nhap";
        }

        // Tìm giỏ hàng hiện tại của người dùng
        Optional<ShoppingCart> shoppingCart = shoppingCartService.findCurrentCart(user);

        if (!shoppingCart.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy giỏ hàng để xóa sản phẩm");
            return "redirect:/own-shoppingcart";
        }

        // Xóa sản phẩm cụ thể
        boolean isDeleted = shoppingCartProductsService.deleteProductFromCart(shoppingCart.get().getShoppingCartCode(), productCode);

        // Kiểm tra kết quả
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa khỏi giỏ hàng");
        } else {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại trong giỏ hàng");
        }

        return "redirect:/own-shoppingcart";
    }

    @GetMapping("/dang-nhap")
    public String getDangNhapPage() {
        User user = (User) session.getAttribute("currentUser"); // Lấy thông tin người dùng trong session
        if (user != null) { // Nếu đăng nhập thì chuyển hướng về trang chủ
            return "redirect:/";
        }
        return "web/dang-nhap"; // Nếu chưa đăng nhập thì hiển thị trang đăng nhập
    }


    @PostMapping("/dang-nhap")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // Xử lý đăng nhập (giả định là bạn đã kiểm tra thông tin đăng nhập ở đây)
        HttpSession session = request.getSession();

        // Kiểm tra nếu tài khoản đã được xác thực
        User user = (User) session.getAttribute("currentUser"); // Giả sử bạn lưu thông tin người dùng trong session
        if (user != null && !user.getIsVerified()) {  // Kiểm tra xem tài khoản có xác thực chưa
            // Nếu tài khoản chưa xác thực, chuyển hướng sang trang OTP
            return "redirect:/xac-thuc-otp";
        }

        // Nếu đăng nhập thành công và tài khoản đã xác thực, chuyển hướng về trang chủ
        return "redirect:/";
    }


    @GetMapping("/xac-thuc-otp")
    public String showOtpVerificationPage(@RequestParam("email") String email, Model model) {
        // Truyền email vào model để sử dụng trong trang xác thực OTP
        model.addAttribute("email", email);
        return "web/xac-thuc-otp"; // Trả về tên view là "xac-thuc-otp"
    }




    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productCode") String productCode,
                            @RequestParam("amount") int amount,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {

        // Lấy người dùng từ session
        User user = (User) request.getSession().getAttribute("currentUser");

        // Kiểm tra nếu người dùng chưa đăng nhập, hiển thị thông báo
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng");
            return "redirect:/dang-nhap";  // Quay lại trang sản phẩm với thông báo lỗi
        }

        // Tìm sản phẩm theo productCode
        Product product = productService.findByProductCode(productCode);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại");
            return "redirect:/products";
        }

        // Kiểm tra nếu giỏ hàng hiện tại của người dùng có trạng thái `status` là `false` (chưa thanh toán)
        ShoppingCart shoppingCart = shoppingCartService.findOrCreateShoppingCart(user);

        // Nếu không có giỏ hàng chưa thanh toán, tạo mới giỏ hàng
        if (shoppingCart == null) {
            shoppingCart = shoppingCartService.createShoppingCart(user);
        }

        // Thêm sản phẩm vào giỏ hàng
        shoppingCartProductsService.addProductToCart(user, product, amount);

        // Cập nhật thông báo thành công
        redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng");
        return "redirect:/own-shoppingcart";  // Chuyển đến trang giỏ hàng của người dùng
    }





    // Xem giỏ hàng của người dùng

    @GetMapping("/own-shoppingcart")
    public String getOwnPage(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Kiểm tra người dùng đã đăng nhập chưa
        User user = (User) request.getSession().getAttribute("currentUser");

        if (user == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập và thông báo lỗi
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để truy cập giỏ hàng");
            return "redirect:/dang-nhap";
        }

        // Lấy tất cả giỏ hàng của người dùng
        List<ShoppingCart> shoppingCarts = shoppingCartService.getAllShoppingCartOfCurrentUser();
        List<ShoppingCartProducts> allShoppingCartProducts = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Duyệt qua tất cả giỏ hàng và tính tổng giá trị
        for (ShoppingCart cart : shoppingCarts) {
            List<ShoppingCartProducts> shoppingCartProducts = shoppingCartProductsService.getShoppingCartProductsByCode(cart.getShoppingCartCode());

            for (ShoppingCartProducts shoppingCartProduct : shoppingCartProducts) {
                BigDecimal productPrice = detailedProductService.getProductPriceByCode(shoppingCartProduct.getProduct().getProductCode());

                if (productPrice == null) {
                    productPrice = BigDecimal.ZERO;
                }

                shoppingCartProduct.setPrice(productPrice);
                BigDecimal productTotalPrice = productPrice.multiply(BigDecimal.valueOf(shoppingCartProduct.getAmount()));
                totalAmount = totalAmount.add(productTotalPrice);
            }

            allShoppingCartProducts.addAll(shoppingCartProducts);
        }

        // Lấy đơn hàng từ giỏ hàng
        List<Order> orders = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            // Tìm đơn hàng liên quan đến shoppingCartCode
            List<Order> ordersForCart = orderRepository.findByFromShoppingCartCode(cart.getShoppingCartCode());
            orders.addAll(ordersForCart);
        }

        // Thêm giỏ hàng, thông tin sản phẩm và đơn hàng vào model
        model.addAttribute("shoppingCarts", shoppingCarts);
        model.addAttribute("shoppingCartProducts", allShoppingCartProducts);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);

        return "web/shoppingcart";
    }

@GetMapping("/thanh-cong")
public String thanhCong(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return "web/paytc";
}


    // hd oder
    @PostMapping("/add-oder")
    public ResponseEntity<Order> addOrder(@RequestBody Order orderRequest) {


        // Gọi service để thêm mới order
        Order savedOrder = orderService.addOrder(orderRequest);

        // Trả về ResponseEntity chứa đối tượng Order đã lưu
        return ResponseEntity.ok(savedOrder);
    }


    // Lấy danh sách review của blog
        @GetMapping("/blogs/{blogCode}")
        public String getBlogDetail(@PathVariable String blogCode, Model model, HttpSession session,RedirectAttributes redirectAttributes,
                                    HttpServletRequest request) {
            // Tìm blog theo mã
            Blog blog = blogService.findByBlogCode(blogCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Blog không tồn tại"));

            // Lấy danh sách review của blog
            List<Review> reviews = reviewService.getReviewsOfMovie(blogCode);

            // Lấy thông tin người dùng hiện tại từ session
            User user = (User) request.getSession().getAttribute("currentUser");

            if (user == null) {
                // Nếu người dùng chưa đăng nhập, lưu đường dẫn hiện tại để chuyển hướng sau khi đăng nhập
                session.setAttribute("redirectAfterLogin", "/blogs/" + blogCode);
            }
            // Đưa dữ liệu vào model
            model.addAttribute("blog", blog);
            model.addAttribute("reviews", reviews);


            return "web/detailBlog"; // Trả về tên template chi tiết blog
        }
    @PostMapping("/reviews/{blogCode}")
    public String createReview(@PathVariable String blogCode, @ModelAttribute UpsertReviewRequest request,
                               HttpSession session, HttpServletRequest requestContext, RedirectAttributes redirectAttributes) {
        // Lấy thông tin người dùng từ session
        User currentUser = (User) requestContext.getSession().getAttribute("currentUser");

        if (currentUser == null) {
            // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
            session.setAttribute("redirectAfterLogin", "/blogs/" + blogCode);
            return "redirect:/dang-nhap";
        }

        // Gọi service để tạo mới đánh giá
        Review review = reviewService.createReview(request, blogCode, currentUser.getUserCode());

        // Chuyển hướng về trang chi tiết blog với thông báo thành công
        redirectAttributes.addFlashAttribute("message", "Đánh giá của bạn đã được đăng thành công.");
        return "redirect:/blogs/" + blogCode;
    }


    @GetMapping("/bao-hanh")
    public String getBaoHanhPage(Model model, HttpSession session) {
        return "web/bao-hanh";
    }

    // Lấy thông tin người dùng hiện tại từ session

    @GetMapping("/lien-he")
    public String getLienHePage(Model model, HttpSession session) {
        return "web/lien-he";
    }
    @GetMapping("/gioi-thieu")
    public String getGioiThieuPage(Model model, HttpSession session) {
        return "web/gioi-thieu";
    }
    @GetMapping("/huong-dan-mua-hang")
    public String getHDMHPage(Model model, HttpSession session) {
        return "web/huong-dan-mua-hang";
    }
    @GetMapping("/tin-tuc")
    public String getTinTucPage(Model model, HttpSession session) {
        return "web/tin-tuc";
    }

    @Autowired
    private GeminiService geminiService;

    private List<Message> messages = new ArrayList<>();

    @GetMapping("/chat")
    public String index(Model model) {
        model.addAttribute("messages", messages);
        return "web/chat";  // trả về trang chat để render
    }

    // API để xử lý câu hỏi và trả lời
    @PostMapping("/ask")
    @ResponseBody  // Đảm bảo trả về JSON thay vì template
    public ChatResponse ask(@RequestBody ChatRequest chatRequest) {
        messages.add(new Message("user", chatRequest.getQuestion()));

        // Gửi câu hỏi đến Gemini API và nhận câu trả lời
        ChatResponse response = geminiService.getAnswer(chatRequest.getQuestion());

        // Thêm câu trả lời của bot vào danh sách tin nhắn
        messages.add(new Message("bot", response.getAnswer()));

        return response; // Trả về JSON cho frontend
    }

    @GetMapping("/exit")
    public String exit() {
        // Xóa danh sách tin nhắn
        messages.clear();
        return "redirect:/chat"; // Chuyển hướng lại trang chat sau khi thoát
    }

    // Lớp nội bộ để lưu thông tin tin nhắn
    public static class Message {
        private String sender;
        private String text;

        public Message(String sender, String text) {
            this.sender = sender;
            this.text = text;
        }

        public String getSender() {
            return sender;
        }

        public String getText() {
            return text;
        }
    }

    @GetMapping("/chatus")
    public String redirectToChatForUser(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = (User) request.getSession().getAttribute("currentUser");

        // Kiểm tra đăng nhập
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để sử dụng tính năng này");
            return "redirect:/dang-nhap";
        }

        // Lấy user_code và chuyển hướng
        String userCode = user.getUserCode();
        return "redirect:/chatus/" + userCode;
    }

    @GetMapping("/admin/chatus")
    public String redirectToChatForAdmin(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = (User) request.getSession().getAttribute("currentUser");

        // Kiểm tra đăng nhập
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để sử dụng tính năng này");
            return "redirect:/dang-nhap";
        }

        // Kiểm tra vai trò
        if (!user.getRole().equals(UserRole.ADMIN)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập vào trang này");
            return "redirect:/";
        }

        // Lấy user_code và chuyển hướng
        String userCode = user.getUserCode();
        return "redirect:/admin/chatus/" + userCode;
    }


    @GetMapping("/chatus/{user_code}")
    public String chatForUser(
            @PathVariable String user_code,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        User user = (User) request.getSession().getAttribute("currentUser");

        // Kiểm tra đăng nhập
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để sử dụng tính năng này");
            return "redirect:/dang-nhap";
        }

        // Kiểm tra user_code
        if (!user.getUserCode().equals(user_code)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập vào trang này");
            return "redirect:/";
        }

        return "web/chatus";
    }

    @GetMapping("/admin/chatus/{user_code}")
    public String chatForAdmin(
            @PathVariable String user_code,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        User user = (User) request.getSession().getAttribute("currentUser");

        // Kiểm tra đăng nhập
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để sử dụng tính năng này");
            return "redirect:/dang-nhap";
        }

        // Kiểm tra vai trò
        if (!user.getRole().equals(UserRole.ADMIN)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập vào trang này");
            return "redirect:/";
        }

        // Kiểm tra user_code
        if (!user.getUserCode().equals(user_code)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập vào trang này");
            return "redirect:/";
        }

        return "admin/chatus";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        // Giả sử bạn có một service để tìm kiếm sản phẩm
        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        return "web/san-pham/san-pham";  // Trang hiển thị kết quả tìm kiếm
    }
}
