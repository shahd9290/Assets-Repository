package com.example.assetsystembackend.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssetControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate template;

    private Map<String, Object> payload;

    private String token;

    @Value("${jwt.secret}")
    private String secret;

    @BeforeEach
    public void generateToken() {
        token = Jwts.builder()
                .setSubject("administrator")
                .setExpiration(new Date((new Date()).getTime() + 10000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @BeforeEach
    public void setUpPayload() {
        Map<String, Object> assetPayload = new HashMap<>();
        assetPayload.put("name", "test.txt");
        assetPayload.put("creatorname", "John Doe");
        assetPayload.put("type", "test");

        Map<String, Object> typePayload = new HashMap<>();
        typePayload.put("test1", "test");

        payload = new HashMap<>();
        payload.put("asset", assetPayload);
        payload.put("type", typePayload);
    }

    @BeforeAll
    public void createTestType() {
        template.execute("CREATE TABLE IF NOT EXISTS test (id INT PRIMARY KEY, test1 VARCHAR(255));");
    }

    @AfterAll
    public void deleteTestType() {
        template.execute("DROP TABLE IF EXISTS test;");
        // Reset the IDs to not disturb the auto-generated sequence. DATA WILL BE LOST.
        template.execute("DROP TABLE IF EXISTS assets_relations;");
        template.execute("DROP TABLE IF EXISTS assets;");


    }

    @Test
    public void testAddNormal() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Insertion successful!"));

    }

    @Test
    public void testAddDescLink() throws Exception {

        HashMap<String, Object> assetPayload = (HashMap<String, Object>) payload.get("asset");
        assetPayload.put("description", "Test Description");
        assetPayload.put("link", "http://test.com/");
        payload.replace("asset", assetPayload);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Insertion successful!"));

    }

}
