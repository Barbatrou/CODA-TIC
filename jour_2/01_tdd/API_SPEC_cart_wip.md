# Feature: Shopping Cart Management

## Overview
A complete shopping cart system that allows users to manage items before checkout, apply discounts, and calculate final costs including tax and shipping. This feature is entirely self-contained and perfect for TDD methodology.

---

## API Endpoints

### 1. Add Item to Cart
```
POST /api/cart/{user_id}/items
```

#### Request Headers
```
Content-Type: application/json
Authorization: Basic <credentials>
```

#### Request Body
```json
{
  "product_id": 1,
  "quantity": 2
}
```

#### Success Response
**Code:** `201 CREATED`

```json
{
  "user_id": 3,
  "items": [
    {
      "product_id": 1,
      "product_name": "Dune",
      "price": 7.99,
      "quantity": 2,
      "subtotal": 15.98
    }
  ],
  "cart_subtotal": 15.98
}
```

#### Error Responses
**Code:** `400 BAD REQUEST`
```json
{
  "error": "Insufficient stock",
  "product_id": 1,
  "requested": 50,
  "available": 31
}
```

---

### 2. Update Cart Item
```
PATCH /api/cart/{user_id}/items/{product_id}
```

#### Request Body
```json
{
  "quantity": 5
}
```

#### Success Response
**Code:** `200 OK`

```json
{
  "product_id": 1,
  "product_name": "Dune",
  "price": 7.99,
  "quantity": 5,
  "subtotal": 39.95
}
```

---

### 3. Remove Item from Cart
```
DELETE /api/cart/{user_id}/items/{product_id}
```

#### Success Response
**Code:** `204 NO CONTENT`

---

### 4. Get Cart
```
GET /api/cart/{user_id}
```

#### Success Response
**Code:** `200 OK`

```json
{
  "user_id": 3,
  "items": [
    {
      "product_id": 1,
      "product_name": "Dune",
      "price": 7.99,
      "quantity": 2,
      "subtotal": 15.98
    }
  ],
  "cart_subtotal": 15.98,
  "discount": 0.00,
  "tax": 1.60,
  "shipping": 5.00,
  "total": 22.58
}
```

---

### 5. Apply Discount Code
```
POST /api/cart/{user_id}/discount
```

#### Request Body
```json
{
  "code": "SAVE10"
}
```

#### Success Response
**Code:** `200 OK`

```json
{
  "code": "SAVE10",
  "type": "percentage",
  "value": 10,
  "discount_amount": 1.60,
  "new_subtotal": 14.38
}
```

#### Error Responses
**Code:** `400 BAD REQUEST`
```json
{
  "error": "Invalid discount code",
  "code": "INVALID"
}
```

---

### 6. Calculate Cart Total
```
GET /api/cart/{user_id}/total
```

#### Success Response
**Code:** `200 OK`

```json
{
  "subtotal": 15.98,
  "discount": 1.60,
  "subtotal_after_discount": 14.38,
  "tax": 1.44,
  "shipping": 5.00,
  "total": 20.82
}
```

---

### 7. Validate Cart
```
POST /api/cart/{user_id}/validate
```

#### Success Response
**Code:** `200 OK`

```json
{
  "valid": true,
  "issues": []
}
```

#### Validation Issues Response
**Code:** `200 OK`

```json
{
  "valid": false,
  "issues": [
    {
      "type": "out_of_stock",
      "product_id": 3,
      "product_name": "Game of Throne: complete mega deluxe edition",
      "requested": 5,
      "available": 2
    },
    {
      "type": "price_changed",
      "product_id": 1,
      "product_name": "Dune",
      "cart_price": 7.99,
      "current_price": 8.99
    }
  ]
}
```

---

## TDD Development Units

### Unit 1: Add Item to Cart
**Purpose:** Add a product to the shopping cart with quantity validation

