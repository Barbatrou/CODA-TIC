package coda.tic.cobaye_api.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JGivenStage
public class ThenStage extends Stage<ThenStage> {

    @ExpectedScenarioState
    private MvcResult result;

    public ThenStage then_the_status_should_be_$(int expectedStatus) {
        assertEquals(expectedStatus, result.getResponse().getStatus());
        return self();
    }

    public ThenStage then_the_response_body_should_be_$(String expectedJson) throws Exception {
        String actualResponse = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualResponse, JSONCompareMode.LENIENT);
        return self();
    }

    public ThenStage then_the_response_body_should_contain_$(String expectedJson) throws Exception {
        String actualResponse = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualResponse, JSONCompareMode.LENIENT);
        return self();
    }
}
