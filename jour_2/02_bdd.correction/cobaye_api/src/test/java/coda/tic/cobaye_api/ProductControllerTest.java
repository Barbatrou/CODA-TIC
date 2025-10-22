package coda.tic.cobaye_api;

import coda.tic.cobaye_api.stages.GivenStage;
import coda.tic.cobaye_api.stages.ThenStage;
import coda.tic.cobaye_api.stages.WhenStage;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.EnableJGiven;
import com.tngtech.jgiven.integration.spring.junit5.SpringScenarioTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@EnableJGiven
class ProductControllerTest extends SpringScenarioTest<GivenStage, WhenStage, ThenStage> {

    @Autowired
    @ScenarioState
    private MockMvc mockMvc;

    @Autowired
    private TestDataLoader testDataLoader;

    @BeforeEach
    void setUp() throws Exception {
        testDataLoader.resetData();
    }

    @Test
    @DisplayName("GET /api/products - client can get all products")
    void testGetAllProducts() throws Exception {
        String expectedResponse = """
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

        given().given_I_am_authenticated_as_$("client_1");
        when().when_calling_$_endpoint_with_$_method("/api/products", "GET");
        then().then_the_status_should_be_$(200)
                .and().then_the_response_body_should_be_$(expectedResponse);
    }

    @Test
    @DisplayName("GET /api/products?category=book - client can filter products by category")
    void testGetProductsByCategory() throws Exception {
        String expectedResponse = """
                [
                    {"id": 1, "name": "Dune", "category": "book", "price": 7.99, "quantity": 31},
                    {"id": 2, "name": "The Witcher", "category": "book", "price": 8.16, "quantity": 14},
                    {"id": 3, "name": "Game of Throne: complete mega deluxe edition", "category": "book", "price": 99.69, "quantity": 2}
                ]
                """;

        given().given_I_am_authenticated_as_$("client_1");
        when().when_calling_$_endpoint_with_$_method("/api/products?category=book", "GET");
        then().then_the_status_should_be_$(200)
                .and().then_the_response_body_should_be_$(expectedResponse);
    }

    @Test
    @DisplayName("GET /api/products/1 - client can get product by ID")
    void testGetProductByIdValid() throws Exception {
        String expectedResponse = """
                {
                    "id": 1,
                    "name": "Dune",
                    "category": "book",
                    "price": 7.99,
                    "quantity": 31
                }
                """;

        given().given_I_am_authenticated_as_$("client_1");
        when().when_calling_$_endpoint_with_$_method("/api/products/1", "GET");
        then().then_the_status_should_be_$(200)
                .and().then_the_response_body_should_be_$(expectedResponse);
    }

    @Test
    @DisplayName("GET /api/products/9999 - returns 404 when product not found")
    void testGetProductByIdInvalid() throws Exception {
        given().given_I_am_authenticated_as_$("client_1");
        when().when_calling_$_endpoint_with_$_method("/api/products/9999", "GET");
        then().then_the_status_should_be_$(404);
    }

    @Test
    @DisplayName("POST /api/products - admin can create product")
    void testCreateProductAsAdmin() throws Exception {
        String requestBody = """
                {
                    "name": "New Product",
                    "category": "test",
                    "price": 99.99,
                    "quantity": 10
                }
                """;

        String expectedResponse = """
                {
                    "name": "New Product",
                    "category": "test",
                    "price": 99.99,
                    "quantity": 10
                }
                """;

        given().given_I_am_authenticated_as_$("admin_1");
        when().when_calling_$_endpoint_with_$_method_with_body_$("/api/products", "POST", requestBody);
        then().then_the_status_should_be_$(201)
                .and().then_the_response_body_should_contain_$(expectedResponse);
    }

    @Test
    @DisplayName("POST /api/products - client cannot create product (403 Forbidden)")
    void testCreateProductAsNonAdmin() throws Exception {
        String requestBody = """
                {
                    "name": "New Product",
                    "category": "test",
                    "price": 99.99,
                    "quantity": 10
                }
                """;

        given().given_I_am_authenticated_as_$("client_1");
        when().when_calling_$_endpoint_with_$_method_with_body_$("/api/products", "POST", requestBody);
        then().then_the_status_should_be_$(403);
    }

    @Test
    @DisplayName("PUT /api/products/1 - admin can update product")
    void testUpdateProductAsAdmin() throws Exception {
        String requestBody = """
                {
                    "name": "Updated Product",
                    "price": 199.99
                }
                """;

        String expectedResponse = """
                {
                    "id": 1,
                    "name": "Updated Product",
                    "category": "book",
                    "price": 199.99,
                    "quantity": 31
                }
                """;

        given().given_I_am_authenticated_as_$("admin_1");
        when().when_calling_$_endpoint_with_$_method_with_body_$("/api/products/1", "PUT", requestBody);
        then().then_the_status_should_be_$(200)
                .and().then_the_response_body_should_be_$(expectedResponse);
    }

    @Test
    @DisplayName("PUT /api/products/1 - client cannot update product (403 Forbidden)")
    void testUpdateProductAsNonAdmin() throws Exception {
        String requestBody = """
                {
                    "name": "Updated Product"
                }
                """;

        given().given_I_am_authenticated_as_$("client_1");
        when().when_calling_$_endpoint_with_$_method_with_body_$("/api/products/1", "PUT", requestBody);
        then().then_the_status_should_be_$(403);
    }

    @Test
    @DisplayName("DELETE /api/products/1 - admin can delete product")
    void testDeleteProductAsAdmin() throws Exception {
        given().given_I_am_authenticated_as_$("admin_1");
        when().when_calling_$_endpoint_with_$_method("/api/products/1", "DELETE");
        then().then_the_status_should_be_$(204);
    }

    @Test
    @DisplayName("DELETE /api/products/1 - client cannot delete product (403 Forbidden)")
    void testDeleteProductAsNonAdmin() throws Exception {
        given().given_I_am_authenticated_as_$("client_1");
        when().when_calling_$_endpoint_with_$_method("/api/products/1", "DELETE");
        then().then_the_status_should_be_$(403);
    }

    @Test
    @DisplayName("PATCH /api/products/category/book/price?percentage=10 - admin can adjust prices")
    void testAdjustPricesAsAdmin() throws Exception {
        given().given_I_am_authenticated_as_$("admin_1");
        when().when_calling_$_endpoint_with_$_method("/api/products/category/book/price?percentage=10", "PATCH");
        then().then_the_status_should_be_$(200);
    }

    @Test
    @DisplayName("PATCH /api/products/category/book/price?percentage=10 - client cannot adjust prices (403 Forbidden)")
    void testAdjustPricesAsNonAdmin() throws Exception {
        given().given_I_am_authenticated_as_$("client_1");
        when().when_calling_$_endpoint_with_$_method("/api/products/category/book/price?percentage=10", "PATCH");
        then().then_the_status_should_be_$(403);
    }

    @Test
    @DisplayName("PATCH /api/products/category/electronics/price?percentage=10 - returns 404 when category not found")
    void testAdjustPricesCategoryNotFound() throws Exception {
        given().given_I_am_authenticated_as_$("admin_1");
        when().when_calling_$_endpoint_with_$_method("/api/products/category/electronics/price?percentage=10", "PATCH");
        then().then_the_status_should_be_$(404);
    }

    @Test
    @DisplayName("PATCH /api/products/category/book/price?percentage=-100 - returns 400 when percentage is invalid")
    void testAdjustPricesInvalidPercentage() throws Exception {
        given().given_I_am_authenticated_as_$("admin_1");
        when().when_calling_$_endpoint_with_$_method("/api/products/category/book/price?percentage=-100", "PATCH");
        then().then_the_status_should_be_$(400);
    }
}
