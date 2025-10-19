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

    @Test
    void testGetUserByIdValid() throws Exception {
        String response = mockMvc.perform(get("/api/users/1")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                {
                    "id": 1,
                    "name": "admin_1",
                    "permissions": ["admin"]
                }
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    void testGetUserByIdInvalid() throws Exception {
        mockMvc.perform(get("/api/users/9999")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUserAsAdmin() throws Exception {
        String response = mockMvc.perform(post("/api/users")
                .with(httpBasic("admin_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"newuser\",\"permissions\":[\"read\"]}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                {
                    "name": "newuser",
                    "permissions": ["read"]
                }
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);

        // Extract ID from response and verify in repository
        Integer createdId = com.jayway.jsonpath.JsonPath.read(response, "$.id");
        var createdUser = userRepository.findById(createdId).orElseThrow();
        assertEquals("newuser", createdUser.getName());
        assertEquals(1, createdUser.getPermissions().size());
        assertEquals("read", createdUser.getPermissions().get(0));
    }

    @Test
    void testCreateUserAsNonAdmin() throws Exception {
        mockMvc.perform(post("/api/users")
                .with(httpBasic("client_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"newuser\",\"permissions\":[\"read\"]}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateUserAsAdmin() throws Exception {
        String response = mockMvc.perform(put("/api/users/1")
                .with(httpBasic("admin_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"admin_1_updated\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expected = """
                {
                    "id": 1,
                    "name": "admin_1_updated",
                    "permissions": ["admin"]
                }
                """;

        JSONAssert.assertEquals(expected, response, JSONCompareMode.LENIENT);

        // Verify update in repository
        var updatedUser = userRepository.findById(1).get();
        assertEquals(1, updatedUser.getId());
        assertEquals("admin_1_updated", updatedUser.getName());
        assertEquals(1, updatedUser.getPermissions().size());
        assertEquals("admin", updatedUser.getPermissions().get(0));
    }

    @Test
    void testUpdateUserAsNonAdmin() throws Exception {
        mockMvc.perform(put("/api/users/1")
                .with(httpBasic("client_1", ""))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"admin_1_updated\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteUserAsAdmin() throws Exception {
        assertTrue(userRepository.existsById(1));

        mockMvc.perform(delete("/api/users/1")
                .with(httpBasic("admin_1", "")))
                .andExpect(status().isNoContent());

        // Verify deletion in repository
        assertFalse(userRepository.existsById(1));
    }

    @Test
    void testDeleteUserAsNonAdmin() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                .with(httpBasic("client_1", "")))
                .andExpect(status().isForbidden());

        // Verify user still exists
        assertTrue(userRepository.existsById(1));
    }
}
