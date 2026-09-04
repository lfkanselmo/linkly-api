package com.linkly.infrastructure.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.linkly.TestcontainersConfiguration;
import com.linkly.application.dto.ShortenResponse;

import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ShortenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void shortensAValidUrl() throws Exception {
        String body = mockMvc.perform(shortenRequest("{\"originalUrl\": \"https://example.com/page\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ShortenResponse response = jsonMapper.readValue(body, ShortenResponse.class);
        assertThat(response.shortCode()).isNotBlank();
        assertThat(response.shortUrl()).endsWith(response.shortCode());
    }

    @Test
    void honoursCustomCode() throws Exception {
        String customCode = "custom" + UUID.randomUUID().toString().substring(0, 6);

        mockMvc.perform(shortenRequest(
                        "{\"originalUrl\": \"https://example.com/custom\", \"customCode\": \"%s\"}".formatted(customCode)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value(customCode));
    }

    @Test
    void rejectsDuplicateCustomCode() throws Exception {
        String customCode = "dup" + UUID.randomUUID().toString().substring(0, 8);
        mockMvc.perform(shortenRequest(
                "{\"originalUrl\": \"https://example.com/first\", \"customCode\": \"%s\"}".formatted(customCode)));

        mockMvc.perform(shortenRequest(
                        "{\"originalUrl\": \"https://example.com/second\", \"customCode\": \"%s\"}".formatted(customCode)))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsNonHttpUrl() throws Exception {
        mockMvc.perform(shortenRequest("{\"originalUrl\": \"ftp://example.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsSelfReferentialUrlRegardlessOfCase() throws Exception {
        mockMvc.perform(shortenRequest("{\"originalUrl\": \"HTTP://LOCALHOST:8080/x\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsReservedCustomCode() throws Exception {
        mockMvc.perform(shortenRequest("{\"originalUrl\": \"https://example.com/reserved\", \"customCode\": \"actuator\"}"))
                .andExpect(status().isBadRequest());
    }

    private MockHttpServletRequestBuilder shortenRequest(String body) {
        return post("/api/v1/urls").contentType(MediaType.APPLICATION_JSON).content(body);
    }
}
