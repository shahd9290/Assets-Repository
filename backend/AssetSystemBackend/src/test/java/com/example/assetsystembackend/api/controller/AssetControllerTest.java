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

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.assetsystembackend.api.controller.AssetController.*;
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

    private String date;

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

    @BeforeAll
    public void createTestType() {
        template.execute("CREATE TABLE IF NOT EXISTS test (id INT PRIMARY KEY, test1 VARCHAR(255));");
        date = String.valueOf(LocalDate.now());
    }

    @AfterAll
    public void deleteTestType() {
        template.execute("DROP TABLE IF EXISTS test;");
        // Reset the IDs to not disturb the auto-generated sequence. DATA WILL BE LOST.
        template.execute("DROP TABLE IF EXISTS assets_relations;");
        template.execute("DROP TABLE IF EXISTS assets;");
    }

    @BeforeEach
    public void assetPayload() {
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

    private void deletePayload(int num) {
        payload = new HashMap<>();
        payload.put("id", num);
    }

    @Test
    public void testAddNormal() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string(SUCCESS_MSG));

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

    @Test
    public void testAddInvalidTypeOne() throws Exception {
        HashMap<String, Object> assetPayload = (HashMap<String, Object>) payload.get("asset");
        assetPayload.put("type", "invalid");
        payload.replace("asset", assetPayload);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_TYPE_MSG + "\nEnsure the Type exists."));
    }

    @Test
    public void testAddInvalidTypeTwo() throws Exception {
        HashMap<String, Object> typePayload = new HashMap<>();
        typePayload.put("test4", "nonexist");
        payload.replace("type", typePayload);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_TYPE_MSG + "\nEnsure the Type contains the specified columns."));
    }

    @Test
    public void testAddMissingKey() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload.get("asset"))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MISSING_DATA_MSG));

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload.get("type"))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MISSING_DATA_MSG));
    }

    @Test
    public void testAddParentOne() throws Exception {
        testAddNormal();

        HashMap<String, Object> assetPayload = (HashMap<String, Object>) payload.get("asset");
        assetPayload.put("parent_id", 1);
        assetPayload.put("relation_type", "Test");
        payload.replace("asset", assetPayload);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string(SUCCESS_MSG));
    }

    @Test
    public void testAddParentTwo() throws Exception {
        HashMap<String, Object> assetPayload = (HashMap<String, Object>) payload.get("asset");
        assetPayload.put("parent_id", 1);
        payload.replace("asset", assetPayload);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(RELATION_MSG));

        assetPayload.remove("parent_id");
        assetPayload.put("relation_type", "Test");
        payload.replace("asset", assetPayload);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(RELATION_MSG));

    }

    @Test
    public void testAddParentThree() throws Exception {
        HashMap<String, Object> assetPayload = (HashMap<String, Object>) payload.get("asset");
        assetPayload.put("parent_id", 15);
        assetPayload.put("relation_type", "Test");
        payload.replace("asset", assetPayload);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-new-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_ID_MSG));
    }

    @Test
    public void testDeleteOne() throws Exception {
        testAddNormal();
        deletePayload(2);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string(REMOVAL_MSG));

    }

    @Test
    public void testDeleteTwo() throws Exception {
        testAddParentOne();
        // asset id 1 should still be in the table from previous requests
        deletePayload(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(DEPENDENCY_MSG));

    }

    @Test
    public void testDeleteThree() throws Exception {
        testAddNormal();
        deletePayload(40);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_ID_MSG));

    }

    @Test
    public void testDeleteFour() throws Exception {
        //empty
        HashMap<String, Object> delPayload = new HashMap<>();

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-asset")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(delPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MISSING_DATA_MSG + "(Missing Asset ID)"));

    }

    @Test
    public void testSearchOne() throws Exception {
        //empty request
        HashMap<String, Object> searchPayload = new HashMap<>();

        mockMvc.perform(MockMvcRequestBuilders.post("/search")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPayload)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testSearchTwo() throws Exception {
        testAddNormal();
        //empty request but returns an asset
        HashMap<String, Object> searchPayload = new HashMap<>();
        searchPayload.put("search_term", "test.txt");
        searchPayload.put("date_before", "2030-01-01");
        searchPayload.put("user", "John Doe");

        mockMvc.perform(MockMvcRequestBuilders.post("/search")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPayload)))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"parent_id\":null,\"name\":\"test.txt\",\"link\":null,\"description\":null," +
                        "\"creator_name\":\"John Doe\",\"id\":1,\"creation_date\":\""+date+"\",\"relation_type\":null," +
                        "\"type\":\"test\",\"test1\":\"test\"}" +
                        "]"));
    }

    @Test
    public void testSearchThree() throws Exception {
        testAddParentOne();
        //empty request but returns an asset
        HashMap<String, Object> searchPayload = new HashMap<>();
        searchPayload.put("parent_id", 1);

        mockMvc.perform(MockMvcRequestBuilders.post("/search")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPayload)))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"parent_id\":1,\"name\":\"test.txt\",\"link\":null,\"description\":null," +
                        "\"creator_name\":\"John Doe\",\"id\":4,\"creation_date\":\""+date+"\",\"relation_type\":\"Test\",\"type\":\"test\"," +
                        "\"test1\":\"test\"}," +
                        "{\"parent_id\":1,\"name\":\"test.txt\",\"link\":null,\"description\":null,\"creator_name\":\"John Doe\",\"id\":6," +
                        "\"creation_date\":\""+date+"\",\"relation_type\":\"Test\",\"type\":\"test\",\"test1\":\"test\"}" +
                        "]"));
    }

    @Test
    public void testSearchFour() throws Exception {
        //empty request
        HashMap<String, Object> searchPayload = new HashMap<>();
        searchPayload.put("type", "testing");
        searchPayload.put("date_after", "3000-01-01");
        mockMvc.perform(MockMvcRequestBuilders.post("/search")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchPayload)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testAssetLog() throws Exception {
        testAddNormal();

        mockMvc.perform(MockMvcRequestBuilders.get("/audit/log/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
