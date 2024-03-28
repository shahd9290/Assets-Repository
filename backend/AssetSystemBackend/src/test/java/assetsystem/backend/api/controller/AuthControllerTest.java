package assetsystem.backend.api.controller;

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
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate template;

    private final Map<String, Object> payload = new HashMap<>();

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

    @AfterEach
    public void deleteUsers() {

        template.execute("DELETE FROM user_roles WHERE user_id = (SELECT id FROM users WHERE username = 'mockuser')");
        template.execute("DELETE FROM users WHERE username = 'mockuser';");
    }

    @Test
    public void createUser() throws Exception {
        payload.put("username", "mockuser");
        payload.put("email", "mockemail@test.com");
        payload.put("password", "mockpassword");
        payload.put("role", new String[]{"user"});

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully"));
    }

    @Test
    public void createAdmin() throws Exception {
        payload.put("username", "mockuser");
        payload.put("email", "mockemail@test.com");
        payload.put("password", "mockpassword");
        payload.put("role", new String[]{"admin"});

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully"));
    }

    @Test
    public void createViewer() throws Exception {
        payload.put("username", "mockuser");
        payload.put("email", "mockemail@test.com");
        payload.put("password", "mockpassword");
        payload.put("role", new String[]{"viewer"});

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully"));
    }

    @Test
    public void createExists() throws Exception {
        payload.put("username", "mockuser");
        payload.put("email", "mockemail@test.com");
        payload.put("password", "mockpassword");
        payload.put("role", new String[]{"admin"});

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Username is already taken!"));

        payload.replace("username", "mockuser2");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Email already in use!"));
    }

    @Test
    public void testSignInUser() throws Exception {
        createUser();

        payload.put("username", "mockuser");
        payload.put("password", "mockpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignAdmin() throws Exception {
        createAdmin();

        payload.put("username", "mockuser");
        payload.put("password", "mockpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignViewer() throws Exception {
        createViewer();

        payload.put("username", "mockuser");
        payload.put("password", "mockpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignInFail() throws Exception {
        createUser();

        payload.put("username", "mockuser");
        payload.put("password", "wrongpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        // Header is required to run the test, not required in production.
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());
    }

}
