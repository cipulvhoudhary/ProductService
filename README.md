# ProductService

Product catalog microservice for the Flopkart platform. Manages products and categories in MySQL, validates sessions via **UserAuthService**, and exposes a REST API for CRUD operations.

| Property | Value |
|----------|-------|
| **Base URL** | `http://localhost:8081` |
| **Port** | `8081` |
| **Framework** | Spring Boot 4.1.0 |
| **Java** | 24 |
| **Database** | MySQL |
| **Auth dependency** | UserAuthService (`http://localhost:8080`) |

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Running the Service](#running-the-service)
- [Authentication](#authentication)
- [API Reference](#api-reference)
- [End-to-End Flow](#end-to-end-flow)
- [Data Model](#data-model)
- [Service Implementations](#service-implementations)
- [Error Handling](#error-handling)
- [Project Structure](#project-structure)
- [Roadmap](#roadmap)

---

## Overview

ProductService is a standalone REST API responsible for product lifecycle management. All `/products/**` endpoints require a valid session token issued by UserAuthService.

The service persists products and categories in MySQL using JPA. Categories are linked to products via a many-to-one relationship. When creating a product, the service looks up the category by title and creates it if it does not exist.

---

## Features

| Feature | Description |
|---------|-------------|
| **Product CRUD** | Create, read, list, and delete products |
| **Category management** | Auto-create categories on product creation; unique category titles |
| **Centralized authentication** | `ProductAuthenticationInterceptor` guards all `/products/**` routes |
| **Inter-service auth** | Token validation delegated to UserAuthService via `RestTemplate` |
| **Dual service strategy** | `selfProductService` (MySQL) and `fakeStoreProductService` (external API) |
| **JSON safety** | `@JsonIgnore` on `Category.products` prevents bidirectional serialization loops |
| **Input validation** | `ProductValidator` enforces product and category presence on create |
| **Global exception handling** | `@ControllerAdvice` maps domain exceptions to HTTP responses |
| **Native SQL queries** | Demo repository methods for raw SQL learning (`nativeQuery = true`) |

---

## Architecture

```
┌─────────────┐   tokenValue header    ┌──────────────────────────────────────┐
│   Client    │ ─────────────────────► │     ProductAuthenticationInterceptor │
└─────────────┘                          └──────────────────┬───────────────────┘
                                                           │ validate token
                                                           ▼
                                                ┌──────────────────────┐
                                                │  UserAuthService     │
                                                │  (RestTemplate)      │
                                                └──────────┬───────────┘
                                                           │ POST /auth/validate
                                                           ▼
                                                ┌──────────────────────┐
                                                │  UserAuthService     │
                                                │  :8080               │
                                                └──────────────────────┘

                                                           │ token OK
                                                           ▼
┌─────────────┐                          ┌──────────────────────────────────────┐
│   Client    │ ── GET/POST/DELETE ────► │         ProductController            │
└─────────────┘                          └──────────────────┬───────────────────┘
                                                           │
                                                           ▼
                                                ┌──────────────────────┐
                                                │  SelfProductService  │
                                                └──────────┬───────────┘
                                                           │
                              ┌────────────────────────────┼────────────────────────┐
                              ▼                            ▼                        ▼
                     ┌─────────────────┐          ┌─────────────────┐      ┌─────────────────┐
                     │ProductRepository│          │CategoryRepository│      │ ProductValidator│
                     └────────┬────────┘          └────────┬────────┘      └─────────────────┘
                              │                            │
                              ▼                            ▼
                     ┌─────────────────────────────────────────────┐
                     │                   MySQL                     │
                     │              products · categories           │
                     └─────────────────────────────────────────────┘
```

**Request path:** `Interceptor → Controller → SelfProductService → Repository → MySQL`

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Runtime | Java 24 |
| Framework | Spring Boot 4.1.0 |
| Web | Spring Web MVC, `HandlerInterceptor` |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL (`mysql-connector-j`) |
| HTTP client | `RestTemplate` (UserAuthService integration) |
| JSON | Jackson (`@JsonIgnore` for entity graphs) |
| Utilities | Lombok, Apache Commons Lang3 |
| Build | Maven |

---

## Prerequisites

- **JDK 24**
- **Maven 3.9+** (or use included `./mvnw`)
- **MySQL 8.x** running locally
- **UserAuthService** running on port `8080` (required for all product API calls)

---

## Database Setup

1. Create the database:

```sql
CREATE DATABASE productservice;
```

2. Create a dedicated user (optional):

```sql
CREATE USER 'productserviceuser'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON productservice.* TO 'productserviceuser'@'localhost';
FLUSH PRIVILEGES;
```

3. Update `src/main/resources/application.properties` with your credentials.

Tables (`products`, `categories`) are created automatically via Hibernate `ddl-auto=update` on first startup.

> SQL migration files exist under `src/main/resources/db/migration/` for reference. Flyway is not configured; schema is managed by Hibernate in development.

---

## Configuration

`src/main/resources/application.properties`:

```properties
spring.application.name=ProductService

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/productservice
spring.datasource.username=productserviceuser
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true

server.port=8081
```

| Property | Description |
|----------|-------------|
| `server.port` | HTTP port (default `8081`) |
| `spring.datasource.url` | MySQL JDBC URL |
| `spring.jpa.hibernate.ddl-auto` | Schema strategy (`update` for dev) |
| `spring.jpa.show-sql` | Log SQL statements (disable in production) |

**UserAuthService URL** is currently hardcoded in `api/UserAuthService.java` as `http://localhost:8080/auth/validate`. Externalize via `application.properties` for production deployments.

---

## Running the Service

1. Start **UserAuthService** on port `8080`
2. Start **ProductService**:

```bash
./mvnw spring-boot:run
```

Or build and run the JAR:

```bash
./mvnw clean package -DskipTests
java -jar target/ProductService-0.0.1-SNAPSHOT.jar
```

---

## Authentication

All endpoints under `/products/**` are protected by `ProductAuthenticationInterceptor`.

### How it works

1. Client sends `tokenValue` request header on every product API call
2. Interceptor calls `POST http://localhost:8080/auth/validate` with the same header
3. UserAuthService returns `UserDto` if token is valid and not expired
4. Authenticated user is stored on the request as `authenticatedUser` (available for future RBAC)
5. Request proceeds to `ProductController`; missing or invalid token returns `404` with error body

### Obtaining a token

```bash
# Login via UserAuthService
curl -X POST "http://localhost:8080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"yourpassword"}'
```

Response is a plain-text 120-character token. Use it as the `tokenValue` header on all product requests.

---

## API Reference

**Base path:** `/products`

**Required header (all endpoints):**

| Header | Description |
|--------|-------------|
| `tokenValue` | Session token from UserAuthService login |

**Request bodies** must use `Content-Type: application/json`.

---

### 1. Get Product by ID

| | |
|---|---|
| **Method** | `GET` |
| **Path** | `/products/{id}` |
| **Path variable** | `id` — product ID |

**Success — `200 OK`:**

```json
{
  "id": 1,
  "title": "iPhone 15",
  "price": 79999.0,
  "description": "Latest iPhone",
  "imageUrl": "https://example.com/iphone.jpg",
  "createdAt": null,
  "lastModifiedAt": null,
  "category": {
    "id": 1,
    "title": "Electronics",
    "createdAt": null,
    "lastModifiedAt": null
  }
}
```

**Example:**

```bash
curl "http://localhost:8081/products/1" \
  -H "tokenValue: YOUR_TOKEN_HERE"
```

---

### 2. Get All Products

| | |
|---|---|
| **Method** | `GET` |
| **Path** | `/products/` |

**Success — `200 OK`:**

```json
[
  {
    "id": 1,
    "title": "iPhone 15",
    "price": 79999.0,
    "description": "Latest iPhone",
    "imageUrl": "https://example.com/iphone.jpg",
    "category": {
      "id": 1,
      "title": "Electronics"
    }
  }
]
```

**Example:**

```bash
curl "http://localhost:8081/products/" \
  -H "tokenValue: YOUR_TOKEN_HERE"
```

---

### 3. Create Product

| | |
|---|---|
| **Method** | `POST` |
| **Path** | `/products` |

Creates a product. If the category title does not exist, a new category is created automatically.

**Request body:**

```json
{
  "title": "iPhone 15",
  "price": 79999.0,
  "description": "Latest iPhone",
  "imageUrl": "https://example.com/iphone.jpg",
  "category": {
    "title": "Electronics"
  }
}
```

> Only `category.title` is required in the nested category object. Category is resolved by title, not ID.

**Success — `200 OK`:** Returns the saved `Product` with generated `id` and linked category.

**Example:**

```bash
curl -X POST "http://localhost:8081/products" \
  -H "tokenValue: YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "iPhone 15",
    "price": 79999.0,
    "description": "Latest iPhone",
    "imageUrl": "https://example.com/iphone.jpg",
    "category": { "title": "Electronics" }
  }'
```

---

### 4. Delete Product

| | |
|---|---|
| **Method** | `DELETE` |
| **Path** | `/products/{id}` |
| **Path variable** | `id` — product ID |

**Success — `200 OK`:** Empty body.

**Example:**

```bash
curl -X DELETE "http://localhost:8081/products/1" \
  -H "tokenValue: YOUR_TOKEN_HERE"
```

---

## End-to-End Flow

### Complete workflow (signup → product access)

```bash
# 1. Sign up (UserAuthService)
curl -X POST "http://localhost:8080/auth/signup" \
  -H "Content-Type: application/json" \
  -d '{"name":"Vipul","email":"user@example.com","phoneNumber":"9999999999","password":"secret"}'

# 2. Login and capture token
TOKEN=$(curl -s -X POST "http://localhost:8080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"secret"}')

# 3. Create a product
curl -X POST "http://localhost:8081/products" \
  -H "tokenValue: $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"MacBook Pro","price":149999,"description":"M3 chip","imageUrl":"https://example.com/mac.jpg","category":{"title":"Electronics"}}'

# 4. List all products
curl "http://localhost:8081/products/" -H "tokenValue: $TOKEN"

# 5. Get product by ID
curl "http://localhost:8081/products/1" -H "tokenValue: $TOKEN"

# 6. Delete product
curl -X DELETE "http://localhost:8081/products/1" -H "tokenValue: $TOKEN"
```

### Authentication failure flow

```
Client → GET /products/1 (no tokenValue header)
  → ProductAuthenticationInterceptor
  → UnauthorisedException("Invalid token")
  → 404 { "message": "Invalid token" }
```

---

## Data Model

### Product (`products` table)

| Field | Type | Notes |
|-------|------|-------|
| `id` | `Long` | Auto-generated |
| `title` | `String` | Product name |
| `price` | `Double` | Price |
| `description` | `String` | Product description |
| `imageUrl` | `String` | Image URL |
| `category` | `Category` | `@ManyToOne` relationship |
| `createdAt`, `lastModifiedAt` | `Date` | Audit fields from `BaseModel` |

### Category (`categories` table)

| Field | Type | Notes |
|-------|------|-------|
| `id` | `Long` | Auto-generated |
| `title` | `String` | Unique, not null |
| `products` | `List<Product>` | `@OneToMany(mappedBy = "category")`; ignored in JSON (`@JsonIgnore`) |

### Relationship

```
Category 1 ──── * Product
```

`Category.products` is excluded from API responses to prevent infinite JSON nesting (`Product → Category → products → Product → ...`).

---

## Service Implementations

ProductService uses the **Strategy pattern** with two beans implementing the same interface:

| Bean name | Class | Data source | Active |
|-----------|-------|-------------|--------|
| `selfProductService` | `SelfProductService` | MySQL (JPA) | **Yes** (default via `@Qualifier`) |
| `fakeStoreProductService` | `FakeStoreProductService` | [fakestoreapi.com](https://fakestoreapi.com) | No (swap `@Qualifier` to use) |

### SelfProductService (default)

- Full CRUD against local MySQL
- Category get-or-create by title on product creation
- `ProductValidator` runs before save

### FakeStoreProductService (alternate)

- Read-only integration with external Fake Store API
- `createProduct` and `deleteProduct` are stubs
- Useful for prototyping without a database

---

## Error Handling

Global handler: `ProductServiceControllerAdvice` (`@ControllerAdvice`)

| Exception | HTTP Status | Scenario |
|-----------|-------------|----------|
| `ProductNotFoundException` | `404 Not Found` | Product ID does not exist |
| `UnauthorisedException` | `404 Not Found` | Missing, invalid, or expired token |
| `CategoryNotFoundException` | `404 Not Found` | Category not found (reserved) |
| `RuntimeException` (catch-all) | `404 Not Found` | Unhandled runtime errors |

### Product not found response

```json
{
  "productId": 99,
  "message": "Product not found",
  "resolutionDetails": "Check product id"
}
```

### Unauthorized response

```json
{
  "message": "Invalid token"
}
```

---

## Project Structure

```
src/main/java/com/flopkart/productservice/
├── ProductServiceApplication.java       # Entry point
├── api/
│   └── UserAuthService.java             # RestTemplate client for token validation
├── config/
│   ├── ApplicationConfig.java           # RestTemplate bean
│   └── WebMvcConfig.java                # Registers auth interceptor
├── controller/
│   ├── ProductController.java           # REST endpoints
│   └── SampleController.java            # Dev sample (/sample/message)
├── controllerAdvice/
│   └── ProductServiceControllerAdvice.java
├── dtos/
│   ├── ErrorResponse.java
│   ├── ExceptionDto.java
│   ├── FakeStoreProductDto.java
│   ├── ProductNotFoundExceptionDto.java
│   └── UserDto.java
├── exceptions/
│   ├── CategoryNotFoundException.java
│   ├── ProductNotFoundException.java
│   └── UnauthorisedException.java
├── models/
│   ├── BaseModel.java
│   ├── Category.java
│   └── Product.java
├── repositories/
│   ├── CategoryRepository.java
│   └── ProductRepository.java
├── security/
│   └── ProductAuthenticationInterceptor.java  # Centralized auth
├── service/
│   ├── FakeStoreProductService.java
│   ├── ProductService.java              # Interface
│   └── SelfProductService.java          # Default implementation
├── validations/
│   └── ProductValidator.java
└── dbInheritenceDemo/                   # JPA inheritance learning examples
```

---

## Roadmap

| Item | Status |
|------|--------|
| Externalize UserAuthService URL via `application.properties` | Planned |
| Spring Security integration | Planned |
| Role-based access (use `authenticatedUser` from interceptor) | Planned |
| Input validation (`@Valid`, price/title constraints) | Planned |
| Flyway migrations (replace `ddl-auto=update`) | Planned |
| Shared DTO library with UserAuthService | Planned |
| `Authorization: Bearer` header support | Planned |
| Remove catch-all `RuntimeException` handler | Planned |
| Move `dbInheritenceDemo` out of main classpath | Planned |

---

## Related Services

| Service | Port | Role |
|---------|------|------|
| **UserAuthService** | `8080` | Signup, login, token validation |
| **ProductService** | `8081` | Product catalog CRUD |

See [UserAuthService README](../UserAuthService/README.md) for authentication API documentation.

---

## License

Internal Flopkart project — license TBD.
