# Cobaye API

## Build and launch

### build image
`make image` or `docker build -t coda/tic/cobaye-api:latest --target testing .`

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

## Exercice

Ecrivez une procedure pour tester chacun des endpoint d'API

Vous êtes libre d'utiliser la technologie de votre choix, par example Postman ou un script shell avec curl fera tres bien l'affaire.

## API Documentation

### Base Configuration

- **Base URL**: `http://localhost:8080`
- **Security**: No authentication required (all endpoints are public)
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

---

```http
PUT /api/users/{id}
```

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

---

```http
DELETE /api/users/{id}
```
**Path Parameters**:
- `id` (integer, required) - User ID

**Response**: `204 No Content`

**Error Response**: `404 Not Found` if user doesn't exist

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

---

```http
PUT /api/products/{id}
```

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

**Error Response**: `404 Not Found` if product doesn't exist

---

```http
DELETE /api/products/{id}
```

**Path Parameters**:
- `id` (integer, required) - Product ID

**Response**: `204 No Content`

**Error Response**: `404 Not Found` if product doesn't exist

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
        "id": 101,
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
      "id": 101,
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

**Request Body**:
```json
{
  "user_id": 1,
  "products": [
    {
      "id": 101,
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
      "id": 101,
      "quantity": 2
    }
  ],
  "total": 199.98
}
```

---

```http
PUT /api/orders/{id}
```

**Path Parameters**:
- `id` (integer, required) - Order ID

**Request Body** (all fields optional):
```json
{
  "user_id": 1,
  "products": [
    {
      "id": 101,
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
      "id": 101,
      "quantity": 2
    }
  ],
  "total": 199.98
}
```

**Error Response**: `404 Not Found` if order doesn't exist

---

```http
DELETE /api/orders/{id}
```

**Path Parameters**:
- `id` (integer, required) - Order ID

**Response**: `204 No Content`

**Error Response**: `404 Not Found` if order doesn't exist

---

### Example cURL Commands

```bash
# Get all users
curl -X GET http://localhost:8080/api/users

# Get user by ID
curl -X GET http://localhost:8080/api/users/1

# Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","permissions":["read","write"]}'

# Update user
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe"}'

# Delete user
curl -X DELETE http://localhost:8080/api/users/1
```
