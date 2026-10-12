package com.dmoney.dmoney;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exports the generated OpenAPI spec to build/openapi/openapi.json.
 *
 * <p>Runs the full Spring context against in-memory H2 (see
 * src/test/resources/application.yml) with no web server and no external
 * database, then serializes {@code GET /v3/api-docs}. Used by
 * dmoney_platform's contract-sync workflow.
 *
 * <p>Disabled unless {@code -Dopenapi.export=true} is supplied, so it never
 * runs as part of the normal test suite.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@EnabledIfSystemProperty(named = "openapi.export", matches = "true")
class OpenApiExportTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exportOpenApi() throws Exception {
        String spec = mockMvc.perform(get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Path output = Path.of(System.getProperty("openapi.output", "build/openapi/openapi.json"));
        Files.createDirectories(output.getParent());
        Files.writeString(output, spec);
    }
}
