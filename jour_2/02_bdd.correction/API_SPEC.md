# API Specification - New Features

## Base URL
```
http://localhost:8080/api
```

All endpoints require authentication. Include authentication credentials with each request.

---

## 1. Create Order

Creates a new order after validating product availability and quantities, automatically calculates the total price, and deducts quantities from product inventory.

### Endpoint
```
POST /api/orders
```

### Request Headers
```
Content-Type: application/json
Authorization: Basic <credentials>
```

### Request Body
```json
{
  "user_id": 3,
  "products": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 7,
      "quantity": 5
    }
  ]
}
```

### Request Schema
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| user_id | integer | Yes | ID of the user placing the order |
| products | array | Yes | List of products to order |
| products[].productId | integer | Yes | ID of the product |
| products[].quantity | integer | Yes | Quantity to order (must be > 0) |

### Success Response
**Code:** `201 CREATED`

```json
{
  "id": 3,
  "user_id": 3,
  "products": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 7,
      "quantity": 5
    }
  ],
  "total": 31.68
}
```

### Error Responses

#### Product Not Found
**Code:** `404 NOT FOUND`

```json
{
  "error": "Product not found",
  "productId": 999
}
```

#### Insufficient Stock
**Code:** `400 BAD REQUEST`

```json
{
  "error": "Insufficient stock",
  "productId": 3,
  "requested": 10,
  "available": 2
}
```

#### Invalid Request
**Code:** `400 BAD REQUEST`

```json
{
  "error": "Invalid request",
  "message": "Products list cannot be empty"
}
```

or

```json
{
  "error": "Invalid request",
  "message": "Quantity must be greater than 0"
}
```

### Business Logic
1. **Validation**: For each product in the order:
   - Verify the product exists in the database
   - Check that requested quantity ≤ available quantity
2. **Calculation**: Calculate total price by summing (product.price × quantity) for all items
3. **Inventory Update**: Deduct ordered quantities from product inventory
4. **Order Creation**: Create and save the order with calculated total

### Example
```bash
curl -X POST http://localhost:8080/api/orders \
  -u client_1:password \
  -H "Content-Type: application/json" \
  -d '{
    "user_id": 3,
    "products": [
      {"productId": 1, "quantity": 2},
      {"productId": 7, "quantity": 5}
    ]
  }'
```

---

## 2. Adjust Category Prices

Updates prices for all products in a specific category by a given percentage. Supports both price increases and decreases.

### Endpoint
```
PATCH /api/products/category/{category}/price
```

### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| category | string | Category name (e.g., "book", "car", "pokemon") |

### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| percentage | number | Yes | Percentage to adjust prices. Positive for increase, negative for decrease |

### Request Headers
```
Authorization: Basic <credentials>
```

### Success Response
**Code:** `200 OK`

```json
{
  "category": "book",
  "percentage": 10,
  "productsUpdated": 3,
  "products": [
    {
      "id": 1,
      "name": "Dune",
      "category": "book",
      "price": 8.79,
      "quantity": 31
    },
    {
      "id": 2,
      "name": "The Witcher",
      "category": "book",
      "price": 8.98,
      "quantity": 14
    },
    {
      "id": 3,
      "name": "Game of Throne: complete mega deluxe edition",
      "category": "book",
      "price": 109.66,
      "quantity": 2
    }
  ]
}
```

### Error Responses

#### Category Not Found
**Code:** `404 NOT FOUND`

```json
{
  "error": "Category not found",
  "category": "electronics"
}
```

#### Invalid Percentage
**Code:** `400 BAD REQUEST`

```json
{
  "error": "Invalid percentage",
  "message": "Percentage cannot result in negative prices"
}
```

or

```json
{
  "error": "Invalid percentage",
  "message": "Percentage parameter is required"
}
```

### Business Logic
1. **Validation**:
   - Verify the category exists (at least one product has this category)
   - Ensure percentage doesn't result in negative prices (percentage ≥ -100)
2. **Calculation**: For each product in the category:
   - New price = current price × (1 + percentage/100)
   - Round to 2 decimal places
3. **Update**: Save all updated products

### Examples

#### Increase prices by 10%
```bash
curl -X PATCH "http://localhost:8080/api/products/category/book/price?percentage=10" \
  -u admin_1
```

#### Decrease prices by 15%
```bash
curl -X PATCH "http://localhost:8080/api/products/category/pokemon/price?percentage=-15" \
  -u admin_1
```

#### Increase prices by 25.5%
```bash
curl -X PATCH "http://localhost:8080/api/products/category/car/price?percentage=25.5" \
  -u admin_1
```

---

## Data Models

### Order
```json
{
  "id": integer,
  "user_id": integer,
  "products": [OrderItem],
  "total": number
}
```

### OrderItem
```json
{
  "productId": integer,
  "quantity": integer
}
```

### Product
```json
{
  "id": integer,
  "name": string,
  "category": string,
  "price": number,
  "quantity": integer
}
```

---

## Authentication

All endpoints require HTTP Basic Authentication using credentials from the users list.

**Available Users:**
- admin_1 (permissions: admin)
- admin_2 (permissions: admin, client)
- client_1 (permissions: client)
- client_2 (permissions: client)

---

## Notes

- All prices are rounded to 2 decimal places
- Product quantities are updated atomically during order creation
- The category parameter is case-sensitive
- Percentage adjustments compound on current prices (not original prices)
- Orders cannot be placed for products with 0 quantity
