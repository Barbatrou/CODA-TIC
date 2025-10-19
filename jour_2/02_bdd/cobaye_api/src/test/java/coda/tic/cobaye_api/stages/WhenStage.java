package coda.tic.cobaye_api.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@JGivenStage
public class WhenStage extends Stage<WhenStage> {

    @ExpectedScenarioState
    private MockMvc mockMvc;

    @ExpectedScenarioState
    private String username;

    @ProvidedScenarioState
    private MvcResult result;

    public WhenStage when_calling_$_endpoint_with_$_method(String endpoint, String method) throws Exception {
        result = performRequest(endpoint, method, null);
        return self();
    }

    public WhenStage when_calling_$_endpoint_with_$_method_with_body_$(String endpoint, String method, String body) throws Exception {
        result = performRequest(endpoint, method, body);
        return self();
    }

    private MvcResult performRequest(String endpoint, String method, String body) throws Exception {
        var requestBuilder = switch (method.toUpperCase()) {
            case "GET" -> get(endpoint);
            case "POST" -> post(endpoint);
            case "PUT" -> put(endpoint);
            case "DELETE" -> delete(endpoint);
            case "PATCH" -> patch(endpoint);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };

        if (username != null) {
            requestBuilder = requestBuilder.with(httpBasic(username, ""));
        }

        if (body != null) {
            requestBuilder = requestBuilder
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body);
        }

        return mockMvc.perform(requestBuilder).andReturn();
    }
}
