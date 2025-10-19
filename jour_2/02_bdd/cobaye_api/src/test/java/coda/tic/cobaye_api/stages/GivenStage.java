package coda.tic.cobaye_api.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@JGivenStage
public class GivenStage extends Stage<GivenStage> {

    @ExpectedScenarioState
    private MockMvc mockMvc;

    @ProvidedScenarioState
    private String username;

    public GivenStage given_I_am_authenticated_as_$(String user) {
        this.username = user;
        return self();
    }

    public GivenStage given_I_am_not_authenticated() {
        this.username = null;
        return self();
    }
}
