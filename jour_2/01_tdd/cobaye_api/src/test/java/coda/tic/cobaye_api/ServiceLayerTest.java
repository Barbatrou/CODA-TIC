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

    }

    @Nested
    @DisplayName("Product Service")
    class ProductServiceTests {

    }
}
