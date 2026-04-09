# 🐧 ENGLISH PRO - Website Học Tiếng Anh

> **Dự án TTCS Nhóm 5** – Ứng dụng web học tiếng Anh trực tuyến xây dựng bằng Spring Boot.

---

## 📋 Tổng Quan

**English Pro** là một nền tảng web hỗ trợ học tiếng Anh với đầy đủ các tính năng: từ vựng, ngữ pháp, bài viết, video, chính tả (dictation), quiz kiểm tra, và hệ thống subscription (gói trả phí). Project được thiết kế theo mô hình **MVC (Model-View-Controller)** với phân quyền Admin/User.

---

## 🛠️ Công Nghệ Sử Dụng

| Thành phần | Công nghệ |
|---|---|
| **Backend** | Spring Boot 3.5.13, Java 21 |
| **ORM** | Spring Data JPA / Hibernate |
| **Database** | MySQL 8 |
| **Template Engine** | Thymeleaf |
| **Bảo mật** | Spring Security + BCrypt |
| **Frontend** | TailwindCSS (CDN), FontAwesome 6.5, Chart.js |
| **Tiện ích** | Lombok |
| **Build Tool** | Maven |

---

## 📁 Cấu Trúc Project

```
src/main/java/com/ttcsn5/webstudyenglish/
├── WebstudyenglishApplication.java          # Main class
├── config/
│   └── SecurityConfig.java                  # Cấu hình Spring Security
├── controller/
│   ├── AuthController.java                  # Đăng nhập, đăng ký, đăng xuất
│   ├── AdminController.java                 # Điều hướng trang admin (đang phát triển)
│   └── UserController.java                  # Quản lý user (phân trang)
├── entity/                                  # 17 entity (JPA)
│   ├── User.java, Role.java                 # Người dùng & phân quyền
│   ├── Vocabulary.java, UserVocabulary.java  # Từ vựng & Spaced Repetition
│   ├── Grammar.java                         # Ngữ pháp
│   ├── Article.java, Category.java          # Bài viết & danh mục
│   ├── Video.java                           # Video bài giảng
│   ├── Quiz.java, Question.java             # Hệ thống quiz
│   ├── UserQuizAttempt.java, UserAnswer.java # Lưu kết quả quiz
│   ├── DictationTopics.java, DictationSentences.java  # Luyện chính tả
│   ├── Plan.java, Subscription.java         # Gói trả phí & thanh toán
│   └── Notification.java                    # Thông báo
├── repository/
│   ├── AccountRepo.java                     # CRUD User
│   └── RoleRepo.java                        # CRUD Role
├── service/
│   ├── AccountService.java                  # Logic nghiệp vụ user
│   ├── RoleService.java                     # Logic nghiệp vụ role
│   └── HashPassword.java                    # Mã hóa mật khẩu BCrypt
└── data/
    └── DataIn.java                          # Seed dữ liệu ban đầu (Role)

src/main/resources/
├── application.properties                   # Cấu hình DB, Thymeleaf
├── static/
│   ├── img/                                 # Logo, hình ảnh
│   └── js/
│       ├── user.js                          # Phân trang user
│       └── adminarticle.js                  # Modal CRUD bài viết
└── templates/
    ├── login.html                           # Trang đăng nhập
    ├── register.html                        # Trang đăng ký
    ├── admin/
    │   ├── adminhome.html                   # Layout admin (sidebar + content)
    │   ├── dashboard.html                   # Dashboard với biểu đồ Chart.js
    │   ├── user.html                        # Quản lý user (bảng + phân trang)
    │   └── article.html                     # Quản lý bài viết (modal + rich editor)
    └── testAdmin/                           # Bản prototype UI admin
        ├── dashboard.html
        └── user.html
```

---

## ✅ Những Gì Đã Làm Được

### 1. 🔐 Hệ Thống Xác Thực (Authentication)
- **Đăng ký** (`/register`): Validate form phía client (username, email, password ≥ 6 ký tự, confirm password). Server-side kiểm tra trùng email/username.
- **Đăng nhập** (`/login`): Xác thực bằng email + password (BCrypt). Lưu `roleId` vào `HttpSession`.
- **Đăng xuất** (`/logout`): Huỷ session và redirect về trang login.
- **Toggle hiển thị mật khẩu**: Nút show/hide password trên cả 2 form.
- **Mã hoá mật khẩu**: Sử dụng `BCryptPasswordEncoder` qua service `HashPassword`.

### 2. 🧑‍💼 Trang Quản Trị Admin
- **Layout admin** với sidebar navigation (Thymeleaf fragment `th:replace`).
- **Sidebar menu** bao gồm: Dashboard, User, Plan, Subscription, Statistic, Article, Video, Grammar, Vocabulary, Dictation, Logout.
- **Highlight menu active** dựa trên biến `path`.

### 3. 📊 Dashboard
- **4 thẻ thống kê**: Total Users, New Users This Month, Total Revenue, Active Subscriptions.
- **4 thẻ phụ**: Total Vocabularies, Quizzes, Articles, Posts.
- **2 biểu đồ Chart.js**:
  - Line chart: Số user đăng ký mới trong 30 ngày.
  - Bar chart: Số lượt nâng cấp Premium trong 30 ngày.
