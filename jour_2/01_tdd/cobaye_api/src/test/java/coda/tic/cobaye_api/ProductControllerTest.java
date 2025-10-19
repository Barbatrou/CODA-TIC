package coda.tic.cobaye_api;

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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataLoader testDataLoader;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() throws Exception {
        testDataLoader.resetData();
    }

    @Test
    void testGetAllProducts() throws Exception {
        String response = mockMvc.perform(get("/api/products")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                [
                    {"id": 1, "name": "Dune", "category": "book", "price": 7.99, "quantity": 31},
                    {"id": 2, "name": "The Witcher", "category": "book", "price": 8.16, "quantity": 14},
                    {"id": 3, "name": "Game of Throne: complete mega deluxe edition", "category": "book", "price": 99.69, "quantity": 2},
                    {"id": 4, "name": "Dacia Sandero", "category": "car", "price": 14138, "quantity": 1},
                    {"id": 5, "name": "Fiat Panda", "category": "car", "price": 27040, "quantity": 1},
                    {"id": 6, "name": "Pikachu", "category": "pokemon", "price": 25.25, "quantity": 1},
                    {"id": 7, "name": "Abo", "category": "pokemon", "price": 3.14, "quantity": 146},
                    {"id": 8, "name": "nosferapti", "category": "pokemon", "price": 0.98, "quantity": 367}
                ]
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    void testGetProductsByCategory() throws Exception {
        String response = mockMvc.perform(get("/api/products?category=book")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                [
                    {"id": 1, "name": "Dune", "category": "book", "price": 7.99, "quantity": 31},
                    {"id": 2, "name": "The Witcher", "category": "book", "price": 8.16, "quantity": 14},
                    {"id": 3, "name": "Game of Throne: complete mega deluxe edition", "category": "book", "price": 99.69, "quantity": 2}
                ]
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    void testGetProductByIdValid() throws Exception {
        String response = mockMvc.perform(get("/api/products/1")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                {
                    "id": 1,
                    "name": "Dune",
                    "category": "book",
                    "price": 7.99,
                    "quantity": 31
                }
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    void testGetProductByIdInvalid() throws Exception {
        mockMvc.perform(get("/api/products/9999")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProductAsAdmin() throws Exception {
        String response = mockMvc.perform(post("/api/products")
                .with(httpBasic("admin_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\",\"category\":\"test\",\"price\":99.99,\"quantity\":10}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                {
                    "name": "New Product",
                    "category": "test",
                    "price": 99.99,
                    "quantity": 10
                }
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);

        // Extract ID from response and verify in repository
        Integer createdId = com.jayway.jsonpath.JsonPath.read(response, "$.id");
        var createdProduct = productRepository.findById(createdId).orElseThrow();
        assertEquals("New Product", createdProduct.getName());
        assertEquals("test", createdProduct.getCategory());
        assertEquals(99.99, createdProduct.getPrice());
        assertEquals(10, createdProduct.getQuantity());
    }

    @Test
    void testCreateProductAsNonAdmin() throws Exception {
        mockMvc.perform(post("/api/products")
                .with(httpBasic("client_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\",\"category\":\"test\",\"price\":99.99,\"quantity\":10}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateProductAsAdmin() throws Exception {
        String response = mockMvc.perform(put("/api/products/1")
                .with(httpBasic("admin_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Product\",\"price\":199.99}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                {
                    "id": 1,
                    "name": "Updated Product",
                    "category": "book",
                    "price": 199.99,
                    "quantity": 31
                }
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);

        // Verify update in repository
        var updatedProduct = productRepository.findById(1).get();
        assertEquals(1, updatedProduct.getId());
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("book", updatedProduct.getCategory());
        assertEquals(199.99, updatedProduct.getPrice());
        assertEquals(31, updatedProduct.getQuantity());
    }

    @Test
    void testUpdateProductAsNonAdmin() throws Exception {
        mockMvc.perform(put("/api/products/1")
                .with(httpBasic("client_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Product\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteProductAsAdmin() throws Exception {
        assertTrue(productRepository.existsById(1));

        mockMvc.perform(delete("/api/products/1")
                .with(httpBasic("admin_1", "")))
                .andExpect(status().isNoContent());

        // Verify deletion in repository
        assertFalse(productRepository.existsById(1));
    }

    @Test
    void testDeleteProductAsNonAdmin() throws Exception {
        mockMvc.perform(delete("/api/products/1")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isForbidden());

        // Verify product still exists
        assertTrue(productRepository.existsById(1));
    }

    @Nested
    @DisplayName("PATCH /api/products/category/{category}/price - Adjust Prices")
    class AdjustCategoryPricesTests {

    }
}
