# 🏦 Jotabank API

A fictional banking API built with **Spring Boot**, featuring **JWT authentication**, in-memory **H2 database**, and a virtual currency called **Jotacoin (JTC)**.

This project simulates a real banking backend system where users can register, authenticate, transfer virtual money, and check their transaction history.

---

# ✨ Features

* 🔐 User registration and login
* 🪪 JWT-based authentication (stateless)
* 💰 Automatic account creation with initial balance
* 🔁 Secure money transfers between accounts
* 📊 Account balance endpoint
* 📜 Transaction statement endpoint

---

# 💰 About Jotacoin (JTC)

Jotacoin is a fictional currency created to simulate financial transactions.

Every new registered user automatically receives:

```
1000.00 JTC
```

---

# 🧰 Tech Stack

* Java 17
* Spring Boot 3
* Spring Security
* JWT
* Spring Data JPA
* H2 Database (in-memory)
* Maven

---

# 🚀 Running the Project

## 1️⃣ Clone the repository

```bash
git clone https://github.com/Jamersom-silva/Jotabank-API.git
cd Jotabank-API
```

## 2️⃣ Start the application

```bash
./mvnw spring-boot:run
```

The API will be available at:

```
http://localhost:8080
```

---

# 🗄 Database

This project uses an in-memory H2 database.

H2 Console:

```
http://localhost:8080/h2-console
```

JDBC URL:

```
jdbc:h2:mem:jotabank
```

Username:

```
sa
```

Password:

```
(empty)
```

---

# 🔄 Complete API Flow (PowerShell Example)

## 1️⃣ Register a User

```powershell
$payload = @{
  name = "User One"
  email = "user1@email.com"
  password = "123456"
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "http://localhost:8080/auth/register" `
  -Method Post `
  -ContentType "application/json" `
  -Body $payload
```

---

## 2️⃣ Login and Retrieve Token

```powershell
$login = @{
  email = "user1@email.com"
  password = "123456"
} | ConvertTo-Json

$res = Invoke-RestMethod `
  -Uri "http://localhost:8080/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body $login

$token = $res.token
$headers = @{ Authorization = "Bearer $token" }
```

---

## 3️⃣ Check Account Information

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/accounts/me" `
  -Headers $headers
```

Expected response:

```json
{
  "accountNumber": "ACC-000001",
  "balance": "1000.00",
  "currency": "JTC"
}
```

---

## 4️⃣ Make a Transfer

```powershell
$transfer = @{
  toAccountNumber = "ACC-000002"
  amount = "50.00"
  description = "payment"
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "http://localhost:8080/transfers" `
  -Method Post `
  -ContentType "application/json" `
  -Headers $headers `
  -Body $transfer
```

---

## 5️⃣ View Transaction Statement

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/transactions/me" `
  -Headers $headers
```

---

# 🧱 Project Structure

```
auth/
accounts/
transactions/
transfers/
users/
common/
```

The architecture follows a domain-based organization with clear separation between:

* Controllers
* Services
* Repositories
* DTOs
* Security configuration
* Error handling

---

# 🔐 Security

* JWT authentication
* BCrypt password encryption
* Stateless API
* Protected routes
* Role-based foundation (CLIENT / ADMIN)

---

# 🏷 Versioning

The project follows semantic versioning.

Example tags:

```
v0.1.0 – Authentication
v0.3.0 – Transfers
v0.4.0 – Statement
```

---

# 🎯 Project Purpose

This project was developed as a backend portfolio application to demonstrate:

* Clean domain modeling
* Transaction management
* REST API best practices
* Secure authentication with JWT
* Layered architecture
* Professional version control workflow

---

# 👨‍💻 Author

**Jamersom Silva**
Backend Developer | Java | Spring Boot

---

⭐ If you found this project interesting, feel free to explore the code and test the API locally.
