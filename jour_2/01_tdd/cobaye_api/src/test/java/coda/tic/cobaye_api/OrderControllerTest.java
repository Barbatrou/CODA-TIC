package coda.tic.cobaye_api;

import coda.tic.cobaye_api.repository.OrderRepository;
import coda.tic.cobaye_api.repository.ProductRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataLoader testDataLoader;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() throws Exception {
        testDataLoader.resetData();
    }

    @Test
    void testGetAllOrders() throws Exception {
        String response = mockMvc.perform(get("/api/orders")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                [
                    {
                        "id": 1,
                        "user_id": 3,
                        "products": [
                            {"productId": 1, "quantity": 1},
                            {"productId": 4, "quantity": 1},
                            {"productId": 7, "quantity": 2}
                        ],
                        "total": 14152.27
                    },
                    {
                        "id": 2,
                        "user_id": 4,
                        "products": [
                            {"productId": 1, "quantity": 1},
                            {"productId": 4, "quantity": 1},
                            {"productId": 7, "quantity": 2}
                        ],
                        "total": 14152.27
                    }
                ]
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    void testGetOrdersByUserId() throws Exception {
        String response = mockMvc.perform(get("/api/orders?user_id=3")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                [
                    {
                        "id": 1,
                        "user_id": 3,
                        "products": [
                            {"productId": 1, "quantity": 1},
                            {"productId": 4, "quantity": 1},
                            {"productId": 7, "quantity": 2}
                        ],
                        "total": 14152.27
                    }
                ]
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    void testGetOrderByIdValid() throws Exception {
        String response = mockMvc.perform(get("/api/orders/1")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                {
                    "id": 1,
                    "user_id": 3,
                    "products": [
                        {"productId": 1, "quantity": 1},
                        {"productId": 4, "quantity": 1},
                        {"productId": 7, "quantity": 2}
                    ],
                    "total": 14152.27
                }
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    void testGetOrderByIdInvalid() throws Exception {
        mockMvc.perform(get("/api/orders/9999")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isNotFound());
    }


    @Test
    void testUpdateOrderAsAdmin() throws Exception {
        String response = mockMvc.perform(put("/api/orders/1")
                .with(httpBasic("admin_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"total\":299.99}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                {
                    "id": 1,
                    "user_id": 3,
                    "products": [
                        {"productId": 1, "quantity": 1},
                        {"productId": 4, "quantity": 1},
                        {"productId": 7, "quantity": 2}
                    ],
                    "total": 299.99
                }
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);

        // Verify update in repository
        var updatedOrder = orderRepository.findById(1).get();
        assertEquals(1, updatedOrder.getId());
        assertEquals(3, updatedOrder.getUserId());
        assertEquals(299.99, updatedOrder.getTotal());
        assertEquals(3, updatedOrder.getProducts().size());
    }

    @Test
    void testUpdateOrderAsNonAdmin() throws Exception {
        mockMvc.perform(put("/api/orders/1")
                .with(httpBasic("client_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"total\":299.99}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteOrderAsAdmin() throws Exception {
        assertTrue(orderRepository.existsById(1));

        mockMvc.perform(delete("/api/orders/1")
                .with(httpBasic("admin_1", "")))
                .andExpect(status().isNoContent());

        // Verify deletion in repository
        assertFalse(orderRepository.existsById(1));
    }

    @Test
    void testDeleteOrderAsNonAdmin() throws Exception {
        mockMvc.perform(delete("/api/orders/1")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isForbidden());

        // Verify order still exists
        assertTrue(orderRepository.existsById(1));
    }

    @Nested
    @DisplayName("POST /api/orders - Create Order")
    class CreateOrderTests {

    }
}
