-- Tạo database (chạy trên SQL Server)
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'webst2')
BEGIN
    CREATE DATABASE webst2;
END
GO

USE webst2;
GO

-- Bảng Users (Hibernate hbm2ddl.auto=update cũng tự tạo/cập nhật)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Users')
BEGIN
    CREATE TABLE Users (
        id INT IDENTITY(1,1) PRIMARY KEY,
        email NVARCHAR(255) NULL,
        username NVARCHAR(100) NOT NULL UNIQUE,
        fullname NVARCHAR(255) NULL,
        password NVARCHAR(255) NOT NULL,
        avatar NVARCHAR(255) NULL,
        roleid INT NOT NULL,
        phone NVARCHAR(20) NULL,
        createddate DATE NULL,
        active INT NOT NULL DEFAULT 0,
        otp_code NVARCHAR(10) NULL,
        otp_expiry DATETIME NULL
    );
END
ELSE
BEGIN
    IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Users') AND name = 'active')
        ALTER TABLE Users ADD active INT NOT NULL DEFAULT 0;
    IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Users') AND name = 'otp_code')
        ALTER TABLE Users ADD otp_code NVARCHAR(10) NULL;
    IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Users') AND name = 'otp_expiry')
        ALTER TABLE Users ADD otp_expiry DATETIME NULL;
END
GO

-- Bảng categories
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'categories')
BEGIN
    CREATE TABLE categories (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        icon NVARCHAR(255) NULL,
        status INT NULL
    );
END
GO

-- Bảng products (1-n với categories)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'products')
BEGIN
    CREATE TABLE products (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        description NVARCHAR(MAX) NULL,
        price DECIMAL(18,2) NOT NULL,
        quantity INT NOT NULL,
        image NVARCHAR(255) NULL,
        status INT NULL DEFAULT 1,
        created_date DATETIME NULL,
        category_id INT NOT NULL,
        CONSTRAINT FK_products_category FOREIGN KEY (category_id) REFERENCES categories(id)
    );
END
GO
