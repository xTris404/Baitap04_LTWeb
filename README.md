# Bài tập: Đăng nhập (Cookie + Session), Đăng ký & Quản lý Profile User — công nghệ JPA API

Project minh họa kiến trúc **3 tầng (Controller → Service → DAO)** sử dụng **JPA 3.0 / Hibernate 7** kết hợp với **Jakarta Servlet**, **Sitemesh** và **SQL Server**.

Project hiện hỗ trợ các chức năng chính:

* **Đăng nhập** bằng Session.
* Chức năng **"Nhớ tôi"** bằng Cookie.
* **Đăng ký tài khoản**, kiểm tra trùng username/email.
* **Quản lý Profile User**.
* Cập nhật **Fullname**.
* Cập nhật **Phone**.
* Cập nhật **Avatar/Image**.
* Upload hình ảnh bằng **Multipart**.
* Sử dụng **JPA / EntityManager** để cập nhật dữ liệu.
* Sử dụng **Sitemesh** để quản lý layout và giao diện dùng chung.
* Phân quyền và điều hướng người dùng theo **Role**.
* Sử dụng Filter để xử lý Encoding UTF-8 cho toàn bộ request.

---

## 1. Công nghệ sử dụng

| Công nghệ           | Mục đích                             |
| ------------------- | ------------------------------------ |
| Java JDK 17+        | Ngôn ngữ lập trình                   |
| Jakarta Servlet     | Xây dựng Controller                  |
| JPA 3.0             | ORM / truy xuất cơ sở dữ liệu        |
| Hibernate 7         | JPA Provider                         |
| SQL Server 2019+    | Hệ quản trị cơ sở dữ liệu            |
| Maven               | Quản lý dependency và build project  |
| JSP                 | Xây dựng giao diện                   |
| JSTL                | Xử lý logic trên JSP                 |
| Sitemesh            | Quản lý Layout / Decorator giao diện |
| Multipart           | Upload file hình ảnh                 |
| Apache Tomcat 10.1+ | Web Server / Servlet Container       |

> **Lưu ý:** Project sử dụng namespace `jakarta.*`, vì vậy cần sử dụng **Tomcat 10.1.x trở lên**. Tomcat 9 sử dụng `javax.*` và không tương thích trực tiếp với project này.

---

# 2. Kiến trúc hệ thống

Project được xây dựng theo mô hình **3 tầng**:

```text
┌──────────────────────────────────────────────┐
│                  VIEW                        │
│           JSP + Sitemesh Layout              │
│                                              │
│ login.jsp | register.jsp | profile.jsp       │
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│                CONTROLLER                    │
│             Jakarta Servlet                 │
│                                              │
│ LoginController                              │
│ RegisterController                           │
│ WaitingController                            │
│ LogoutController                             │
│ ProfileController                            │
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│                  SERVICE                     │
│                                              │
│ IUserService                                 │
│ UserServiceImpl                              │
│                                              │
│ Login / Register / Profile / Update          │
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│                    DAO                       │
│                                              │
│ IUserDao                                     │
│ UserDaoImpl                                  │
│                                              │
│ EntityManager + JPQL + Transaction            │
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│                 DATABASE                     │
│                  SQL Server                  │
│                                              │
│                   Users                      │
└──────────────────────────────────────────────┘
```

### Nguyên tắc phân tầng

**Controller**

* Tiếp nhận HTTP Request.
* Đọc dữ liệu từ Form.
* Xử lý Session/Cookie.
* Xử lý Multipart Upload.
* Gọi Service.
* Điều hướng đến JSP hoặc Servlet khác.

**Service**

* Xử lý nghiệp vụ.
* Kiểm tra username/email.
* Xác thực đăng nhập.
* Đăng ký tài khoản.
* Cập nhật thông tin Profile.
* Gọi DAO để thao tác dữ liệu.

**DAO**

* Làm việc trực tiếp với database.
* Sử dụng `EntityManager`.
* Thực hiện JPQL.
* Quản lý Transaction khi thêm/cập nhật dữ liệu.

---

# 3. Cấu trúc project

Cấu trúc project hiện tại:

