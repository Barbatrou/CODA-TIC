package coda.tic.cobaye_api;

import coda.tic.cobaye_api.repository.ProductRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
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

    // add you tests here
}
