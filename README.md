# Bài tập: Đăng nhập (Cookie + Session) & Đăng ký tài khoản — công nghệ JPA API

Project minh họa kiến trúc **3 tầng (Controller → Service → DAO)** dùng **JPA 3.0 / Hibernate 7**
thay cho JDBC thuần, áp dụng cho 2 chức năng:

- **Đăng nhập**: xác thực bằng Session, có tuỳ chọn "Nhớ tôi" lưu qua Cookie.
- **Đăng ký**: tạo tài khoản mới, kiểm tra trùng username/email.

```
src/main/java/vn/iotstar/
 ├── entity/User.java              Entity JPA ánh xạ bảng Users
 ├── config/JpaConfig.java         Khởi tạo EntityManagerFactory (singleton)
 ├── dao/IUserDao.java             Interface tầng DAO
 ├── dao/UserDaoImpl.java          Cài đặt DAO bằng EntityManager (JPQL)
 ├── service/IUserService.java     Interface tầng Service
 ├── service/UserServiceImpl.java  Xử lý nghiệp vụ (login/register)
 ├── controller/LoginController.java     Servlet /login  (Session + Cookie)
 ├── controller/RegisterController.java  Servlet /register
 ├── controller/WaitingController.java   Servlet /waiting (điều hướng theo role)
 ├── controller/LogoutController.java    Servlet /logout (huỷ Session + xoá Cookie)
 ├── filter/EncodingFilter.java    Filter set UTF-8 cho mọi request
 └── constants/Constant.java       Hằng số dùng chung
src/main/resources/META-INF/persistence.xml   Cấu hình JPA/Hibernate kết nối SQL Server
src/main/webapp/views/{login,register,home}.jsp
sql/create_database.sql           Script tạo database + bảng Users
```

---

## 1. Cấu hình môi trường

### 1.1. Cài đặt công cụ
| Công cụ | Phiên bản khuyến nghị | Ghi chú |
|---|---|---|
| JDK | 17+ | `java -version` để kiểm tra |
| Eclipse EE / IntelliJ IDEA | bản mới nhất | hỗ trợ Maven + Servlet |
| Apache Tomcat | 10.1.x trở lên | **bắt buộc ≥ 10** vì dùng `jakarta.*` (Jakarta EE 10), Tomcat 9 vẫn dùng `javax.*` sẽ không chạy được |
| SQL Server | 2019+ | bật TCP/IP port 1433 trong SQL Server Configuration Manager |
| Maven | đi kèm IDE hoặc cài riêng | quản lý thư viện (pom.xml) |

### 1.2. Tạo database
Mở SSMS, chạy file `sql/create_database.sql` (tạo database `webst2` + bảng `Users`,
kèm 1 tài khoản admin mẫu: `admin / 123456`).

> Nếu không muốn tạo bảng tay, để nguyên `hibernate.hbm2ddl.auto=update` trong
> `persistence.xml`, Hibernate sẽ tự tạo bảng `Users` khi ứng dụng khởi động lần đầu
> (đúng như tài liệu CRUD JPA3.0/Jakarta 6.0 đã hướng dẫn).

### 1.3. Cấu hình persistence.xml
Sửa lại 3 dòng sau trong `src/main/resources/META-INF/persistence.xml` theo máy của bạn:

```xml
<property name="jakarta.persistence.jdbc.url"
    value="jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;databaseName=webst2" />
<property name="jakarta.persistence.jdbc.user" value="sa" />
<property name="jakarta.persistence.jdbc.password" value="MẬT_KHẨU_CỦA_BẠN" />
```

### 1.4. Import project
- Eclipse: `File → Import → Existing Maven Projects` → chọn thư mục project.
- Add server: `Window → Preferences → Server → Runtime Environment` → thêm Tomcat 10.
- Add project vào server (Servers view → Add and Remove...).
- Maven sẽ tự tải các thư viện khai báo trong `pom.xml` (hibernate-core, mssql-jdbc, jstl...).
- Chạy `Run As → Run on Server`, truy cập `http://localhost:8080/jpa-login-demo/`.

---

## 2. Luồng xử lý Đăng nhập (Session + Cookie)