```text
src/
├── main/
│   ├── java/
│   │   └── vn/iotstar/
│   │       │
│   │       ├── entity/
│   │       │   └── User.java
│   │       │
│   │       ├── config/
│   │       │   └── JpaConfig.java
│   │       │
│   │       ├── dao/
│   │       │   ├── IUserDao.java
│   │       │   └── UserDaoImpl.java
│   │       │
│   │       ├── service/
│   │       │   ├── IUserService.java
│   │       │   └── UserServiceImpl.java
│   │       │
│   │       ├── controller/
│   │       │   ├── LoginController.java
│   │       │   ├── RegisterController.java
│   │       │   ├── WaitingController.java
│   │       │   ├── LogoutController.java
│   │       │   └── ProfileController.java
│   │       │
│   │       ├── filter/
│   │       │   └── EncodingFilter.java
│   │       │
│   │       └── constants/
│   │           └── Constant.java
│   │
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml
│   │
│   └── webapp/
│       │
│       ├── views/
│       │   ├── login.jsp
│       │   ├── register.jsp
│       │   ├── home.jsp
│       │   └── profile.jsp
│       │
│       ├── decorators/
│       │   └── ...
│       │
│       ├── assets/
│       │   ├── css/
│       │   ├── js/
│       │   └── images/
│       │
│       └── WEB-INF/
│           └── ...
│
├── sql/
│   └── create_database.sql
│
└── pom.xml
```

> Tên file/thư mục có thể thay đổi tùy theo cấu trúc thực tế của project. Thành phần quan trọng là Controller → Service → DAO vẫn được tách biệt rõ ràng.

---

# 4. Cấu hình môi trường

## 4.1. JDK

Khuyến nghị sử dụng:

```text
JDK 17 trở lên
```

Kiểm tra:

```powershell
java -version
```

và:

```powershell
javac -version
```

---

## 4.2. Maven

Kiểm tra Maven:

```powershell
mvn -version
```

Maven được sử dụng để quản lý các dependency của project.

Sau khi clone project từ GitHub:

```powershell
mvn clean install
```

Maven sẽ tự động tải các thư viện cần thiết.

---

## 4.3. Apache Tomcat

Project sử dụng:

```text
Apache Tomcat 10.1.x trở lên
```

Do project sử dụng:

```java
jakarta.servlet.*
jakarta.persistence.*
```

không sử dụng:

```java
javax.servlet.*
javax.persistence.*
```

Sau khi cấu hình Tomcat trong Eclipse/IntelliJ, chạy project bằng:

```text
Run on Server
```

Sau đó truy cập:

```text
http://localhost:8080/<context-path>/
```

---

# 5. Cấu hình SQL Server

Project sử dụng SQL Server làm hệ quản trị cơ sở dữ liệu.

Database mặc định:

```text
webst2
```

Ví dụ cấu hình:

```xml
<property name="jakarta.persistence.jdbc.url"
    value="jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;databaseName=webst2" />

<property name="jakarta.persistence.jdbc.user"
    value="sa" />

<property name="jakarta.persistence.jdbc.password"
    value="MẬT_KHẨU_CỦA_BẠN" />
```

Cần thay đổi username/password cho phù hợp với SQL Server trên máy.

---

# 6. Entity User

Entity `User.java` đại diện cho bảng:

```text
Users
```

Entity chứa các thông tin phục vụ đăng nhập, phân quyền và Profile User.

Các thuộc tính chính bao gồm:

```text
id
userName
password
email
fullName
phone
images
roleid
```

Trong đó:

| Thuộc tính | Chức năng                       |
| ---------- | ------------------------------- |
| `id`       | Khóa chính                      |
| `userName` | Tên đăng nhập                   |
| `password` | Mật khẩu                        |
| `email`    | Email                           |
| `fullName` | Họ và tên                       |
| `phone`    | Số điện thoại                   |
| `images`   | Đường dẫn/tên file ảnh đại diện |
| `roleid`   | Phân quyền người dùng           |

---

# 7. Chức năng Đăng nhập

## 7.1. GET `/login`

Khi truy cập trang Login:

1. Kiểm tra Session.

2. Nếu Session đã có `account`:

   ```java
   session.getAttribute("account")
   ```

   → chuyển đến `/waiting`.

3. Nếu chưa có Session → kiểm tra Cookie `username`.

4. Nếu Cookie tồn tại:

   * Lấy username.
   * Tìm User trong database bằng JPA.
   * Nếu User tồn tại → tạo lại Session.
   * Chuyển đến `/waiting`.

5. Nếu không có Cookie hợp lệ → hiển thị `login.jsp`.

---

## 7.2. POST `/login`

Người dùng nhập:

```text
Username
Password
Remember me
```

Controller gọi:

