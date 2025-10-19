# Cobaye API

## Build and launch

### build image
`make image` or `docker build -t coda/tic/cobaye-api:latest --target testing`

### serve project
```
$ make debug
$ make shell
container-host$ /opt/build_and_run.sh
```
or
```
$ docker run --name cobaye-api -p 8080:8080 -d --rm  -v ./cobaye_api:/app -v ./data.json:/var/cobaye_api.data.json coda/tic/cobaye-api:latest
```

### test project
```
$ make debug
$ make shell
container-host$ /opt/build_and_test.sh
```
or
```
$ docker run --name cobaye-api -p 8080:8080 -d --rm --entrypoint /opt/build_and_test.sh -v ./cobaye_api:/app -v ./data.json:/var/cobaye_api.data.json coda/tic/cobaye-api:latest
```

## Exercice

Ecrivez les tests de l'API en utilisant `mockMvc` pour simuler un appel http
Faites attention à bien penser à tout les cas d'usage, qui a la permission de faire quoi etc...

## API Documentation

### Base Configuration

- **Base URL**: `http://localhost:8080`
- **Authentication**: HTTP Basic Auth (username only, no password required)
  - Available users: `admin_1`, `admin_2`, `client_1`, `client_2`
  - Example: `curl -u admin_1: http://localhost:8080/api/users`
- **Authorization**:
  - Admin users (`admin_1`, `admin_2`) can perform all operations (GET, POST, PUT, DELETE)
  - Client users (`client_1`, `client_2`) can only perform read operations (GET)
- **CSRF**: Disabled
- **Content-Type**: `application/json`

### API Endpoints

```http
GET /api/users
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "string",
    "permissions": ["string"]
  }
]
```

---

```http
GET /api/users/{id}
```

**Path Parameters**:
- `id` (integer, required) - User ID

**Response**: `200 OK`
```json
{
  "id": 1,
  "name": "string",
  "permissions": ["string"]
}
```

**Error Response**: `404 Not Found` if user doesn't exist

---

```http
POST /api/users
```

**Authorization**: Requires admin permission

**Request Body**:
```json
{
  "name": "string",
  "permissions": ["string"]
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "name": "string",
  "permissions": ["string"]
}
```

**Error Response**: `403 Forbidden` if user doesn't have admin permission

---

```http
PUT /api/users/{id}
```

**Authorization**: Requires admin permission

**Path Parameters**:
- `id` (integer, required) - User ID

**Request Body** (all fields optional):
```json
{
  "name": "string",
  "permissions": ["string"]
}
```

**Response**: `200 OK`
```json
{
  "id": 1,
  "name": "string",
  "permissions": ["string"]
}
```

**Error Responses**:
- `403 Forbidden` if user doesn't have admin permission
- `404 Not Found` if user doesn't exist

---

```http
DELETE /api/users/{id}
```

**Authorization**: Requires admin permission

**Path Parameters**:
- `id` (integer, required) - User ID

**Response**: `204 No Content`

**Error Responses**:
- `403 Forbidden` if user doesn't have admin permission
- `404 Not Found` if user doesn't exist

---

```http
GET /api/products?category={category}
```