1. **GET /login**
   - Nếu `Session` đã có `account` → chuyển thẳng `/waiting`.
   - Ngược lại kiểm tra `Cookie "username"` (Remember me) — nếu có và username hợp lệ
     trong DB thì tạo lại `Session` rồi chuyển `/waiting`.
   - Nếu không có gì → hiển thị `login.jsp`.

2. **POST /login**
   - Lấy `username`, `password`, checkbox `remember`.
   - Gọi `UserServiceImpl.login()` → `UserDaoImpl.findByUsername()` dùng **JPQL**:
     ```java
     SELECT u FROM User u WHERE u.userName = :username
     ```
   - Nếu đúng mật khẩu → tạo `HttpSession`, lưu `session.setAttribute("account", user)`.
   - Nếu người dùng tick "Nhớ tôi" → tạo `Cookie` tên `username`, sống 7 ngày, gửi về
     trình duyệt bằng `response.addCookie(cookie)`.
   - Sai tài khoản/mật khẩu → forward lại `login.jsp` kèm thông báo lỗi.

3. **GET /waiting**: đọc `User` từ Session, điều hướng theo `roleid` (admin/manager/user).

4. **GET /logout**: `session.invalidate()` + set `Cookie` cùng tên với `maxAge=0` để xoá
   cookie remember-me phía trình duyệt.

### So sánh Cookie vs Session trong bài tập này
| | Session | Cookie |
|---|---|---|
| Lưu ở đâu | Bộ nhớ server (`HttpSession`) | Trình duyệt client |
| Dùng để | Ghi nhớ trạng thái đăng nhập trong 1 phiên làm việc | Ghi nhớ tài khoản giữa các lần truy cập khác nhau (khi trình duyệt đã đóng/mở lại) |
| Thời gian sống | Đến khi đóng trình duyệt / hết `session-timeout` | Tự chọn (ở đây 7 ngày) qua `cookie.setMaxAge()` |
| Bảo mật | An toàn hơn (dữ liệu không lộ ra client) | Cần cẩn trọng, chỉ nên lưu username, **không** lưu password |

---

## 3. Luồng xử lý Đăng ký

1. **GET /register**: nếu đã đăng nhập → chuyển `/waiting`, ngược lại hiển thị `register.jsp`.
2. **POST /register**:
   - Validate rỗng, `password` khớp `repassword`.
   - `UserServiceImpl.checkExistUsername()` / `checkExistEmail()` dùng JPQL đếm bản ghi:
     ```java
     SELECT COUNT(u) FROM User u WHERE u.userName = :username
     ```
   - Nếu hợp lệ → `UserServiceImpl.register()` tạo `User` mới, gọi
     `EntityManager.persist(user)` trong transaction (`UserDaoImpl.insert`).
   - Thành công → redirect `/login` kèm thông báo; thất bại → forward lại form kèm lỗi.

---

## 4. Kiểm thử nhanh

1. Chạy ứng dụng, vào `/register`, tạo 1 tài khoản mới.
2. Đăng nhập lại bằng tài khoản vừa tạo, **không** tick "Nhớ tôi" → đăng xuất trình duyệt
   (đóng tab) → mở lại `/login`: phải yêu cầu đăng nhập lại (không còn Session).
3. Đăng nhập lần nữa, **có** tick "Nhớ tôi" → xoá Session bằng DevTools (Application →
   Session Storage/Cookies → xoá `JSESSIONID`) nhưng giữ Cookie `username` → tải lại `/login`:
   ứng dụng phải tự động đăng nhập lại nhờ Cookie.
4. Bấm "Đăng xuất": kiểm tra Cookie `username` bị xoá, `/login` yêu cầu nhập lại từ đầu.

---

## 5. Lưu ý khi build/nộp bài

- Vì môi trường thực thi Claude không truy cập được Maven Central nên project **chưa
  được build/test tự động** ở đây — hãy mở bằng Eclipse/IntelliJ có kết nối mạng để Maven
  tải dependency và build thực tế trước khi quay video nộp bài.
- Mật khẩu trong bài đang lưu dạng chữ thường (plain text) đúng theo ví dụ gốc của giảng
  viên để dễ đối chiếu; nếu muốn nâng cao có thể băm bằng `BCryptPasswordEncoder` hoặc
  `hibernate-validator`/`jakarta.validation` để validate `@NotEmpty` như tài liệu CRUD gốc.
