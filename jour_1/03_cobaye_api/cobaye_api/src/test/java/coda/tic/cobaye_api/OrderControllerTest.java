package coda.tic.cobaye_api;

import coda.tic.cobaye_api.repository.OrderRepository;
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
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataLoader testDataLoader;

    @Autowired
    private OrderRepository orderRepository;

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

    // add your tests here

}
