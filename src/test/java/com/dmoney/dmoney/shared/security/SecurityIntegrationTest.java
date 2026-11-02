package com.dmoney.dmoney.shared.security;

import com.dmoney.dmoney.auth.infrastructure.security.JwtTokenGenerator;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    private static final String TEST_SECRET = "test-secret-key-test-secret-key-test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenGenerator tokenGenerator;

    @Autowired
    private TagRepository tagRepository;

    private String validToken;

    @BeforeEach
    void setUp() {
        validToken = tokenGenerator.generate(UserId.newId());
    }

    @Test
    void sys01_returns401WithApiErrorWhenNoJwt() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Unauthorized"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void sys01_returns401WithApiErrorWhenTokenIsInvalid() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void sys01_returns401WithApiErrorWhenHeaderIsMalformed() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Basic " + validToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void sys01_returns401ForMovementsWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/movements"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void sys01_returns401WhenTokenIsExpired() throws Exception {
        String expired = Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis() - 7_200_000))
                .expiration(new Date(System.currentTimeMillis() - 3_600_000))
                .signWith(Keys.hmacShaKeyFor(TEST_SECRET.getBytes()))
                .compact();

        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer " + expired))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void authenticatesWithValidJwt() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }

    @Test
    void sys03_returns400ForMalformedTagId() throws Exception {
        mockMvc.perform(get("/api/tags/not-a-uuid")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void sys03_returns400ForMalformedCategoryId() throws Exception {
        mockMvc.perform(get("/api/categories/not-a-uuid")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void sys03_returns400ForMalformedMovementId() throws Exception {
        mockMvc.perform(get("/api/movements/not-a-uuid")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void sys03_returns400WhenCategoryTypeIsInvalid() throws Exception {
        String body = "{\"name\":\"cat-" + UUID.randomUUID() + "\",\"type\":\"nope\"}";

        mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void sys03_returns400WhenTagNameIsTooLong() throws Exception {
        String body = "{\"name\":\"" + "a".repeat(256) + "\"}";

        mockMvc.perform(post("/api/tags")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void sys02_doesNotExposeAnotherUsersTag() throws Exception {
        UserId owner = UserId.newId();
        UserId other = UserId.newId();
        Tag tag = tagRepository.save(Tag.create(
                owner,
                TagName.from("owned-" + UUID.randomUUID()),
                TagDescription.empty()
        ));
        String ownerToken = tokenGenerator.generate(owner);
        String otherToken = tokenGenerator.generate(other);

        mockMvc.perform(get("/api/tags/" + tag.id().value())
                        .header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isNotFound());

        mockMvc.perform(patch("/api/tags/" + tag.id().value())
                        .header("Authorization", "Bearer " + otherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"stolen\"}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/tags/" + tag.id().value())
                        .header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/tags/" + tag.id().value())
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tags")
                        .header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void sys04_errorResponsesShareApiErrorShape() throws Exception {
        mockMvc.perform(get("/api/tags/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        mockMvc.perform(get("/api/tags/not-a-uuid")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void sys04_returns409WithApiErrorOnDuplicateTag() throws Exception {
        String body = "{\"name\":\"dup-" + UUID.randomUUID() + "\"}";

        mockMvc.perform(post("/api/tags")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tags")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
