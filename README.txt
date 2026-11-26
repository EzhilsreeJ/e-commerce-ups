E-COMMERCE BACKEND - JWT AUTHENTICATION
=======================================

This project contains the Phase 1-3 backend from the student guide:
- Product and category APIs
- MySQL + Spring Data JPA
- User registration and BCrypt password hashing
- Login and JWT generation
- JWT request filter and stateless Spring Security
- Postman testing up to the JWT checkpoint

TECH STACK
----------
Java 17
Spring Boot 3.5.x
Spring Security 6
Spring Data JPA / Hibernate
MySQL 8
JJWT 0.12.6

1. DATABASE SETUP
----------------
Open MySQL Workbench and run:

CREATE DATABASE ecommerce_db;

2. CONFIGURE DATABASE
---------------------
Open src/main/resources/application.properties.
Replace YOUR_MYSQL_PASSWORD with your local MySQL root password.
The JWT secret is a development-only value. Use a private environment variable
for real deployments and never commit a production secret.

3. RUN THE PROJECT
-----------------
Open this folder in STS or IntelliJ as a Maven project.
Run Maven > Update Project, then run EcommerceApplication.java.

Or from the project folder, run:

mvn spring-boot:run

4. JWT TESTING (POSTMAN)
-----------------------
Base URL: http://localhost:8080

A. Public product list
GET /api/products
Expected: 200 OK (no token)

B. Register a user
POST /api/users/register
Body > raw > JSON:
{
    "fullName": "Jane Doe",
    "email": "jane@example.com",
    "password": "secret123"
}
Expected: 201 Created. Response includes id, fullName and email only.

C. Duplicate registration
Send the same registration request again.
Expected: 400 Bad Request, error = Email is already registered.

D. Validation check
Register with password "123".
Expected: 400 Bad Request, error = Password must be at least 6 characters.

E. Login
POST /api/users/login
Body > raw > JSON:
{
    "email": "jane@example.com",
    "password": "secret123"
}
Expected: 200 OK. Copy the token from the response.

F. Wrong password
Send login with an incorrect password.
Expected: 401 Unauthorized, error = Invalid email or password.

G. Protected product creation without token
POST /api/products
Body > raw > JSON (use an existing categoryId from GET /api/categories):
{
    "name": "Test Product",
    "description": "JWT protected product",
    "price": 499.00,
    "stock": 10,
    "category": {
        "id": 1
    }
}
Expected: 401 Unauthorized.

H. Protected product creation with token
Open Authorization tab > Type: Bearer Token > paste the login token.
Send the same POST /api/products request again.
Expected: 201 Created.

I. Check password hashing in MySQL
Run:

SELECT email, password FROM users;

The password column should contain a BCrypt hash beginning with $2a$ or $2b$,
not the original password.

SECURITY RULES
---------------
Public: GET /api/products/**, GET /api/categories/**, /api/hello,
        POST /api/users/register, POST /api/users/login
Protected: all other requests, including product POST/PUT/DELETE

JWT must be sent as:
Authorization: Bearer <your-token>

If the application fails to start, check the MySQL password, database name,
Maven dependencies and the jwt.secret property. The JWT secret must be at least
32 characters for HS256.

Note: Product JSON field names must match the Product entity in this project.
If your entity uses a different category field structure, use the response from
GET /api/categories to set category.id correctly.