**Query Parameters**:
- `category` (string, optional) - Filter products by category

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "string",
    "category": "string",
    "price": 99.99,
    "quantity": 100
  }
]
```

---

```http
GET /api/products/{id}
```

**Path Parameters**:
- `id` (integer, required) - Product ID

**Response**: `200 OK`
```json
{
  "id": 1,
  "name": "string",
  "category": "string",
  "price": 99.99,
  "quantity": 100
}
```

**Error Response**: `404 Not Found` if product doesn't exist

---

```http
POST /api/products
```

**Authorization**: Requires admin permission

**Request Body**:
```json
{
  "name": "string",
  "category": "string",
  "price": 99.99,
  "quantity": 100
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "name": "string",
  "category": "string",
  "price": 99.99,
  "quantity": 100
}
```

**Error Response**: `403 Forbidden` if user doesn't have admin permission

---

```http
PUT /api/products/{id}
```

**Authorization**: Requires admin permission

**Path Parameters**:
- `id` (integer, required) - Product ID

**Request Body** (all fields optional):
```json
{
  "name": "string",
  "category": "string",
  "price": 99.99,
  "quantity": 100
}
```

**Response**: `200 OK`
```json
{
  "id": 1,
  "name": "string",
  "category": "string",
  "price": 99.99,
  "quantity": 100
}
```

**Error Responses**:
- `403 Forbidden` if user doesn't have admin permission
- `404 Not Found` if product doesn't exist

---

```http
DELETE /api/products/{id}
```

**Authorization**: Requires admin permission

**Path Parameters**:
- `id` (integer, required) - Product ID

**Response**: `204 No Content`

**Error Responses**:
- `403 Forbidden` if user doesn't have admin permission
- `404 Not Found` if product doesn't exist

---

```http
GET /api/orders?user_id={user_id}
```

**Query Parameters**:
- `user_id` (integer, optional) - Filter orders by user ID

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "user_id": 1,
    "products": [
      {
        "productId": 101,
        "quantity": 2
      }
    ],
    "total": 199.98
  }
]
```

---

```http
GET /api/orders/{id}
```

**Path Parameters**:
- `id` (integer, required) - Order ID

**Response**: `200 OK`
```json
{
  "id": 1,
  "user_id": 1,
  "products": [
    {
      "productId": 101,
      "quantity": 2
    }
  ],
  "total": 199.98
}
```

**Error Response**: `404 Not Found` if order doesn't exist

---

```http
POST /api/orders
```

**Authorization**: Requires admin permission

**Request Body**:
```json
{
  "user_id": 1,
  "products": [
    {
      "productId": 101,
      "quantity": 2
    }
  ],
  "total": 199.98
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "user_id": 1,
  "products": [
    {
      "productId": 101,
      "quantity": 2
    }
  ],
  "total": 199.98
}
```

**Error Response**: `403 Forbidden` if user doesn't have admin permission

---

```http
PUT /api/orders/{id}
```

**Authorization**: Requires admin permission

**Path Parameters**:
- `id` (integer, required) - Order ID

**Request Body** (all fields optional):
```json
{
  "user_id": 1,
  "products": [
    {
      "productId": 101,
      "quantity": 2
    }
  ],
  "total": 199.98
}
```

**Response**: `200 OK`
```json
{
  "id": 1,
  "user_id": 1,
  "products": [
    {
      "productId": 101,
      "quantity": 2
    }
  ],
  "total": 199.98
}
```

**Error Responses**:
- `403 Forbidden` if user doesn't have admin permission
- `404 Not Found` if order doesn't exist

---

```http
DELETE /api/orders/{id}
```

**Authorization**: Requires admin permission

**Path Parameters**:
- `id` (integer, required) - Order ID

**Response**: `204 No Content`

**Error Responses**:
- `403 Forbidden` if user doesn't have admin permission
- `404 Not Found` if order doesn't exist

---

### Example cURL Commands

```bash
# Get all users (any authenticated user)
curl -u client_1: http://localhost:8080/api/users

# Get user by ID (any authenticated user)
curl -u client_1: http://localhost:8080/api/users/1

# Create user (admin only)
curl -u admin_1: -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","permissions":["read","write"]}'

# Update user (admin only)
curl -u admin_1: -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe"}'

# Delete user (admin only)
curl -u admin_1: -X DELETE http://localhost:8080/api/users/1

# Get all products (any authenticated user)
curl -u client_1: http://localhost:8080/api/products

# Get products by category (any authenticated user)
curl -u client_1: "http://localhost:8080/api/products?category=book"

# Create product (admin only)
curl -u admin_1: -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"New Book","category":"book","price":15.99,"quantity":50}'

# Create order (admin only)
curl -u admin_1: -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"user_id":3,"products":[{"productId":1,"quantity":2}],"total":15.98}'
```