- *(Hiện tại dữ liệu biểu đồ là static/hardcoded)*.

### 4. 👥 Quản Lý User
- **Bảng danh sách user** với các cột: ID, Name, Email, Role (badge màu), Status (toggle), Created At, Updated At, Action (Edit/Delete).
- **Phân trang (Pagination)**: Server-side pagination qua `PageRequest.of(cnt, 10)`, hiển thị tối đa 10 user/trang.
- **Filter form** (UI): Tìm theo Name, Email, Role (chưa kết nối backend).
- **Nút Export/Import/Create** (chỉ có UI).

### 5. 📝 Quản Lý Bài Viết (Article)
- **Bảng danh sách bài viết** với thumbnail, title, category, status, created date.
- **Modal Create/Update** với animation mở/đóng (scale + opacity transition).
- **Rich Text Editor** tự xây dựng: toolbar với Bold, Italic, Underline, Strikethrough, Highlight, Align, Heading (H1-H3), List, Link, Image.
- **Upload thumbnail & audio** (UI form).
- **Filter** theo Title, Category, Status (chỉ có UI).
- *(Chưa kết nối backend – dữ liệu demo hardcoded)*.

### 6. 🗄️ Thiết Kế Database (17 Entity JPA)

| Nhóm | Entity | Mô tả |
|---|---|---|
| **User** | `User`, `Role` | Quản lý tài khoản và phân quyền (ADMIN/USER) |
| **Từ vựng** | `Vocabulary`, `UserVocabulary` | Học từ vựng với Spaced Repetition (easeFactor, repetitions, reviewInterval) |
| **Ngữ pháp** | `Grammar` | Bài học ngữ pháp theo danh mục |
| **Bài viết** | `Article`, `Category` | Bài viết học tiếng Anh theo chủ đề |
| **Video** | `Video` | Video bài giảng với subtitle, duration |
| **Quiz** | `Quiz`, `Question`, `UserQuizAttempt`, `UserAnswer` | Hệ thống kiểm tra trắc nghiệm, lưu kết quả từng lần làm |
| **Chính tả** | `DictationTopics`, `DictationSentences` | Luyện nghe chính tả với audio |
| **Thanh toán** | `Plan`, `Subscription` | Gói trả phí Premium với transaction tracking |
| **Thông báo** | `Notification` | Thông báo hệ thống (actor → recipient, với isRead) |

### 7. 🔒 Cấu Hình Bảo Mật
- Spring Security được tích hợp với `BCryptPasswordEncoder`.
- CSRF tắt, tất cả request được `permitAll()` (đang phát triển, chưa phân quyền theo role).

### 8. 🎨 Giao Diện UI
- **Login/Register**: Card layout 2 cột (branding bên trái + form bên phải), gradient xanh, responsive.
- **Admin Panel**: Sidebar + Content layout, thẻ thống kê rounded-xl shadow, bảng dữ liệu có hover effect.
- **Prototype** (`testAdmin/`): Bản thiết kế UI tĩnh để test layout trước khi tích hợp Thymeleaf fragment.

---

## 🚧 Những Phần Chưa Hoàn Thành

| Tính năng | Trạng thái |
|---|---|
| Phân quyền Admin/User/Premium thực tế | ❌ Chưa triển khai (permitAll) |
| CRUD backend cho Article, Vocabulary, Grammar, Video, Dictation, Quiz | ❌ Chưa có Controller/Service/Repository |
| Kết nối filter/search phía admin với backend | ❌ Chỉ có UI |
| Hệ thống Spaced Repetition (SM-2) cho UserVocabulary | ❌ Chỉ có entity |
| Thanh toán / Subscription management | ❌ Chỉ có entity |
| Notification system | ❌ Chỉ có entity |
| Quiz system (làm bài, chấm điểm) | ❌ Chỉ có entity |
| Dashboard lấy dữ liệu động từ DB | ❌ Đang hardcode |
| Export/Import user | ❌ Chỉ có UI button |
| Trang học cho user (student-facing) | ❌ Chưa có |
| Responsive cho mobile | ❌ Chưa tối ưu |
| Forgot password | ❌ Chưa triển khai |

---

## 🚀 Hướng Dẫn Chạy

### Yêu cầu
- **Java 21+**
- **MySQL 8+**
- **Maven 3.8+**

### Các bước

1. **Tạo database MySQL:**
   ```sql
   CREATE DATABASE ttcs;
   ```

2. **Cấu hình kết nối** trong `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ttcs
   spring.datasource.username=root
   spring.datasource.password=<your_password>
   ```

3. **Seed dữ liệu Role** – bỏ comment trong `DataIn.java`:
   ```java
   rre.save(new Role("ADMIN", "Admin"));
   rre.save(new Role("USER", "User"));
   ```

4. **Chạy ứng dụng:**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Truy cập:**
   - Đăng nhập: [http://localhost:8080/login](http://localhost:8080/login)
   - Đăng ký: [http://localhost:8080/register](http://localhost:8080/register)
   - Admin Dashboard: [http://localhost:8080/admin/dashboard](http://localhost:8080/admin/dashboard)

---

## 👥 Nhóm 5 – TTCS

**Tên project:** `webstudyenglish`  
**Group ID:** `com.ttcsn5`  
**Version:** `0.0.1-SNAPSHOT`