```text
LoginController
      ↓
UserServiceImpl.login()
      ↓
UserDaoImpl.findByUsername()
      ↓
EntityManager + JPQL
```

JPQL:

```sql
SELECT u
FROM User u
WHERE u.userName = :username
```

Nếu thông tin đăng nhập chính xác:

```java
session.setAttribute("account", user);
```

Nếu chọn **Nhớ tôi**, tạo Cookie:

```text
username
```

với thời gian sống:

```text
7 ngày
```

---

# 8. Chức năng Đăng ký

URL:

```text
/register
```

### GET `/register`

Hiển thị:

```text
register.jsp
```

Nếu người dùng đã đăng nhập → chuyển đến `/waiting`.

### POST `/register`

Các bước xử lý:

```text
Nhập thông tin
      ↓
Validate dữ liệu
      ↓
Kiểm tra password/repassword
      ↓
Kiểm tra username tồn tại
      ↓
Kiểm tra email tồn tại
      ↓
Tạo User
      ↓
EntityManager.persist()
      ↓
Transaction
      ↓
Đăng ký thành công
      ↓
Chuyển về /login
```

Kiểm tra username:

```sql
SELECT COUNT(u)
FROM User u
WHERE u.userName = :username
```

Kiểm tra email tương tự.

---

# 9. Chức năng User Profile

Project được mở rộng thêm chức năng **quản lý thông tin cá nhân của User**.

Người dùng sau khi đăng nhập có thể truy cập:

```text
/profile
```

hoặc URL tương ứng được cấu hình trong `ProfileController`.

Profile cho phép người dùng cập nhật:

* Họ và tên (**Fullname**).
* Số điện thoại (**Phone**).
* Ảnh đại diện (**Images / Avatar**).

---

# 10. Cập nhật Fullname và Phone

Khi người dùng gửi Form Profile:

```text
profile.jsp
      ↓
ProfileController
      ↓
UserServiceImpl
      ↓
UserDaoImpl
      ↓
EntityManager
      ↓
SQL Server
```

Service tiếp nhận thông tin:

```text
User ID
Fullname
Phone
```

Sau đó DAO thực hiện cập nhật Entity.

Việc cập nhật dữ liệu được thực hiện thông qua:

```java
EntityManager
```

và Transaction của JPA.

---

# 11. Upload Image bằng Multipart

Một điểm mở rộng của project là chức năng **upload ảnh đại diện**.

Form Profile cần hỗ trợ:

```html
enctype="multipart/form-data"
```

Ví dụ:

```html
<form action="profile" method="post"
      enctype="multipart/form-data">
```

Input:

```html
<input type="file" name="image">
```

Khi người dùng chọn ảnh:

```text
Browser
   │
   │ multipart/form-data
   ▼
ProfileController
   │
   ├── Fullname
   ├── Phone
   └── Image file
        │
        ▼
Upload / lưu file
        │
        ▼
Cập nhật tên/đường dẫn ảnh
        │
        ▼
User Entity
        │
        ▼
JPA / EntityManager
        │
        ▼
SQL Server
```

Database không nhất thiết lưu trực tiếp dữ liệu binary của ảnh. Thay vào đó, trường `images` có thể lưu **tên file hoặc đường dẫn tới file ảnh**.

Ví dụ:

```text
images/avatar_001.jpg
```

Sau khi upload thành công, thông tin ảnh được cập nhật vào User.

---

# 12. Sử dụng Sitemesh quản lý giao diện

Project sử dụng **Sitemesh** để quản lý Layout giao diện.

Thay vì mỗi JSP phải viết lại:

```text
Header
Navbar
Sidebar
Footer
CSS
JavaScript
```

Sitemesh cho phép xây dựng một Layout/Decorator dùng chung.

Mô hình:

```text
              Sitemesh
                  │
                  ▼
        ┌───────────────────┐
        │      Layout       │
        │                   │
        │ Header / Navbar   │
        │                   │
        │     Content       │
        │                   │
        │ Footer            │
        └─────────┬─────────┘
                  │
        ┌─────────┼──────────┐
        ▼         ▼          ▼
      Login    Register    Profile
       JSP       JSP         JSP
```

Nhờ đó các trang:

```text
login.jsp
register.jsp
home.jsp
profile.jsp
```

có thể sử dụng chung giao diện.

---

# 13. Vai trò của Sitemesh

Sitemesh chịu trách nhiệm chính cho phần **trang trí và bố cục giao diện**, không xử lý nghiệp vụ.