**Test Cases:**
1. Successfully add new product to empty cart
2. Successfully add new product to cart with existing items
3. Add same product twice - should update quantity (not duplicate)
4. Cannot add product with quantity <= 0
5. Cannot add product with quantity > available stock
6. Cannot add non-existent product
7. Adding product calculates item subtotal correctly (price × quantity)

**Implementation Class & Method:**
```java
public class CartItem {
    private int productId;
    private String productName;
    private double price;
    private int quantity;

    public CartItem(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return Math.round(this.price * this.quantity * 100.0) / 100.0;
    }

    // Getters and setters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

public class CartService {
    /**
     * Add item to cart or update quantity if already exists.
     * @throws IllegalArgumentException if product doesn't exist, quantity invalid, or insufficient stock
     * @return updated cart items list
     */
    public List<CartItem> addItemToCart(
        List<CartItem> cartItems,
        int productId,
        int quantity,
        List<Product> products
    ) {
        // Implementation here
        return cartItems;
    }
}
```

---

### Unit 2: Update Cart Item Quantity
**Purpose:** Change the quantity of an existing cart item

**Test Cases:**
1. Successfully increase quantity of existing item
2. Successfully decrease quantity of existing item
3. Setting quantity to 0 removes the item
4. Cannot update to quantity > available stock
5. Cannot update to negative quantity
6. Cannot update non-existent cart item
7. Subtotal recalculates correctly after update

**Implementation Method:**
```java
public class CartService {
    /**
     * Update quantity of cart item.
     * If new quantity is 0, removes the item.
     * @throws IllegalArgumentException if item not in cart, quantity invalid, or insufficient stock
     * @return updated cart items list
     */
    public List<CartItem> updateCartItemQuantity(
        List<CartItem> cartItems,
        int productId,
        int newQuantity,
        List<Product> products
    ) {
        // Implementation here
        return cartItems;
    }
}
```

---

### Unit 3: Remove Item from Cart
**Purpose:** Remove a specific product from the cart

**Test Cases:**
1. Successfully remove existing item
2. Removing from cart with multiple items leaves others intact
3. Removing last item results in empty cart
4. Cannot remove non-existent item (should raise error)
5. Removing item from empty cart raises error

**Implementation Method:**
```java
public class CartService {
    /**
     * Remove item from cart.
     * @throws IllegalArgumentException if item not in cart
     * @return updated cart items list
     */
    public List<CartItem> removeItemFromCart(
        List<CartItem> cartItems,
        int productId
    ) {
        // Implementation here
        return cartItems;
    }
}
```

---

### Unit 4: Calculate Cart Subtotal
**Purpose:** Sum the cost of all items in cart

**Test Cases:**
1. Empty cart has subtotal of 0.00
2. Single item: subtotal = price × quantity
3. Multiple items: sum all item subtotals
4. Result rounded to 2 decimal places
5. Large quantities calculate correctly
6. Decimal prices (like 7.99) calculate correctly

**Implementation Method:**
```java
public class CartCalculator {
    /**
     * Calculate total cost of all items in cart before discounts/tax/shipping.
     * @return subtotal rounded to 2 decimal places
     */
    public double calculateCartSubtotal(List<CartItem> cartItems) {
        // Implementation here
        return 0.0;
    }
}
```

---

### Unit 5: Apply Discount Code
**Purpose:** Validate and apply discount codes to cart

**Test Cases:**
1. Valid percentage discount (e.g., 10% off): subtotal × (percentage/100)
2. Valid fixed discount (e.g., $5 off): subtotal - fixed_amount
3. Invalid/unknown discount code raises error
4. Expired discount code raises error
5. Discount requiring minimum purchase - valid when met
6. Discount requiring minimum purchase - invalid when not met
7. Discount cannot exceed subtotal (max discount = subtotal)
8. One-time use code already used raises error
9. Discount amount rounded to 2 decimal places

