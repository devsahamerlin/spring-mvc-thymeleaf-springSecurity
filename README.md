# spring-mvc-thymeleaf-springSecurity

Java web application based on Spring, Spring Data JPA, Hibernate, Tymeleaf and Spring Security that allows you to manage products

This Spring MVC application provides a comprehensive product management app with role-based access control. The system supports both USER and ADMIN roles with different levels of access to various features.

![run.gif](images/run.gif)

## Features Overview

### Product Search & Filtering
- Search products by name (case-insensitive)
- Filter products by price range
- Filter products by minimum quantity/stock
- Combined search with multiple filters
- View all products without filters

### Role-Based Access Control
- **USER Role**: Read-only access to product listings and search
- **ADMIN Role**: Full CRUD operations on products
- Secure authentication and authorization

### Product Management (Admin Only)
- Create new products
- Edit existing products
- Delete products
- Form validation with error handling

## Controller

### Public Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Redirects to main product listing |
| GET | `/login` | Login page |
| GET | `/logout` | Logout and session invalidation |
| GET | `/notAuthorized` | Unauthorized access page |


### User Endpoints (Requires USER role)

#### Main Product Listing
```
GET /user/index
```
**Parameters:**
- `keyword` (optional): Search term for product name
- `minPrice` (optional): Minimum price filter
- `maxPrice` (optional): Maximum price filter
- `minQuantity` (optional): Minimum quantity filter

**Features:**
- Supports multiple filter combinations
- Price range filtering
- Stock quantity filtering
- Returns all products if no filters applied

#### Price-Based Search
```
GET /user/searchByPrice
```
**Parameters:**
- `minPrice` (required): Minimum price
- `maxPrice` (required): Maximum price

**Description:** Dedicated route for price range searches

#### Stock-Based Search
```
GET /user/inStock
```
**Parameters:**
- `threshold` (optional, default: 0): Minimum quantity threshold

**Description:** Find products with quantity greater than specified threshold

### Admin Routes (Requires ADMIN role)

#### Product Creation
```
GET /admin/newProduct
```
**Description:** Display form for creating new products

```
POST /admin/saveProduct
```
**Description:** Save new product with validation
- Automatically generates UUID for new products
- Validates product data
- Handles validation errors gracefully

#### Product Management
```
GET /admin/editProduct?id={productId}
```
**Description:** Display edit form for existing product

```
POST /admin/updateProduct
```
**Description:** Update existing product with validation

```
POST /admin/delete?id={productId}
```
**Description:** Delete product by ID

## Search Capabilities

### 1. Keyword Search
- Searches product names
- Partial matching supported

### 2. Price Range Filtering
- Filter products within specified price range
- Supports decimal values
- Can be combined with keyword search

### 3. Stock/Quantity Filtering
- Find products with quantity above threshold
- Useful for inventory management
- Default threshold is 0 (all products with stock)

### 4. Combined Filters
The system intelligently combines multiple filters:
- Keyword + Price range
- Single keyword search
- Single price range
- Single quantity filter
- No filters (show all)

## Security Features

- **Role-based authorization** using Spring Security
- **Method-level security** with `@PreAuthorize`
- **Session management** for logout functionality
- **Input validation** on all forms
- **Error handling** for unauthorized access

## Repository Methods

The controller utilizes these repository methods:
- `findAll()`: Get all products
- `findByNameContainingIgnoreCase()`: name search
- `findByPriceBetween()`: Price range filtering
- `findByQuantityGreaterThan()`: Stock-based filtering
- `findByNameContainingIgnoreCaseAndPriceBetween()`: Combined search
- `save()`: Create/update products
- `deleteById()`: Remove products
- `findById()`: Get specific product

## Technology Stack

- **Spring MVC**: Web framework
- **Spring Security**: Authentication & authorization
- **Spring Data JPA**: Database operations
- **Thymeleaf**: Template engine (implied by return values)
- **Bean Validation**: Form validation
- **Relational Database**: H2

