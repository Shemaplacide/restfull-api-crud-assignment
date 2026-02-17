#  E-Commerce Product Management API

A RESTful API built with **Spring Boot** and **PostgreSQL** that allows you to manage products in an e-commerce system. This project was built as an assignment to practice and demonstrate full **CRUD operations** (Create, Read, Update, Delete) using a clean, layered Java architecture.

---

## Author

**Name:** Shema Placide  
**ID:** 26497  
**Institution:** AUCA (Adventist University of Central Africa)

---

##  What is CRUD?

CRUD stands for the four basic operations you can do with data:

| Letter | Operation | What it means |
|--------|-----------|----------------|
| **C** | Create | Add a new product to the database |
| **R** | Read | View one or all products |
| **U** | Update | Edit an existing product |
| **D** | Delete | Remove a product from the database |

This project implements all four operations through REST API endpoints.

---

##  Tech Stack

| Technology | Purpose |
|---|---|
| **Java 23** | Programming language |
| **Spring Boot 4.0.2** | Framework for building the API |
| **Spring Data JPA** | Simplifies database operations |
| **Hibernate ORM** | Maps Java objects to database tables |
| **PostgreSQL 16.11** | Database to store products |
| **Maven** | Manages project dependencies |
| **Postman** | Used to test the API endpoints |

---

##  Project Architecture

The project is organized into **3 layers**. Each layer has a specific responsibility and they work together like a chain:

```
[ Client / Postman ]
        ↓
  Controller Layer       ← Receives HTTP requests and sends responses
        ↓
  Service Layer          ← Contains the business logic and rules
        ↓
  Repository Layer       ← Talks directly to the database
        ↓
  PostgreSQL Database    ← Where all product data is stored
```

### What each layer does in this project:

- **ProductController.java** — Defines all the API endpoints (URLs). When you send a request to `/api/products`, this class receives it and decides what to do.
- **ProductService.java** — Contains the actual logic. For example, before saving a product, it checks if a product with the same ID already exists.
- **ProductRepository.java** — Uses Spring Data JPA to run database queries without writing SQL manually.
- **Product.java** — The model/entity that represents what a product looks like in the database.

---

##  Project Structure

```
src/
└── main/
    └── java/
        └── auca/ac/rw/restfullApiAssignment/
            ├── RestfullApiAssignmentApplication.java  ← Starts the application
            ├── controller/
            │   └── ProductController.java             ← API endpoints
            ├── service/
            │   └── ProductService.java                ← Business logic
            ├── repository/
            │   └── ProductRepository.java             ← Database access
            └── modal/ecommerce/
                └── Product.java                       ← Product data model
```

---

##  Product Entity (What a Product Looks Like)

Every product stored in the database has these fields:

| Field | Type | Description |
|---|---|---|
| `id` | Long | Unique identifier for each product |
| `name` | String | Name of the product |
| `description` | String | Details about the product |
| `price` | Double | Price of the product |
| `category` | String | Category it belongs to (e.g., Electronics) |
| `stockQuantity` | Integer | How many units are available |
| `brand` | String | Brand name of the product |

---

##  Database Configuration

In the `application.properties` file, the app is configured to connect to a local PostgreSQL database:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> **Note:** `ddl-auto=update` means Hibernate will automatically create or update the product table in the database when the app starts. You do not need to create the table manually.

---

##  API Endpoints (The CRUD Operations Explained)

### Base URL
```
http://localhost:8080/api/products
```

---

###  1. CREATE — Add a New Product
**Method:** `POST`  
**URL:** `/api/products`

**What happens behind the scenes:**
1. You send a JSON body with the product details
2. `ProductController` receives the request and calls `productService.addNewProduct()`
3. `ProductService` checks if a product with that ID already exists in the database
4. If it exists → returns `409 CONFLICT` with message "Product already exists"
5. If it does not exist → saves the product and returns `201 CREATED`

**Request Body:**
```json
{
    "id": 1001,
    "name": "Wireless Bluetooth Headphones",
    "description": "Premium noise-cancelling wireless headphones with 30-hour battery life",
    "price": 89.99,
    "category": "Electronics",
    "stockQuantity": 150,
    "brand": "TechSound"
}
```

**Success Response:** `201 CREATED`
```json
"Product added successfully"
```

**If product ID already exists:** `409 CONFLICT`
```json
"Product with id 1001 already exists"
```

---

###  2. READ — Get All Products
**Method:** `GET`  
**URL:** `/api/products`

**What happens behind the scenes:**
1. You send a GET request with no body
2. `ProductController` calls `productService.getAllProducts()`
3. `ProductService` calls `productRepository.findAll()` which fetches every product from the database
4. Returns the full list with `200 OK`

**Response:** `200 OK`
```json
[
    {
        "id": 1001,
        "name": "Wireless Bluetooth Headphones",
        "description": "Premium noise-cancelling wireless headphones",
        "price": 89.99,
        "category": "Electronics",
        "stockQuantity": 150,
        "brand": "TechSound"
    },
    {
        "id": 1002,
        "name": "Smart Watch",
        "description": "Fitness tracking smartwatch",
        "price": 199.99,
        "category": "Electronics",
        "stockQuantity": 75,
        "brand": "FitTech"
    }
]
```

---

###  3. READ — Get a Single Product by ID
**Method:** `GET`  
**URL:** `/api/products/{id}`  
**Example:** `/api/products/1001`