**Implementation Classes & Method:**
```java
public class DiscountCode {
    private String code;
    private String type; // "percentage" or "fixed"
    private double value;
    private double minPurchase;
    private String expiryDate; // ISO format or null
    private boolean oneTimeUse;
    private List<Integer> usedBy; // user IDs who used it

    public DiscountCode(String code, String type, double value) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.minPurchase = 0.0;
        this.expiryDate = null;
        this.oneTimeUse = false;
        this.usedBy = new ArrayList<>();
    }

    // Getters and setters
    public String getCode() { return code; }
    public String getType() { return type; }
    public double getValue() { return value; }
    public double getMinPurchase() { return minPurchase; }
    public void setMinPurchase(double minPurchase) { this.minPurchase = minPurchase; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public boolean isOneTimeUse() { return oneTimeUse; }
    public void setOneTimeUse(boolean oneTimeUse) { this.oneTimeUse = oneTimeUse; }
    public List<Integer> getUsedBy() { return usedBy; }
}

public class AppliedDiscount {
    private String code;
    private String type;
    private double value;
    private double discountAmount;

    public AppliedDiscount(String code, String type, double value, double discountAmount) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.discountAmount = discountAmount;
    }

    // Getters
    public String getCode() { return code; }
    public String getType() { return type; }
    public double getValue() { return value; }
    public double getDiscountAmount() { return discountAmount; }
}

public class DiscountService {
    /**
     * Apply discount code to cart subtotal.
     * @throws IllegalArgumentException if code invalid, expired, doesn't meet minimum, or already used
     * @return AppliedDiscount with calculated discount amount
     */
    public AppliedDiscount applyDiscountCode(
        double subtotal,
        String discountCode,
        List<DiscountCode> availableCodes,
        int userId,
        String currentDate
    ) {
        // Implementation here
        return null;
    }
}
```

---

### Unit 6: Calculate Tax
**Purpose:** Calculate tax based on product categories

**Test Cases:**
1. Books: 5% tax rate
2. Cars: 20% tax rate
3. Pokemon: 10% tax rate
4. Mixed cart with multiple categories - sum individual taxes
5. Tax applied to subtotal after discount
6. Tax rounded to 2 decimal places
7. Empty cart has $0.00 tax

**Implementation Method:**
```java
public class TaxCalculator {
    private static final Map<String, Double> TAX_RATES = Map.of(
        "book", 0.05,
        "car", 0.20,
        "pokemon", 0.10
    );

    /**
     * Calculate tax based on product categories.
     * Tax is calculated per item based on category, then summed.
     * Tax is applied to discounted prices.
     * @return tax amount rounded to 2 decimal places
     */
    public double calculateTax(
        List<CartItem> cartItems,
        List<Product> products,
        double subtotalAfterDiscount
    ) {
        // Implementation here
        return 0.0;
    }
}
```

---

### Unit 7: Calculate Shipping Cost
**Purpose:** Calculate shipping based on cart total

**Test Cases:**
1. Cart total < $50: shipping = $5.00
2. Cart total >= $50 and < $100: shipping = $3.00
3. Cart total >= $100: free shipping ($0.00)
4. Empty cart: shipping = $0.00
5. Shipping calculated on subtotal after discount
6. Exact threshold values ($50.00, $100.00) use the lower rate

**Implementation Method:**
```java
public class ShippingCalculator {
    /**
     * Calculate shipping cost based on cart subtotal after discount.
     * - Under $50: $5.00
     * - $50-$99.99: $3.00
     * - $100+: Free
     * @return shipping cost
     */
    public double calculateShipping(double subtotalAfterDiscount) {
        // Implementation here
        return 0.0;
    }
}
```

---

### Unit 8: Calculate Cart Total
**Purpose:** Combine all cost components into final total

