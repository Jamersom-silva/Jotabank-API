🏦 Jotabank API

Jotabank is a fictional digital banking API built with Spring Boot.
It simulates real banking operations using a fictional currency called Jotacoin (JTC).

This project demonstrates authentication, account management, transfers, and transaction statements following clean architecture principles.

🚀 Features

User registration and authentication (JWT)

Automatic account creation with initial balance (1000 JTC)

Secure transfers between accounts

Transaction history (statement)

Validation rules (no negative balance, no self-transfer, etc.)

🧱 Tech Stack

Java 17

Spring Boot 3

Spring Security (JWT)

Spring Data JPA

H2 (development database)

Maven

💰 Business Rules

Every user receives 1000 JTC on registration

Transfers must be greater than zero

Transfers cannot be made to the same account

Accounts cannot have negative balance

All transfers are transactional (atomic)

📡 API Endpoints (MVP)
Authentication

POST /auth/register
POST /auth/login

Account

GET /accounts/me

Transfers

POST /transfers

Statement

GET /accounts/me/statement

🎯 Project Purpose

This project was built as a portfolio-level backend system to demonstrate:

REST API design

Authentication and authorization

Transactional consistency

Domain modeling

Clean architecture principles
