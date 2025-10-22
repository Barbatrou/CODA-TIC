package coda.tic.cobaye_api;

import coda.tic.cobaye_api.model.OrderItem;
import coda.tic.cobaye_api.model.Product;
import coda.tic.cobaye_api.repository.ProductRepository;
import coda.tic.cobaye_api.service.OrderService;
import coda.tic.cobaye_api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service Layer Tests")
class ServiceLayerTest {

    @Mock
    private ProductRepository productRepository;

    private OrderService orderService;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(productRepository);
        productService = new ProductService(productRepository);
    }

    @Nested
    @DisplayName("Order Service")
    class OrderServiceTests {

        @Test
        @DisplayName("Should validate order successfully with available stock")
        void validateOrder_withValidProducts_shouldSucceed() {
            List<OrderItem> items = Arrays.asList(new OrderItem(1, 2));
            when(productRepository.findById(1))
                .thenReturn(Optional.of(new Product(1, "Product", "book", 10.0, 10)));

            OrderService.ValidationStatus result = orderService.validateOrder(items);

            assertEquals(OrderService.ValidationStatus.VALID, result);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -5})
        @DisplayName("Should reject orders with invalid quantities")
        void validateOrder_withInvalidQuantity_shouldFail(int quantity) {
            List<OrderItem> items = Arrays.asList(new OrderItem(1, quantity));

            OrderService.ValidationStatus result = orderService.validateOrder(items);

            assertEquals(OrderService.ValidationStatus.INVALID_REQUEST, result);
        }

        @Test
        @DisplayName("Should reject order when product not found")
        void validateOrder_withNonExistentProduct_shouldFail() {
            List<OrderItem> items = Arrays.asList(new OrderItem(999, 2));
            when(productRepository.findById(999)).thenReturn(Optional.empty());

            OrderService.ValidationStatus result = orderService.validateOrder(items);

            assertEquals(OrderService.ValidationStatus.PRODUCT_NOT_FOUND, result);
        }

        @Test
        @DisplayName("Should reject order when insufficient stock")
        void validateOrder_withInsufficientStock_shouldFail() {
            List<OrderItem> items = Arrays.asList(new OrderItem(1, 10));
            when(productRepository.findById(1))
                .thenReturn(Optional.of(new Product(1, "Product", "book", 10.0, 5)));

            OrderService.ValidationStatus result = orderService.validateOrder(items);

            assertEquals(OrderService.ValidationStatus.INSUFFICIENT_STOCK, result);
        }

        @Test
        @DisplayName("Should calculate total correctly for multiple products")
        void calculateTotal_withMultipleProducts_shouldReturnCorrectTotal() {
            List<OrderItem> items = Arrays.asList(
                new OrderItem(1, 2),  // 2 * 7.99 = 15.98
                new OrderItem(7, 5)   // 5 * 3.14 = 15.70
            );
            when(productRepository.findById(1))
                .thenReturn(Optional.of(new Product(1, "Dune", "book", 7.99, 31)));
            when(productRepository.findById(7))
                .thenReturn(Optional.of(new Product(7, "Abo", "pokemon", 3.14, 146)));

            double total = orderService.calculateTotal(items);

            assertEquals(31.68, total);
        }

        @Test
        @DisplayName("Should deduct inventory after order")
        void deductInventory_shouldUpdateProductQuantities() {
            List<OrderItem> items = Arrays.asList(new OrderItem(1, 2));
            Product product = new Product(1, "Product", "book", 10.0, 10);
            when(productRepository.findById(1)).thenReturn(Optional.of(product));

            orderService.deductInventory(items);

            verify(productRepository).save(argThat(p ->
                p.getId().equals(1) && p.getQuantity().equals(8)
            ));
        }
    }

    @Nested
    @DisplayName("Product Service")
    class ProductServiceTests {

        @Test
        @DisplayName("Should validate price adjustment with valid parameters")
        void validatePriceAdjustment_withValidParams_shouldSucceed() {
            when(productRepository.findByCategory("book"))
                .thenReturn(Arrays.asList(new Product(1, "Book", "book", 10.0, 5)));

            ProductService.ValidationStatus result = productService.validatePriceAdjustment("book", 10.0);

            assertEquals(ProductService.ValidationStatus.VALID, result);
        }

        @Test
        @DisplayName("Should reject adjustment when category not found")
        void validatePriceAdjustment_withNonExistentCategory_shouldFail() {
            when(productRepository.findByCategory("electronics")).thenReturn(Collections.emptyList());

            ProductService.ValidationStatus result = productService.validatePriceAdjustment("electronics", 10.0);

            assertEquals(ProductService.ValidationStatus.CATEGORY_NOT_FOUND, result);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-100.0, -150.0})
        @DisplayName("Should reject percentage that results in negative prices")
        void validatePriceAdjustment_withInvalidPercentage_shouldFail(double percentage) {
            ProductService.ValidationStatus result = productService.validatePriceAdjustment("book", percentage);

            assertEquals(ProductService.ValidationStatus.INVALID_PERCENTAGE, result);
        }

        @Test
        @DisplayName("Should increase prices by positive percentage")
        void adjustCategoryPrices_withPositivePercentage_shouldIncreasePrices() {
            Product product = new Product(1, "Dune", "book", 7.99, 31);
            when(productRepository.findByCategory("book")).thenReturn(Arrays.asList(product));

            List<Product> result = productService.adjustCategoryPrices("book", 10.0);

            assertEquals(8.79, result.get(0).getPrice());
            verify(productRepository).save(argThat(p -> p.getPrice().equals(8.79)));
        }

        @Test
        @DisplayName("Should decrease prices by negative percentage")
        void adjustCategoryPrices_withNegativePercentage_shouldDecreasePrices() {
            Product product = new Product(1, "Product", "book", 10.0, 5);
            when(productRepository.findByCategory("book")).thenReturn(Arrays.asList(product));

            List<Product> result = productService.adjustCategoryPrices("book", -20.0);

            assertEquals(8.0, result.get(0).getPrice());
        }

        @Test
        @DisplayName("Should round prices to two decimal places")
        void adjustCategoryPrices_shouldRoundCorrectly() {
            Product product = new Product(1, "Product", "book", 7.99, 10);
            when(productRepository.findByCategory("book")).thenReturn(Arrays.asList(product));

            List<Product> result = productService.adjustCategoryPrices("book", 10.0);

            assertEquals(8.79, result.get(0).getPrice()); // 7.99 * 1.10 = 8.789 → 8.79
        }
    }
}