**Test Cases:**
1. Total = subtotal - discount + tax + shipping
2. Empty cart: total = $0.00
3. Cart with no discount: total = subtotal + tax + shipping
4. Cart with free shipping: total = subtotal - discount + tax
5. All amounts rounded to 2 decimal places
6. Order of operations: apply discount first, then calculate tax and shipping

**Implementation Classes & Method:**
```java
public class CartTotal {
    private double subtotal;
    private double discount;
    private double subtotalAfterDiscount;
    private double tax;
    private double shipping;
    private double total;

    public CartTotal(double subtotal, double discount, double subtotalAfterDiscount,
                     double tax, double shipping, double total) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.subtotalAfterDiscount = subtotalAfterDiscount;
        this.tax = tax;
        this.shipping = shipping;
        this.total = total;
    }

    // Getters
    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getSubtotalAfterDiscount() { return subtotalAfterDiscount; }
    public double getTax() { return tax; }
    public double getShipping() { return shipping; }
    public double getTotal() { return total; }
}

public class CartCalculator {
    private TaxCalculator taxCalculator;
    private ShippingCalculator shippingCalculator;

    public CartCalculator() {
        this.taxCalculator = new TaxCalculator();
        this.shippingCalculator = new ShippingCalculator();
    }

    /**
     * Calculate complete cart total with all components.
     * @return CartTotal object with all cost breakdowns
     */
    public CartTotal calculateCartTotal(
        List<CartItem> cartItems,
        List<Product> products,
        AppliedDiscount appliedDiscount
    ) {
        // Implementation here
        return null;
    }
}
```

---

### Unit 9: Validate Cart Before Checkout
**Purpose:** Ensure cart is valid for checkout

**Test Cases:**
1. Valid cart - all items in stock, prices unchanged, cart not empty
2. Empty cart is invalid
3. Item out of stock detected
4. Item quantity exceeds available stock detected
5. Product price increased since added to cart detected
6. Product price decreased since added to cart detected
7. Product no longer exists detected
8. Multiple issues detected and all reported
9. Valid cart returns empty issues list

**Implementation Classes & Method:**
```java
public class ValidationIssue {
    private String type; // "out_of_stock", "price_changed", "product_removed", "insufficient_stock"
    private int productId;
    private String productName;
    private Map<String, Object> details;

    public ValidationIssue(String type, int productId, String productName, Map<String, Object> details) {
        this.type = type;
        this.productId = productId;
        this.productName = productName;
        this.details = details;
    }

    // Getters
    public String getType() { return type; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Map<String, Object> getDetails() { return details; }
}

public class CartValidation {
    private boolean valid;
    private List<ValidationIssue> issues;

    public CartValidation(boolean valid, List<ValidationIssue> issues) {
        this.valid = valid;
        this.issues = issues;
    }

    // Getters
    public boolean isValid() { return valid; }
    public List<ValidationIssue> getIssues() { return issues; }
}

public class CartValidator {
    /**
     * Validate cart before checkout.
     * Checks: cart not empty, all products exist, stock available, prices current.
     * @return CartValidation with validity status and any issues found
     */
    public CartValidation validateCart(
        List<CartItem> cartItems,
        List<Product> products
    ) {
        // Implementation here
        return null;
    }
}
```

---

## Data Models

### Product
```java
public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    public Product(int id, String name, String category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
```

### Cart
```java
public class Cart {
    private int userId;
    private List<CartItem> items;
    private AppliedDiscount appliedDiscount;

    public Cart(int userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.appliedDiscount = null;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
    public AppliedDiscount getAppliedDiscount() { return appliedDiscount; }
    public void setAppliedDiscount(AppliedDiscount appliedDiscount) { this.appliedDiscount = appliedDiscount; }
}
```

---

## TDD Development Workflow