Ví dụ:

```text
Controller
    ↓
Service
    ↓
DAO
    ↓
Database
```

chịu trách nhiệm về dữ liệu và nghiệp vụ.

Trong khi:

```text
Sitemesh
    ↓
Layout
    ↓
JSP Content
```

chịu trách nhiệm về giao diện.

Điều này giúp giảm việc lặp lại HTML giữa các JSP.

---

# 14. Waiting và phân quyền

Sau khi đăng nhập thành công:

```text
/login
   ↓
Session
   ↓
/waiting
```

`WaitingController` đọc:

```java
User user = session.getAttribute("account");
```

Sau đó kiểm tra:

```text
roleid
```

và điều hướng người dùng đến giao diện tương ứng.

Ví dụ:

```text
roleid = admin
       ↓
Admin page

roleid = manager
       ↓
Manager page

roleid = user
       ↓
User/Home page
```

---

# 15. Logout

URL:

```text
/logout
```

Khi đăng xuất:

### Xóa Session

```java
session.invalidate();
```

### Xóa Cookie

Cookie `username` được tạo lại với:

```java
cookie.setMaxAge(0);
```

Sau đó:

```java
response.addCookie(cookie);
```

Kết quả:

```text
Session bị hủy
      +
Cookie Remember Me bị xóa
      ↓
Người dùng phải đăng nhập lại
```

---

# 16. Encoding Filter

Project sử dụng:

```text
EncodingFilter
```

để đảm bảo dữ liệu tiếng Việt được xử lý bằng UTF-8.

Filter áp dụng cho các request:

```text
Request
   ↓
EncodingFilter
   ↓
Controller
   ↓
Service
   ↓
DAO
```

Giúp tránh tình trạng lỗi font khi nhập:

```text
Nguyễn Văn A
Đặng Xuân Trí
Số điện thoại
Thông tin Profile
```

---

# 17. Luồng tổng thể của hệ thống

```text
                     ┌───────────────┐
                     │    Browser    │
                     └───────┬───────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ EncodingFilter  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   Controller    │
                    │                 │
                    │ Login           │
                    │ Register        │
                    │ Profile         │
                    │ Waiting         │
                    │ Logout          │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │     Service     │
                    │                 │
                    │ Login           │
                    │ Register        │
                    │ Update Profile  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │       DAO       │
                    │                 │
                    │ EntityManager   │
                    │ JPQL            │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   SQL Server    │
                    │                 │
                    │     Users       │
                    └─────────────────┘


        ┌─────────────────────────────────┐
        │             Sitemesh             │
        │                                  │
        │  Header + Navbar + Content +     │
        │  Footer / Layout                 │
        └─────────────────────────────────┘
```

---

# 18. Kiểm thử chức năng

## 18.1. Kiểm thử đăng ký

Truy cập:

```text
/register
```

Tạo tài khoản mới.

Kiểm tra:

* Không được để trống.
* Password và Re-password phải giống nhau.
* Username không được trùng.
* Email không được trùng.
* User mới được lưu vào database.

---

## 18.2. Kiểm thử Login

Đăng nhập bằng:

```text
Username
Password
```

Nếu đúng:

```text
/login
   ↓
Session
   ↓
/waiting
```

Nếu sai:

```text
login.jsp
+
Thông báo lỗi
```

---

## 18.3. Kiểm thử Remember Me

### Không chọn "Nhớ tôi"

Đăng nhập:

```text
Username + Password
```

Session được tạo.

Sau khi Session hết hạn hoặc bị xóa:

```text
/login
```

→ yêu cầu đăng nhập lại.

### Có chọn "Nhớ tôi"

Đăng nhập:

```text
Username + Password + Remember Me
```

Cookie:

```text
username
```

được tạo.

Sau khi Session bị xóa nhưng Cookie vẫn còn:

```text
/login
   ↓
Cookie username
   ↓
Database
   ↓
Tạo Session mới
   ↓
/waiting
```

---

# 19. Kiểm thử Profile

Sau khi đăng nhập:

```text
/profile
```

Kiểm tra:

### Cập nhật Fullname

Thay đổi:

```text
Fullname
```

→ Save.

Kiểm tra lại database.

### Cập nhật Phone

Nhập:

```text
Phone
```

→ Save.

Kiểm tra lại database.

### Upload Avatar

Chọn:

```text
avatar.jpg
```

→ Upload.

Kiểm tra:

