package coda.tic.cobaye_api;

import coda.tic.cobaye_api.repository.UserRepository;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataLoader testDataLoader;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        testDataLoader.resetData();
    }

    @Test
    void testGetAllUsersAuthenticated() throws Exception {
        String response = mockMvc.perform(get("/api/users")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                [
                    {"id": 1, "name": "admin_1", "permissions": ["admin"]},
                    {"id": 2, "name": "admin_2", "permissions": ["admin", "client"]},
                    {"id": 3, "name": "client_1", "permissions": ["client"]},
                    {"id": 4, "name": "client_2", "permissions": ["client"]}
                ]
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);
    }
}