### Recommended Implementation Order:
1. Unit 4: Calculate Cart Subtotal (simplest - pure calculation)
2. Unit 1: Add Item to Cart (core functionality)
3. Unit 2: Update Cart Item Quantity (builds on Unit 1)
4. Unit 3: Remove Item from Cart (simple operation)
5. Unit 7: Calculate Shipping Cost (simple rules-based calculation)
6. Unit 5: Apply Discount Code (validation logic)
7. Unit 6: Calculate Tax (category-based calculation)
8. Unit 8: Calculate Cart Total (integrates Units 4, 5, 6, 7)
9. Unit 9: Validate Cart Before Checkout (comprehensive validation)

---

## Example JUnit Test Structure

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class CartCalculatorTest {

    private CartCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new CartCalculator();
    }

    @Test
    public void testEmptyCartHasZeroSubtotal() {
        List<CartItem> emptyCart = new ArrayList<>();
        double subtotal = calculator.calculateCartSubtotal(emptyCart);
        assertEquals(0.0, subtotal, 0.001);
    }

    @Test
    public void testSingleItemSubtotal() {
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(1, "Dune", 7.99, 2));

        double subtotal = calculator.calculateCartSubtotal(cart);
        assertEquals(15.98, subtotal, 0.001);
    }

    @Test
    public void testMultipleItemsSubtotal() {
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(1, "Dune", 7.99, 2));
        cart.add(new CartItem(2, "Pikachu", 25.25, 1));

        double subtotal = calculator.calculateCartSubtotal(cart);
        assertEquals(41.23, subtotal, 0.001);
    }
}
```

---

## Example Usage

### Add item to cart
```bash
curl -X POST http://localhost:8080/api/cart/3/items \
  -u client_1:password \
  -H "Content-Type: application/json" \
  -d '{
    "product_id": 1,
    "quantity": 2
  }'
```

### Update item quantity
```bash
curl -X PATCH http://localhost:8080/api/cart/3/items/1 \
  -u client_1:password \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 5
  }'
```

### Apply discount code
```bash
curl -X POST http://localhost:8080/api/cart/3/discount \
  -u client_1:password \
  -H "Content-Type: application/json" \
  -d '{
    "code": "SAVE10"
  }'
```

### Get cart total
```bash
curl -X GET http://localhost:8080/api/cart/3/total \
  -u client_1:password
```

### Validate cart
```bash
curl -X POST http://localhost:8080/api/cart/3/validate \
  -u client_1:password
```

### Remove item
```bash
curl -X DELETE http://localhost:8080/api/cart/3/items/1 \
  -u client_1:password
```

---

## Sample Discount Codes for Testing

```json
{
  "discount_codes": [
    {
      "code": "SAVE10",
      "type": "percentage",
      "value": 10,
      "min_purchase": 0,
      "expiry_date": null,
      "one_time_use": false
    },
    {
      "code": "SAVE5",
      "type": "fixed",
      "value": 5.00,
      "min_purchase": 20.00,
      "expiry_date": null,
      "one_time_use": false
    },
    {
      "code": "FIRSTORDER",
      "type": "percentage",
      "value": 15,
      "min_purchase": 0,
      "expiry_date": null,
      "one_time_use": true,
      "used_by": []
    },
    {
      "code": "EXPIRED",
      "type": "percentage",
      "value": 50,
      "min_purchase": 0,
      "expiry_date": "2025-01-01",
      "one_time_use": false
    }
  ]
}
```

---

## Benefits for TDD Practice

This feature is excellent for TDD because:

1. **Self-Contained:** No external dependencies (suppliers, payments, etc.)
2. **Clear Business Rules:** Well-defined calculation logic
3. **Progressive Complexity:** Start simple, build up to integration
4. **Pure Functions:** Most units are pure calculations (same input = same output)
5. **Real-World Relevance:** Common e-commerce functionality
6. **Rich Edge Cases:** Rounding, empty states, boundary conditions
7. **Data Validation:** Multiple validation scenarios
8. **State Management:** Cart state changes through operations
9. **Integration Testing:** Final units combine earlier units
10. **Testable Components:** Each unit can be tested independently
