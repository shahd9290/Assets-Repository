package com.example.assetsystembackend.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate template;

    private String token;

    @Value("${jwt.secret}")
    private String secret;
    @BeforeEach
    public void generateToken() {
        token = Jwts.builder()
                .setSubject("admin")
                .setExpiration(new Date((new Date()).getTime() + 10000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Test
    public void testAddTypeEndpoint() throws Exception {
        //Delete table
        template.execute("DROP TABLE IF EXISTS test_table;");
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("table_name", "test_table");
        payload.put("columns", List.of("column1", "column2"));

        String payloadJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(MockMvcRequestBuilders.post("/type/add-type")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Data added successfully"));

        //Delete table
        template.execute("DROP TABLE IF EXISTS test_table;");
    }

    @BeforeEach
    public void createType () {
        template.execute("CREATE TABLE IF NOT EXISTS test_table (column1 VARCHAR(255), column2 VARCHAR(255));");
    }

    @AfterEach
    public void deleteType () {
        template.execute("DROP TABLE IF EXISTS test_table;");
    }

    @Test
    public void testAddTypeEndpointDuplicate() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("table_name", "test_table");
        payload.put("columns", List.of("column1", "column2"));

        String payloadJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(MockMvcRequestBuilders.post("/type/add-type")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Table 'test_table' already exists."))
                .andReturn();

    }

    @Test
    public void testGetTypeEndpoint() throws Exception {
        String tableName = "users"; // Assuming a table with this name exists

        mockMvc.perform(MockMvcRequestBuilders.get("/type/get-type-data/{name}", tableName)
                    .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void testDeleteTypeEndpoint() throws Exception {
        template.execute("INSERT INTO test_table(column1) VALUES ('testSubject1'), ('testSubject2'), ('testSubject3');");

        Map<String, Object> payload = new HashMap<>();
        payload.put("table_name", "test_table");

        String payloadJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(MockMvcRequestBuilders.delete("/type/delete-type")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Data removed successfully"));


    }

    @Test
    public void testGetColumns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/type/get-columns/test_table")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/type/get-columns/other")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}
