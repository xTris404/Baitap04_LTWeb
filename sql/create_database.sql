-- Chạy trong SQL Server Management Studio (SSMS)

CREATE DATABASE webst2;
GO

USE webst2;
GO

-- Bảng này sẽ tự động được Hibernate tạo nếu bạn để
-- hibernate.hbm2ddl.auto = update trong persistence.xml.
-- Script dưới đây chỉ để bạn tham khảo cấu trúc / tạo tay nếu muốn.

CREATE TABLE Users (
    id          INT IDENTITY(1,1) PRIMARY KEY,
    email       NVARCHAR(255)   NULL,
    username    NVARCHAR(100)   NOT NULL UNIQUE,
    fullname    NVARCHAR(255)   NULL,
    password    NVARCHAR(255)   NOT NULL,
    avatar      NVARCHAR(255)   NULL,
    roleid      INT             NOT NULL DEFAULT 3,   -- 1: admin, 2: manager, 3: user
    phone       NVARCHAR(20)    NULL,
    createddate DATE            NULL
);
GO

-- Tài khoản admin mẫu để test (mật khẩu đang lưu dạng thô theo đúng ví dụ của môn học,
-- thực tế nên băm mật khẩu bằng BCrypt trước khi lưu)
INSERT INTO Users (email, username, fullname, password, roleid, phone, createddate)
VALUES ('admin@iotstar.vn', 'admin', 'Quản trị viên', '123456', 1, '0900000000', GETDATE());
GO
