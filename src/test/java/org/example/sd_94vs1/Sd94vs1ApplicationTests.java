package org.example.sd_94vs1;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.example.sd_94vs1.entity.*;
import org.example.sd_94vs1.entity.oder.Order;
import org.example.sd_94vs1.entity.product.*;
import org.example.sd_94vs1.model.enums.UserRole;
import org.example.sd_94vs1.model.request.UpsertReviewRequest;
import org.example.sd_94vs1.repository.*;
import org.example.sd_94vs1.repository.Product.*;
import org.example.sd_94vs1.repository.oder.OrderRepository;
import org.example.sd_94vs1.service.ReviewService;
import org.example.sd_94vs1.service.product.ManufacturerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Sd94vs1ApplicationTests {

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ManufacturerService manufacturerService;
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private BlogRepository blogRepository;
	private final Random random = new Random();
	@Autowired
	private ProductTypeRepository productTypeRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private DetailedProductRepository detailedProductRepository;
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	@Autowired
	private ShoppingCartProductsRepository shoppingCartProductsRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ReviewService reviewService;

	@Test
	void contextLoads() {
	}

	@Test
	void save_all_user() {
		Faker faker = new Faker(); // Faker data
		Random random = new Random();//randum
		for (int i = 0; i < 20; i++) {
			String name = faker.name().fullName();
			String userCode = String.format("us%04d", random.nextInt(10000));
			User user = User.builder()
					.userCode(userCode) // Gán mã người dùng
					.name(name)
					.email(faker.internet().emailAddress())
					.password(bcryptPasswordEncoder.encode("123")) // Mã hóa mật khẩu
					.avatar(generateLinkImage(name)) // Giả định rằng bạn có phương thức để tạo link hình
					.role(i == 0 || i == 1 ? UserRole.ADMIN : UserRole.USER)
					.build();

			userRepository.save(user); // Lưu vào database
		}
	}
	// tự tạo 1 tk của cả nhân nếu muốn

	@Test
	void save_specific_and_random_users1() {
		Faker faker = new Faker(); // Faker data
		Random random = new Random();
		String name = faker.name().fullName();
		String userCode = String.format("us%04d", random.nextInt(10000));
		// Create the specific user
		User specificUser = User.builder()
				.userCode("us270125") // Gán mã người dùng
				.name("may")
				.email("may2701@gmail.com")
				.password(bcryptPasswordEncoder.encode("123")) // Mã hóa mật khẩu
				.avatar("/img/may.jpg") // Giả định rằng bạn có phương thức để tạo link hình
				.role(UserRole.ADMIN)
				.build();

		userRepository.save(specificUser); // Save the specific user


	}

	@Test
	public void testManufacturerCreation() {
		// Tạo nhà cung cấp
		Manufacturer manufacturer = Manufacturer.builder()
				.manufacturerCode("M001") // Mã nhà cung cấp
				.name("DJI")              // Tên nhà cung cấp
				.country("Trung Quốc")    // Quốc gia
				.build();

		manufacturerRepository.save(manufacturer);

	}

	@Test
	@Rollback(false) // Để không rollback khi bài kiểm tra kết thúc
	public void testAddCategories() {
		// Danh sách các loại sản phẩm
		List<Category> categories = List.of(
				new Category("cat001", "DJI Mavic", new Date(), null, "active", "/img/anh1.jpg"),
				new Category("cat002", "DJI Air", new Date(), null, "active", "/img/anh2.jpg"),
				new Category("cat003", "DJI Mini", new Date(), null, "active", "/img/anh3.jpg"),
				new Category("cat004", "DJI FPV", new Date(), null, "active", "/img/anh4.jpg"),
				new Category("cat005", "Osmo Pocket", new Date(), null, "active", "/img/anh5.jpg"),
				new Category("cat006", "Osmo Action", new Date(), null, "active", "/img/anh6.jpg"),
				new Category("cat007", "Osmo Mobile", new Date(), null, "active", "/img/anh7.jpg"),
				new Category("cat008", "DJI Ronin", new Date(), null, "active", "/img/anh8.jpg"),
				new Category("cat009", "DJI Power", new Date(), null, "active", "/img/anh9.jpg"),
				new Category("cat010", "Sản phẩm khác", new Date(), null, "active", "/img/anh10.jpg")
		);

		// Lưu tất cả các loại sản phẩm vào cơ sở dữ liệu
		categoryRepository.saveAll(categories);

		// Kiểm tra xem số lượng loại sản phẩm đã lưu

	}

	private static String getCharacter(String str) {
		return str.substring(0, 1).toUpperCase();
	}

	public static String generateLinkImage(String name) {
		return "https://placehold.co/200x200?text=" + getCharacter(name);
	}

	// blog
	private String generateBlogCode() {
		// Tạo số ngẫu nhiên từ 0 đến 9999
		int number = random.nextInt(10000);
		// Định dạng số với 4 chữ số
		return String.format("bl%04d", number);
	}

	private String generateUniqueBlogCode() {
		String blogCode;
		do {
			blogCode = generateBlogCode(); // Tạo mã blogCode ngẫu nhiên
		} while (blogRepository.existsById(blogCode)); // Kiểm tra xem mã đã tồn tại chưa
		return blogCode;
	}

	@Test
	void save_all_blog() {
		List<User> userList = userRepository.findByRole(UserRole.ADMIN);

		Faker faker = new Faker(); // Faker data
		Random random = new Random();
		Slugify slugify = Slugify.builder().build(); // Generate slug

		for (int i = 0; i < 20; i++) {
			// Random 1 user
			User rdUser = userList.get(random.nextInt(userList.size()));

			boolean status = faker.bool().bool();
			Date createdAt = new Date();
			Date publishedAt = null;

			if (status) {
				publishedAt = createdAt;
			}

			// Tạo blog
			String title = faker.book().title();

			// Tạo blogCode ngẫu nhiên
			String blogCode = generateBlogCode(); // Gọi phương thức tạo mã blogCode

			Blog blog = Blog.builder()
					.blogCode(blogCode) // Sử dụng mã blogCode đã tạo
					.title(title)
					.slug(slugify.slugify(title))
					.description(faker.lorem().paragraph())
					.content(faker.lorem().paragraph())
					.thumbnail("/img/Osmo-Action-9.png")
					.status(status)
					.createdAt(createdAt)
					.updatedAt(createdAt)
					.publishedAt(publishedAt)
					.user(rdUser)
					.build();
			blogRepository.save(blog); // Lưu vào database
		}
	}

	// hãng
	@Test
	public void saveManufacturerData() {
		// Tạo Manufacturer với dữ liệu bạn yêu cầu
		Manufacturer manufacturer = Manufacturer.builder()
				.manufacturerCode("ma001")
				.name("DJI")
				.country("Trung Quốc")
				.build();

		// Lưu vào database
		manufacturerRepository.save(manufacturer);

		// Kiểm tra xem dữ liệu đã được lưu hay chưa
		Manufacturer savedManufacturer = manufacturerRepository.findById("ma001").orElse(null);
		assert savedManufacturer != null;
		assert "DJI".equals(savedManufacturer.getName());
		assert "Trung Quốc".equals(savedManufacturer.getCountry());
	}

	@Test
	public void saveProductTypeData() {
		// Thêm dữ liệu cho từng ProductType
		ProductType productType1 = ProductType.builder()
				.productTypeCode("bn001")
				.productTypeName("Bản đơn")
				.description("1 nửa bộ cánh, 1 body, 1 pin, 1 remote, 3 sợi cáp")
				.build();

		ProductType productType2 = ProductType.builder()
				.productTypeCode("bn002")
				.productTypeName("Bản combo")
				.description("1 bộ cánh, 1 body, 3 pin, 1 hub sạc, 1 túi, 3 sợi cáp")
				.build();

		ProductType productType3 = ProductType.builder()
				.productTypeCode("bn003")
				.productTypeName("Bản đơn tay RC")
				.description("1 nửa bộ cánh, 1 body, 1 pin, 1 remote")
				.build();

		ProductType productType4 = ProductType.builder()
				.productTypeCode("bn004")
				.productTypeName("Bản combo tay RC")
				.description("1 bộ cánh, 1 body, 3 pin, 1 hub sạc, 1 túi")
				.build();
		ProductType productType5 = ProductType.builder()
				.productTypeCode("bn005")
				.productTypeName("Bản đơn")
				.description("full phụ kiện như hãng bán")
				.build();
		ProductType productType6 = ProductType.builder()
				.productTypeCode("bn006")
				.productTypeName("Bản combo")
				.description("full phụ kiện như hãng bán")
				.build();

		// Lưu vào cơ sở dữ liệu
		productTypeRepository.save(productType1);
		productTypeRepository.save(productType2);
		productTypeRepository.save(productType3);
		productTypeRepository.save(productType4);
		productTypeRepository.save(productType5);
		productTypeRepository.save(productType6);


		// Xác minh dữ liệu đã lưu

	}
////product
//// trc khi chạy cần phải vào product tạm xóa biến lưu tạm productPrice
//@Test
//@Rollback(false) // Để không rollback khi bài kiểm tra kết thúc
//public void testAddProducts() {
//	List<Product> products = List.of(
//			// Mavic 3 - Bản đơn
//			new Product("mv0001", "Mavic 3 cine", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//
//			// Mavic 3 - Bản combo
//			new Product("mv0002", "Mavic 3 cine", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3.jpg",
//					productTypeRepository.findById("bn002").orElse(null)),
//
//			// Mavic 3 - Bản đơn tay RC
//			new Product("mv0003", "Mavic 3 cine", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3.jpg",
//					productTypeRepository.findById("bn003").orElse(null)),
//
//			// Mavic 3 - Bản combo tay RC
//			new Product("mv0004", "Mavic 3 cine", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3.jpg",
//					productTypeRepository.findById("bn004").orElse(null)),
//
//			// Mavic 3 pro - Bản đơn
//			new Product("mv0005", "Mavic 3 pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//
//			// Mavic 3 pro - Bản combo
//			new Product("mv0006", "Mavic 3 pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn002").orElse(null)),
//
//			// Mavic 3 pro- Bản đơn tay RC
//			new Product("mv0007", "Mavic 3 pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn003").orElse(null)),
//
//			// Mavic 3 pro- Bản combo tay RC
//			new Product("mv0008", "Mavic 3 pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn004").orElse(null)),
//			// Mavic 3 pro - Bản đơn
//			new Product("mv0009", "Mavic 2 pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//
//			// Mavic 3 pro - Bản combo
//			new Product("mv00010", "Mavic 2 pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn002").orElse(null)),
//
//			// Mavic 3 pro- Bản đơn tay RC
//			new Product("mv00011", "Mavic 2 pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn003").orElse(null)),
//
//			// Mavic 3 pro- Bản combo tay RC
//			new Product("mv00012", "Mavic 2 pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn004").orElse(null)),
//			new Product("mv00013", "Mavic pro", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//			new Product("mv00014", "Mavic pro platilum", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//			new Product("mv00015", "Mavic pro white", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//			new Product("mv00016", "Mavic 2 pro cine", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//			new Product("mv00017", "Mavic 2 pro entertaiment", "Drone nhỏ gọn với khả năng quay video 4K",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat001").orElse(null),
//					new Date(), null, "active", "/img/mavic3pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//
//			// DJI FPV - Bản đơn
//			new Product("fpv0001", "DJI FPV Flas", "Drone với chế độ góc nhìn thứ nhất, tốc độ cao",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat004").orElse(null),
//					new Date(), null, "active", "/img/fpvflas.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//
//			// DJI FPV - Bản combo
//			new Product("fpv0002", "DJI FPV Flas", "Drone với chế độ góc nhìn thứ nhất, tốc độ cao",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat004").orElse(null),
//					new Date(), null, "active", "/img/fpvflas.jpg",
//					productTypeRepository.findById("bn002").orElse(null)),
//
//			// Mini 4 Pro - Bản đơn
//			new Product("mn0001", "Mini 4 Pro", "Drone nhỏ gọn với chất lượng quay 4K và điều khiển dễ dàng",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat003").orElse(null),
//					new Date(), null, "active", "/img/mini4pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//
//			// Mini 4 Pro - Bản combo
//			new Product("mn0002", "Mini 4 Pro", "Drone nhỏ gọn với chất lượng quay 4K và điều khiển dễ dàng",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat003").orElse(null),
//					new Date(), null, "active", "/img/mini4pro.jpg",
//					productTypeRepository.findById("bn002").orElse(null)),
//
//			// Mini 4 Pro - Bản đơn tay RC
//			new Product("mn0003", "Mini 4 Pro", "Drone nhỏ gọn với chất lượng quay 4K và điều khiển dễ dàng",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat003").orElse(null),
//					new Date(), null, "active", "/img/mini4pro.jpg",
//					productTypeRepository.findById("bn003").orElse(null)),
//
//			// Mini 4 Pro - Bản combo tay RC
//			new Product("mn0004", "Mini 4 Pro", "Drone nhỏ gọn với chất lượng quay 4K và điều khiển dễ dàng",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat003").orElse(null),
//					new Date(), null, "active", "/img/mini4pro.jpg",
//					productTypeRepository.findById("bn004").orElse(null)),
//			// Mini 4 Pro - Bản đơn
//			new Product("mn0007", "Mini 3 Pro", "Drone nhỏ gọn với chất lượng quay 4K và điều khiển dễ dàng",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat003").orElse(null),
//					new Date(), null, "active", "/img/mini4pro.jpg",
//					productTypeRepository.findById("bn001").orElse(null)),
//
//			// Mini 4 Pro - Bản combo
//			new Product("mn0008", "Mini 3 Pro", "Drone nhỏ gọn với chất lượng quay 4K và điều khiển dễ dàng",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat003").orElse(null),
//					new Date(), null, "active", "/img/mini4pro.jpg",
//					productTypeRepository.findById("bn002").orElse(null)),
//
//			// Mini 4 Pro - Bản đơn tay RC
//			new Product("mn0009", "Mini 3 Pro", "Drone nhỏ gọn với chất lượng quay 4K và điều khiển dễ dàng",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat003").orElse(null),
//					new Date(), null, "active", "/img/mini4pro.jpg",
//					productTypeRepository.findById("bn003").orElse(null)),
//
//			// Mini 4 Pro - Bản combo tay RC
//			new Product("mn00010", "Mini 3 Pro", "Drone nhỏ gọn với chất lượng quay 4K và điều khiển dễ dàng",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat003").orElse(null),
//					new Date(), null, "active", "/img/mini4pro.jpg",
//					productTypeRepository.findById("bn004").orElse(null))
//	);
//
//	// Lưu danh sách sản phẩm vào cơ sở dữ liệu
//	productRepository.saveAll(products);
//
//	// Kiểm tra xem các sản phẩm đã được lưu chưa
//
//}
//	@Test
//	@Rollback(false) // Để không rollback khi bài kiểm tra kết thúc
//	public void testAddMoreProducts() {
//		List<Product> additionalProducts = List.of(
//				// Mavic Air 2S - Bản đơn
//				new Product("mar0001", "Mavic Air 2S", "Drone nhỏ gọn, chất lượng quay video 5.4K",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat001").orElse(null),
//						new Date(), null, "active", "/img/mavic_air_2s.jpg",
//						productTypeRepository.findById("bn001").orElse(null)),
//
//				// Mavic Air 2S - Bản combo
//				new Product("mar0002", "Mavic Air 2S", "Drone nhỏ gọn, chất lượng quay video 5.4K",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat001").orElse(null),
//						new Date(), null, "active", "/img/mavic_air_2s.jpg",
//						productTypeRepository.findById("bn002").orElse(null)),
//				// Mavic Air 2S - Bản đơn tay rc
//				new Product("mar0003", "Mavic Air 2S", "Drone nhỏ gọn, chất lượng quay video 5.4K",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat001").orElse(null),
//						new Date(), null, "active", "/img/mavic_air_2s.jpg",
//						productTypeRepository.findById("bn003").orElse(null)),
//
//				// Mavic Air 2S - Bản combo tay rc
//				new Product("mar0004", "Mavic Air 2S", "Drone nhỏ gọn, chất lượng quay video 5.4K",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat001").orElse(null),
//						new Date(), null, "active", "/img/mavic_air_2s.jpg",
//						productTypeRepository.findById("bn004").orElse(null)),
//
//
//				// Phantom 4 Pro - Bản đơn
//				new Product("pt0001", "Phantom 4 Pro", "Drone ổn định, chất lượng hình ảnh cao, phù hợp quay phim chuyên nghiệp",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat002").orElse(null),
//						new Date(), null, "active", "/img/phantom4pro.jpg",
//						productTypeRepository.findById("bn001").orElse(null)),
//
//				// Phantom 4 Pro - Bản combo
//				new Product("pt0002", "Phantom 4 Pro", "Drone ổn định, chất lượng hình ảnh cao, phù hợp quay phim chuyên nghiệp",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat002").orElse(null),
//						new Date(), null, "active", "/img/phantom4pro.jpg",
//						productTypeRepository.findById("bn002").orElse(null)),
//
//				// Inspire 2 - Bản đơn
//				new Product("ins0001", "Inspire 2", "Drone cao cấp cho quay phim chuyên nghiệp với chất lượng hình ảnh vượt trội",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat003").orElse(null),
//						new Date(), null, "active", "/img/inspire2.jpg",
//						productTypeRepository.findById("bn001").orElse(null)),
//
//				// Osmo Action - Bản đơn
//				// Dòng DJI Osmo Action từ 1 đến 5 Pro
//				new Product("om0001", "Osmo Action 1", "Camera hành động đầu tiên của DJI với khả năng chống nước",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat006").orElse(null),
//						new Date(), null, "active", "/img/osmo_action1.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("om0002", "Osmo Action 2", "Camera hành động thế hệ 2 với thiết kế mô-đun độc đáo",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat006").orElse(null),
//						new Date(), null, "active", "/img/osmo_action2.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("om0003", "Osmo Action 3", "Camera hành động thế hệ 3 với cải tiến về hiệu suất và độ bền",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat006").orElse(null),
//						new Date(), null, "active", "/img/osmo_action3.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("om0004", "Osmo Action 4", "Camera hành động thế hệ 4 với khả năng quay phim 4K",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat006").orElse(null),
//						new Date(), null, "active", "/img/osmo_action4.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("om0005", "Osmo Action 5 Pro", "Phiên bản Pro của Osmo Action 5 với chất lượng hình ảnh vượt trội và khả năng chống nước tối ưu",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat006").orElse(null),
//						new Date(), null, "active", "/img/osmo_action5_pro.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//
//				// Osmo Pocket - Bản đơn
//				new Product("op0001", "Osmo Pocket", "Camera nhỏ gọn, dễ sử dụng cho quay video di động.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat007").orElse(null),
//						new Date(), null, "active", "/img/osmo_pocket.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("op0002", "Osmo Pocket 2", "Camera nâng cấp với khả năng âm thanh và tính năng vượt trội.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat007").orElse(null),
//						new Date(), null, "active", "/img/osmo_pocket_2.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("op0003", "Osmo Pocket 3", "Phiên bản mới nhất với cảm biến lớn hơn và nhiều tính năng thông minh.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat007").orElse(null),
//						new Date(), null, "active", "/img/osmo_pocket_3.jpg",
//						productTypeRepository.findById("bn005").orElse(null))
//
//
//
//				);
//
//		// Lưu các sản phẩm bổ sung vào cơ sở dữ liệu
//		productRepository.saveAll(additionalProducts);
//
//		// Xác minh các sản phẩm đã được lưu
//
//	}
//
//	@Test
//	@Rollback(false)
//	public void testAddAdditionalProducts() {
//		List<Product> moreProducts = List.of(
//				// Mavic Mini - Bản đơn
//				new Product("mn0005", "Mavic Mini", "Drone nhỏ gọn, trọng lượng nhẹ, dễ sử dụng",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat003").orElse(null),
//						new Date(), null, "active", "/img/mavic_mini.jpg",
//						productTypeRepository.findById("bn001").orElse(null)),
//
//				// Mavic Mini - Bản combo
//				new Product("mn0006", "Mavic Mini", "Drone nhỏ gọn, trọng lượng nhẹ, dễ sử dụng",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat003").orElse(null),
//						new Date(), null, "active", "/img/mavic_mini.jpg",
//						productTypeRepository.findById("bn002").orElse(null)),
//
//				// DJI Ronin-S - Bản đơn
//				new Product("rn0001", "Ronin-S", "Gimbal chống rung cho máy quay chuyên nghiệp",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat008").orElse(null),
//						new Date(), null, "active", "/img/ronin_s.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				// DJI Ronin-S - Bản combo
//				new Product("rn0002", "Ronin-S", "Gimbal chống rung cho máy quay chuyên nghiệp",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat008").orElse(null),
//						new Date(), null, "active", "/img/ronin_s.jpg",
//						productTypeRepository.findById("bn006").orElse(null)),
//
//				// DJI Osmo Mobile 5 - Bản đơn
//				new Product("omm0001", "Osmo Mobile", "Gimbal di động cho smartphone, giúp quay video ổn định.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat008").orElse(null),
//						new Date(), null, "active", "/img/osmo_mobile_1.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("omm0002", "Osmo Mobile 2", "Gimbal với pin lâu hơn và thiết kế gọn nhẹ.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat008").orElse(null),
//						new Date(), null, "active", "/img/osmo_mobile_2.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("omm0003", "Osmo Mobile 3", "Thiết kế gập lại dễ dàng và tính năng chống rung cải tiến.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat008").orElse(null),
//						new Date(), null, "active", "/img/osmo_mobile_3.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("omm0004", "Osmo Mobile 4", "Gimbal với công nghệ từ tính cho trải nghiệm sử dụng thuận tiện.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat008").orElse(null),
//						new Date(), null, "active", "/img/osmo_mobile_4.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("omm0005", "Osmo Mobile 5", "Gimbal mới nhất với các tính năng thông minh và ổn định nâng cao.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat008").orElse(null),
//						new Date(), null, "active", "/img/osmo_mobile_5.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				new Product("omm0006", "Osmo Mobile 6", "Gimbal di động với điều khiển thông minh và tính năng video nâng cao.",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat008").orElse(null),
//						new Date(), null, "active", "/img/osmo_mobile_6.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//
//				// DJI Matrice 300 RTK - Bản đơn
//				new Product("mas001", "Matrice 300 RTK", "Drone chuyên dụng cho các ứng dụng công nghiệp",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat010").orElse(null),
//						new Date(), null, "active", "/img/matrice_300.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				// DJI Matrice 300 RTK - Bản combo
//				new Product("mas002", "Matrice 300 RTK", "Drone chuyên dụng cho các ứng dụng công nghiệp",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat010").orElse(null),
//						new Date(), null, "active", "/img/matrice_300.jpg",
//						productTypeRepository.findById("bn002").orElse(null)),
//
//				// DJI Agras T30 - Bản đơn
//				new Product("mas003", "Agras T30", "Drone nông nghiệp hỗ trợ phun thuốc và giám sát mùa màng",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat010").orElse(null),
//						new Date(), null, "active", "/img/agras_t30.jpg",
//						productTypeRepository.findById("bn001").orElse(null)),
//
//				// DJI Agras T30 - Bản combo
//				new Product("mas004", "Agras T30", "Drone nông nghiệp hỗ trợ phun thuốc và giám sát mùa màng",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat010").orElse(null),
//						new Date(), null, "active", "/img/agras_t30.jpg",
//						productTypeRepository.findById("bn002").orElse(null)),
//
//				// DJI Mic - Bản đơn
//				new Product("mic001", "DJI Mic", "Hệ thống micro không dây chất lượng cao cho quay phim",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat010").orElse(null),
//						new Date(), null, "active", "/img/dji_mic.jpg",
//						productTypeRepository.findById("bn005").orElse(null)),
//
//				// DJI Mic - Bản combo
//				new Product("mic002", "DJI Mic", "Hệ thống micro không dây chất lượng cao cho quay phim",
//						manufacturerRepository.findById("ma001").orElse(null),
//						categoryRepository.findById("cat010").orElse(null),
//						new Date(), null, "active", "/img/dji_mic.jpg",
//						productTypeRepository.findById("bn006").orElse(null))
//		);
//		// Lưu các sản phẩm bổ sung vào cơ sở dữ liệu
//		productRepository.saveAll(moreProducts);
//	}
//
//@Test
//	@Rollback(false)
//	public void testAddAdditionalProducts() {
//	// DJI Ronin 2
//	List<Product> moreProducts = List.of(
//	new Product("rn0005", "Ronin 2", "Gimbal chống rung mạnh mẽ, thiết kế dành cho máy quay chuyên nghiệp",
//			manufacturerRepository.findById("ma001").orElse(null),
//			categoryRepository.findById("cat008").orElse(null),
//			new Date(), null, "active", "/img/ronin_s.jpg",
//			productTypeRepository.findById("bn005").orElse(null)),
//
//// DJI Ronin 3
//			new Product("rn0006", "Ronin 3", "Gimbal chống rung với khả năng ổn định vượt trội, phiên bản mới với công nghệ hiện đại",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat008").orElse(null),
//					new Date(), null, "active", "/img/ronin_s.jpg",
//					productTypeRepository.findById("bn005").orElse(null)),
//
//// DJI Ronin 3 - Bản combo
//			new Product("rn0007", "Ronin 3", "Gimbal chống rung với khả năng ổn định vượt trội, phiên bản combo với đầy đủ phụ kiện",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat008").orElse(null),
//					new Date(), null, "active", "/img/ronin_s.jpg",
//					productTypeRepository.findById("bn006").orElse(null)),
//// DJI Ronin 4 Pro
//			new Product("rn0003", "Ronin 4 Pro", "Gimbal chống rung cho máy quay chuyên nghiệp, phiên bản mới với nhiều tính năng nâng cấp",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat008").orElse(null),
//					new Date(), null, "active", "/img/ronin_s.jpg",
//					productTypeRepository.findById("bn005").orElse(null)),
//
//// DJI Ronin 4 Pro - Bản combo
//			new Product("rn0004", "Ronin 4 Pro", "Gimbal chống rung cho máy quay chuyên nghiệp, phiên bản combo với phụ kiện đầy đủ",
//					manufacturerRepository.findById("ma001").orElse(null),
//					categoryRepository.findById("cat008").orElse(null),
//					new Date(), null, "active", "/img/ronin_s.jpg",
//					productTypeRepository.findById("bn006").orElse(null))
//			);
//			productRepository.saveAll(moreProducts);
//}
	//	detail
	@Test
	@Rollback(false)
	public void createDetailedProducts() {
		// Danh sách mã sản phẩm cần tạo DetailedProduct
		List<String> productCodes = Arrays.asList(
				"fpv0001", "fpv0002", "ins0001", "mar0001", "mar0002", "mar0003", "mar0004",
				"mas001", "mas002", "mas003", "mas004", "mic001", "mic002", "mn0001", "mn00010",
				"mn0002", "mn0003", "mn0004", "mn0005", "mn0006", "mn0007", "mn0008", "mn0009",
				"mv0001", "mv00010", "mv00011", "mv00012", "mv00013", "mv00014", "mv00015", "mv00016",
				"mv00017", "mv0002", "mv0003", "mv0004", "mv0005", "mv0006", "mv0007", "mv0008", "mv0009",
				"om0001", "om0002", "om0003", "om0004", "om0005", "omm0001", "omm0002", "omm0003",
				"omm0004", "omm0005", "omm0006", "op0001", "op0002", "op0003", "pt0001", "pt0002",
				"rn0001", "rn0002"
		);

		for (String code : productCodes) {
			// Tìm sản phẩm từ mã code
			productRepository.findByProductCode(code).ifPresentOrElse(
					product -> {
						// Nếu tìm thấy sản phẩm, tạo DetailedProduct
						DetailedProduct detailedProduct = DetailedProduct.builder()
								.detailedProductCode("dt" + code) // tạo mã chi tiết duy nhất
								.product(product) // gán sản phẩm vào chi tiết sản phẩm
								.description("Mô tả chi tiết cho sản phẩm " + product.getName()) // mô tả chi tiết gồm tên sản phẩm và loại sản phẩm
								.quantity(100) // số lượng mặc định
								.priceVND(BigDecimal.valueOf(5000000)) // giá mặc định
								.date(new Date()) // ngày tạo
								// ngày tạo
								.build();

						// Lưu vào cơ sở dữ liệu
						detailedProductRepository.save(detailedProduct);
					},
					() -> {
						// Nếu không tìm thấy sản phẩm
						System.out.println("Không tìm thấy sản phẩm với mã " + code);
					}
			);
		}

	}

	@Test
	@Rollback(false)
	public void createDetailedProducts1() {
		// Danh sách mã sản phẩm cần tạo DetailedProduct
		List<String> productCodes = Arrays.asList(
				 "rn0003", "rn0004", "rn0005", "rn0006", "rn0007" // Thêm các mã mới ở đây
		);

		for (String code : productCodes) {
			// Tìm sản phẩm từ mã code
			productRepository.findByProductCode(code).ifPresentOrElse(
					product -> {
						// Nếu tìm thấy sản phẩm, tạo DetailedProduct
						DetailedProduct detailedProduct = DetailedProduct.builder()
								.detailedProductCode("dt" + code) // tạo mã chi tiết duy nhất
								.product(product) // gán sản phẩm vào chi tiết sản phẩm
								.description("Mô tả chi tiết cho sản phẩm " + product.getName()) // mô tả chi tiết gồm tên sản phẩm và loại sản phẩm
								.quantity(100) // số lượng mặc định
								.priceVND(BigDecimal.valueOf(5000000)) // giá mặc định
								.date(new Date()) // ngày tạo
								// ngày tạo
								.build();

						// Lưu vào cơ sở dữ liệu
						detailedProductRepository.save(detailedProduct);
					},
					() -> {
						// Nếu không tìm thấy sản phẩm
						System.out.println("Không tìm thấy sản phẩm với mã " + code);
					}
			);
		}
	}


	private String generateShoppingCartCode() {
		return "GH" + (new Random().nextInt(9000) + 1000);
	}

//	@Test
//	public void saveShoppingCartData() {
//		// Tìm User với userCode là "us270125"
//		List<User> users = userRepository.findByUserCode("us270125");
//
//// Kiểm tra xem danh sách người dùng có tồn tại và không rỗng
//		assert users != null && !users.isEmpty();
//
//// Lấy User đầu tiên từ danh sách (giả sử chỉ có một user với userCode này)
//		User user = users.get(0);
//
//// Tạo 2 ShoppingCart với 2 ngày khác nhau
//		ShoppingCart cart1 = ShoppingCart.builder()
//				.shoppingCartCode(generateShoppingCartCode())
//				.user(user)
//				.status(true)
//				.createdAt(new Date())
//				.build();
//
//		ShoppingCart cart2 = ShoppingCart.builder()
//				.shoppingCartCode(generateShoppingCartCode())
//				.user(user)
//				.status(false)
//				.createdAt(new Date(System.currentTimeMillis() - 86400000L)) // Trừ 1 ngày
//				.build();
//
//// Lưu dữ liệu vào database
//		shoppingCartRepository.save(cart1);
//		shoppingCartRepository.save(cart2);
//
//
//		shoppingCartRepository.save(cart1);
//		shoppingCartRepository.save(cart2);
//
//
//	}

	@Test
	public void addProductsToShoppingCarts() {
		// Fetch shopping carts by codes
		ShoppingCart cart1 = shoppingCartRepository.findById("GH6394").orElseThrow();
		ShoppingCart cart2 = shoppingCartRepository.findById("GH2423").orElseThrow();

		// Fetch products by codes
		Product product1 = productRepository.findById("fpv0001").orElseThrow();
		Product product2 = productRepository.findById("fpv0002").orElseThrow();
		Product product3 = productRepository.findById("ins0001").orElseThrow();
		Product product4 = productRepository.findById("mar0001").orElseThrow();

		// Create ShoppingCartProducts entries for cart1
		ShoppingCartProducts cart1Product1 = ShoppingCartProducts.builder()
				.shoppingCartProductCode("CP1-" + System.currentTimeMillis()) // Gán mã cho sản phẩm giỏ hàng
				.shoppingCart(cart1)
				.product(product1)
				.amount(2)
				.createdAt(new Date())
				.updatedAt(new Date())
				.price(BigDecimal.valueOf(5000))
				.build();

		ShoppingCartProducts cart1Product2 = ShoppingCartProducts.builder()
				.shoppingCartProductCode("CP2-" + System.currentTimeMillis()) // Gán mã cho sản phẩm giỏ hàng
				.shoppingCart(cart1)
				.product(product2)
				.amount(1)
				.createdAt(new Date())
				.updatedAt(new Date())
				.price(BigDecimal.valueOf(3000))
				.build();

		// Save cart1 products
		shoppingCartProductsRepository.save(cart1Product1);
		shoppingCartProductsRepository.save(cart1Product2);

		// Create ShoppingCartProducts entries for cart2
		ShoppingCartProducts cart2Product1 = ShoppingCartProducts.builder()
				.shoppingCartProductCode("CP3-" + System.currentTimeMillis()) // Gán mã cho sản phẩm giỏ hàng
				.shoppingCart(cart2)
				.product(product3)
				.amount(3)
				.createdAt(new Date())
				.updatedAt(new Date())
				.price(BigDecimal.valueOf(7500))
				.build();

		ShoppingCartProducts cart2Product2 = ShoppingCartProducts.builder()
				.shoppingCartProductCode("CP4-" + System.currentTimeMillis()) // Gán mã cho sản phẩm giỏ hàng
				.shoppingCart(cart2)
				.product(product4)
				.amount(2)
				.createdAt(new Date())
				.updatedAt(new Date())
				.price(BigDecimal.valueOf(8500))
				.build();

		// Save cart2 products
		shoppingCartProductsRepository.save(cart2Product1);
		shoppingCartProductsRepository.save(cart2Product2);

		// Assertions
		// Ensure that the cart1 and cart2 products were saved correctly
		assertNotNull(cart1Product1.getShoppingCartProductCode());
		assertNotNull(cart1Product2.getShoppingCartProductCode());
		assertNotNull(cart2Product1.getShoppingCartProductCode());
		assertNotNull(cart2Product2.getShoppingCartProductCode());

		// Further assertions can be added here to check that the amounts, prices, and relationships are correct.
	}

// themoder
private final Faker faker = new Faker();


	// Phương thức để tạo orderCode theo cấu trúc 'hd' + số ngẫu nhiên 4 chữ số
	private String generateOrderCode() {
		int randomNumber = 1000 + random.nextInt(9000);
		return "hd" + randomNumber;
	}

	@Test
	public void save_all_orders() {
		// Lấy thông tin User với userCode là us270125



			for (int i = 0; i < 2; i++) {
				String orderCode = generateOrderCode(); // Tạo mã orderCode
				boolean status = faker.bool().bool();
				Date createdAt = new Date();
				Date approvedAt = status ? createdAt : null;

				Order order = Order.builder()
						.orderCode(orderCode)
						.userCode("us270125")
						.shippingAddress(faker.address().fullAddress())

						.totalAmountMoney(faker.number().randomDouble(2, 500000, 2000000)) // Tổng tiền ngẫu nhiên


						.status(status)
						.createdAt(createdAt)
						.updatedAt(createdAt)
						.approvedAt(approvedAt)
						.build();

				orderRepository.save(order); // Lưu vào database
			}

	}

	@Test
	public void testSaveReview() {
		Blog blog = blogRepository.findById("bl9144").orElseThrow();
		User user=userRepository.findById("us270125").orElseThrow();
		// Tạo review
		Review review = Review.builder()
				.reviewCode("rv1001")
				.content("This is a test review")
				.rating(8)
				.blog(blog)
				.user(user)
				.build();

		// Lưu review vào database
		Review savedReview = reviewRepository.save(review);

		// Kiểm tra xem review đã được lưu chưa
		Optional<Review> fetchedReview = reviewRepository.findById("rv1001");

		Assertions.assertTrue(fetchedReview.isPresent(), "Review không được lưu thành công");
		Assertions.assertEquals("This is a test review", fetchedReview.get().getContent());
		Assertions.assertEquals(8, fetchedReview.get().getRating());
		Assertions.assertEquals("bl9144", fetchedReview.get().getBlog().getBlogCode());
		Assertions.assertEquals("us270125", fetchedReview.get().getUser().getUserCode());
	}

	@Test
	public void testCreateReview() {
		// Chuẩn bị dữ liệu
		UpsertReviewRequest request = new UpsertReviewRequest();
		request.setRating(5);
		request.setContent("Bài viết rất hay!");

		String blogCode = "bl9144";
		String userCode = "us270125";

		// Thực hiện gọi phương thức createReview
		Review review = reviewService.createReview(request, blogCode, userCode);

		// Kiểm tra kết quả
		assertNotNull(review); // Kiểm tra rằng review không null
		System.out.println("Review created: " + review); // In kết quả ra console
	}

}