**What happens behind the scenes:**
1. You provide the product ID in the URL
2. `ProductController` calls `productService.getProductById(id)`
3. `ProductService` uses `productRepository.findById(id)` to look for the product
4. If found → returns the product with `200 OK`
5. If not found → throws a `RuntimeException` and returns `404 NOT FOUND`

**Success Response:** `200 OK`
```json
{
    "id": 1001,
    "name": "Wireless Bluetooth Headphones",
    "price": 89.99,
    "category": "Electronics",
    "stockQuantity": 150,
    "brand": "TechSound"
}
```

**If not found:** `404 NOT FOUND`
```json
"Product not found with id: 1001"
```

---

###  4. UPDATE — Edit an Existing Product
**Method:** `PUT`  
**URL:** `/api/products/{id}`  
**Example:** `/api/products/1001`

**What happens behind the scenes:**
1. You provide the ID in the URL and the new details in the request body
2. `ProductController` calls `productService.updateProduct(id, product)`
3. `ProductService` first fetches the existing product using `getProductById(id)`
4. It then replaces all the old field values (name, price, description, etc.) with the new ones
5. Saves the updated product back to the database and returns it with `200 OK`
6. If the product does not exist → returns `404 NOT FOUND`

**Request Body:**
```json
{
    "name": "Wireless Bluetooth Headphones Pro",
    "description": "Upgraded version with better sound quality",
    "price": 99.99,
    "category": "Electronics",
    "stockQuantity": 200,
    "brand": "TechSound"
}
```

**Success Response:** `200 OK` — Returns the full updated product

**If not found:** `404 NOT FOUND`
```json
"Product not found with id: 1001"
```

---

###  5. DELETE — Remove a Product
**Method:** `DELETE`  
**URL:** `/api/products/{id}`  
**Example:** `/api/products/1001`

**What happens behind the scenes:**
1. You provide the product ID in the URL
2. `ProductController` calls `productService.deleteProduct(id)`
3. `ProductService` checks if the product exists using `productRepository.existsById(id)`
4. If it exists → deletes it and returns `200 OK`
5. If it does not exist → returns `404 NOT FOUND` without crashing

**Success Response:** `200 OK`
```json
"Product deleted successfully"
```

**If not found:** `404 NOT FOUND`
```json
"Product not found with id: 1001"
```

---

###  6. SEARCH — Find Products by Category
**Method:** `GET`  
**URL:** `/api/products/search?category={category}`  
**Example:** `/api/products/search?category=Electronics`

**What happens behind the scenes:**
1. You pass a category name as a query parameter
2. `ProductService` calls `productRepository.findByCategory(categoryName)`
3. Spring Data JPA automatically builds the SQL query from the method name
4. Returns all matching products or `404 NOT FOUND` if none exist

---

###  7. SEARCH — Find Products by Price and Brand
**Method:** `GET`  
**URL:** `/api/products/searchByPriceAndBrand?price={price}&brand={brand}`  
**Example:** `/api/products/searchByPriceAndBrand?price=89.99&brand=TechSound`

**What happens behind the scenes:**
1. You pass both price and brand as query parameters
2. `ProductService` calls `productRepository.findByPriceAndBrand(price, brand)`
3. Returns all products that match both conditions

---

##  How to Test with Postman

1. Open **Postman**
2. Choose the request method (GET, POST, PUT, DELETE)
3. Enter the full URL e.g. `http://localhost:8080/api/products`
4. For **POST** and **PUT** requests:
   - Click on **Body** tab → select **raw** → choose **JSON**
   - Paste your JSON data
5. Click **Send** and check the response

---

##  How to Run the Project

### Step 1 — Set up the database
Open PostgreSQL and run:
```sql
CREATE DATABASE ecommerce_db;
```

### Step 2 — Clone and run the project
```bash
git clone https://github.com/yourname/restfull-api-crud-assignment.git
cd restfull-api-crud-assignment
mvn clean install
mvn spring-boot:run
```

### Step 3 — Test it
Open Postman and start sending requests to `http://localhost:8080/api/products`

---

##  Sample Test Data

```json
{
    "id": 1002,
    "name": "Smart Watch",
    "description": "Fitness tracking smartwatch with heart rate monitor",
    "price": 199.99,
    "category": "Electronics",
    "stockQuantity": 75,
    "brand": "FitTech"
}
```

```json
{
    "id": 1003,
    "name": "Laptop Stand",
    "description": "Ergonomic aluminum laptop stand",
    "price": 45.50,
    "category": "Accessories",
    "stockQuantity": 200,
    "brand": "DeskPro"
}
```

---

##  Summary of What Was Implemented

| Feature | Status |
|---|---|
| Create a new product | ✅ Done |
| Get all products | ✅ Done |
| Get a product by ID | ✅ Done |
| Update an existing product | ✅ Done |
| Delete a product | ✅ Done |
| Search by category | ✅ Done |
| Search by price and brand | ✅ Done |
| Duplicate ID validation | ✅ Done |
| Proper HTTP status codes | ✅ Done |
| PostgreSQL database integration | ✅ Done |

---

##  HTTP Status Codes Used

| Code | Meaning | When it happens |
|---|---|---|
| `200 OK` | Success | Product found, updated, or deleted |
| `201 CREATED` | Created | New product added successfully |
| `302 FOUND` | Found | Search results returned |
| `404 NOT FOUND` | Not found | Product with given ID does not exist |
| `409 CONFLICT` | Conflict | Product with same ID already exists |
