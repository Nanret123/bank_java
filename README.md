# 💰 Bank Management System - Spring Boot API

A robust and extensible core banking backend built with **Spring Boot**, featuring user authentication, account management, transaction handling, KYC processing, and file storage with Cloudinary.

## 🚀 Features

- ✅ **JWT Authentication** & Role-Based Access Control
- 👤 **Customer & KYC Management**
- 🏦 **Account Opening** & Balance Tracking
- 💸 **Fund Transfers, Deposits & Withdrawals**
- 🧾 **Double-Entry Ledger System**
- 📦 **File Uploads via Cloudinary** (for profile pictures, KYC docs)
- 📋 **Audit Logging** via AOP
- 🔍 **Filterable & Paginated APIs**
- 📖 **Interactive Swagger API Docs**

## 🛠️ Tech Stack

- **Java 17**, **Spring Boot**
- **PostgreSQL** + **Liquibase**
- **MapStruct**, **Lombok**
- **Cloudinary API** for file storage
- **Spring Security (JWT)**
- **Spring Data JPA**
- **Swagger/OpenAPI**

## 📂 Project Structure

```bash
src/main/java/com/example/bank/
├── auth/              # Authentication and JWT handling
├── user/              # User & role management
├── customer/          # Customer management
├── kyc/               # KYC processing
├── account/           # Account creation and queries
├── transaction/       # Transfers, deposits, withdrawals
├── ledger/            # Double-entry ledger tracking
├── audit/             # AOP-based audit logs
├── file/              # File uploads via Cloudinary
└── common/            # Shared DTOs, enums, utils

```

## 🧪 Running the Project
```bash
# Clone the repo
git clone https://github.com/Nanret123/bank_java.git
cd bank-project

# Build the project
./mvnw clean install

# Run the app
./mvnw spring-boot:run
```
## 📖 API Documentation
Once the app is running, visit: **https://core-bank-management-app.onrender.com/swagger-ui.html**

To explore and test available endpoints using Swagger UI.