```text
File được lưu
       +
User.images được cập nhật
       +
Avatar hiển thị trên giao diện
```

---

# 20. Kiểm thử Sitemesh

Kiểm tra các trang:

```text
/login
/register
/home
/profile
```

Các trang phải sử dụng chung Layout.

Kiểm tra:

* Header.
* Navigation.
* Footer.
* CSS.
* Nội dung riêng của từng JSP.

Mục tiêu là đảm bảo **phần Layout được quản lý tập trung bằng Sitemesh**, thay vì lặp lại trong từng JSP.

---

# 21. Database

Database:

```text
webst2
```

Bảng chính:

```text
Users
```

Thông tin User phục vụ các chức năng:

```text
Authentication
      +
Authorization
      +
Profile
```

Các trường Profile:

```text
fullName
phone
images
```

được lưu cùng User và được cập nhật thông qua JPA.

---

# 22. Lưu ý bảo mật

Project được xây dựng chủ yếu nhằm mục đích học tập.

Hiện tại password có thể được lưu dưới dạng **plain text** để phù hợp với yêu cầu bài thực hành và dễ đối chiếu.

Trong hệ thống thực tế nên:

* Hash password bằng BCrypt/Argon2.
* Không lưu password vào Cookie.
* Chỉ lưu thông tin cần thiết trong Cookie.
* Validate file upload.
* Giới hạn kích thước file.
* Kiểm tra MIME type/extension.
* Đổi tên file upload để tránh trùng tên.
* Không cho phép upload các file thực thi.
* Cấu hình thư mục upload phù hợp.

---

# 23. Build và chạy project

Sau khi clone project:

```powershell
git clone <repository-url>
```

Di chuyển vào project:

```powershell
cd <project-folder>
```

Build:

```powershell
mvn clean install
```

Sau khi Maven build thành công:

```text
BUILD SUCCESS
```

cấu hình project với Apache Tomcat 10.1+ và chạy:

```text
Run on Server
```

Sau đó truy cập:

```text
http://localhost:8080/<context-path>/
```

---

# 24. Git Workflow

Sau khi thực hiện thay đổi trong project:

```powershell
git status
```

Thêm các thay đổi:

```powershell
git add .
```

Commit:

```powershell
git commit -m "Update user profile feature"
```

Push:

```powershell
git push
```

Ví dụ khi bổ sung chức năng upload ảnh:

```powershell
git add .
git commit -m "Add user profile image upload"
git push
```

---

# 25. Kết luận

Project đã được mở rộng từ hệ thống **Đăng nhập + Đăng ký** thành một ứng dụng Java Web có đầy đủ các thành phần cơ bản của một hệ thống quản lý người dùng.

Các chức năng chính:

```text
┌─────────────────────────────────────┐
│          USER MANAGEMENT             │
├─────────────────────────────────────┤
│                                     │
│  ✓ Login                            │
│  ✓ Session                          │
│  ✓ Remember Me / Cookie             │
│  ✓ Register                         │
│  ✓ Validate Username / Email        │
│  ✓ Role-based Navigation            │
│  ✓ Logout                           │
│  ✓ User Profile                     │
│  ✓ Update Fullname                  │
│  ✓ Update Phone                     │
│  ✓ Upload Avatar                    │
│  ✓ Multipart File Upload            │
│  ✓ JPA / EntityManager              │
│  ✓ JPQL                             │
│  ✓ SQL Server                       │
│  ✓ 3-Tier Architecture              │
│  ✓ UTF-8 Encoding Filter            │
│  ✓ Sitemesh Layout Management       │
│                                     │
└─────────────────────────────────────┘
```

Việc sử dụng **JPA** giúp tách biệt tầng DAO khỏi JDBC thuần, trong khi **Service** đảm nhiệm nghiệp vụ và **Controller** xử lý HTTP request. **Sitemesh** được sử dụng để quản lý Layout giao diện tập trung, còn **Multipart** cung cấp cơ chế upload file cho chức năng Profile.

Qua đó project thể hiện được sự kết hợp giữa:

```text
Jakarta Servlet
      +
JPA / Hibernate
      +
SQL Server
      +
JSP / JSTL
      +
Sitemesh
      +
Multipart Upload
      +
3-Tier Architecture
```

tạo thành một ứng dụng Java Web có cấu trúc rõ ràng, dễ mở rộng và phù hợp cho mục đích học tập cũng như phát triển thêm các chức năng quản lý người dùng trong tương lai.
