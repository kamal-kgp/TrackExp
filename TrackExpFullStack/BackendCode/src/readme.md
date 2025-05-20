Expense Tracker API
This project provides a RESTful API for an Expense Tracker application using Kotlin, Spring Boot, and PostgreSQL.
Core Features

JWT Authentication
Create, Read, Update, Delete expenses
Filter expenses by date range, category
View expenses by time frame (today, week, month)
Search expenses by title
Get expense summary

Technologies Used

Kotlin
Spring Boot 3.2.0
Spring Security with JWT
Spring Data JPA
PostgreSQL
Gradle

Getting Started
Prerequisites

JDK 17+
PostgreSQL
Gradle

Setup Database
Create a PostgreSQL database named expense_tracker
sqlCREATE DATABASE expense_tracker;
Configure Application Properties
Update the src/main/resources/application.properties file with your database credentials and JWT secret.
Build and Run
bash./gradlew bootRun
API Endpoints
Authentication
POST /api/auth/signup - Register a new user
POST /api/auth/signin - Authenticate user and get JWT token
Expenses
GET /api/expenses - Get all expenses for current user
POST /api/expenses - Create a new expense
GET /api/expenses/{id} - Get expense by ID
PUT /api/expenses/{id} - Update expense
DELETE /api/expenses/{id} - Delete expense
GET /api/expenses/filter?category=FOOD&startDate=2025-01-01&endDate=2025-01-31 - Filter expenses
GET /api/expenses/today - Get today's expenses
GET /api/expenses/week - Get this week's expenses
GET /api/expenses/month - Get this month's expenses
GET /api/expenses/summary?startDate=2025-01-01&endDate=2025-01-31 - Get expense summary
API Request & Response Examples
Authentication
Register User
Request:
jsonPOST /api/auth/signup
{
"username": "john_doe",
"email": "john@example.com",
"password": "Password123"
}
Response:
json{
"message": "User registered successfully!"
}
Login
Request:
jsonPOST /api/auth/signin
{
"username": "john_doe",
"password": "Password123"
}
Response:
json{
"token": "eyJhbGciOiJIUzI1NiJ9...",
"type": "Bearer",
"id": 1,
"username": "john_doe",
"email": "john@example.com"
}
Expenses
Create Expense
Request:
jsonPOST /api/expenses
{
"title": "Lunch",
"amount": 15.50,
"currency": "INR"
"category": "FOOD",
"date": "2025-05-16"
}
Response:
json{
"id": 1,
"title": "Lunch",
"amount": 15.50,
"currency": "INR"
"category": "FOOD",
"date": "2025-05-16",
"userId": 1
}
Get All Expenses
Request:
GET /api/expenses
Response:
json[
{
"id": 1,
"title": "Lunch",
"amount": 15.50,
"currency": "INR"
"category": "FOOD",
"date": "2025-05-16",
"userId": 1
},
{
"id": 2,
"title": "Bus Fare",
"amount": 2.50,
"currency": "INR"
"category": "TRANSPORTATION",
"date": "2025-05-16",
"userId": 1
}
]
Filter Expenses
Request:
GET /api/expenses/filter?category=FOOD&startDate=2025-05-01&endDate=2025-05-16
Response:
json[
{
"id": 1,
"title": "Lunch",
"amount": 15.50,
"currency": "INR"
"category": "FOOD",
"date": "2025-05-16",
"userId": 1
}
]
Get Expense Summary
Request:
GET /api/expenses/summary?startDate=2025-05-01&endDate=2025-05-31
Response:
json{
"total": 18.00,
"categorySummary": {
"FOOD": 15.50,
"TRANSPORTATION": 2.50
}
}
Security
All API endpoints except for /api/auth/** require authentication.
You need to include the JWT token in the Authorization header:
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...