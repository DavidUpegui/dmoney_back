package com.dmoney.dmoney.shared.security;

import com.dmoney.dmoney.auth.infrastructure.security.JwtTokenGenerator;
import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenGenerator tokenGenerator;

    private String validToken;

    @BeforeEach
    void setUp() {
        validToken = tokenGenerator.generate(UserId.newId());
    }

    @Test
    void should_return_401_when_no_jwt() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_return_401_when_invalid_jwt() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_return_401_when_malformed_header() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Basic " + validToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_authenticate_with_valid_jwt() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }
}
