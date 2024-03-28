package assetsystem.backend.api.payload.response;

import java.util.List;

/**
 * Represents a response payload for JWT authentication, containing the access token, user ID, username, email, and roles.
 */
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;

    /**
     * Constructs a JwtResponse object with the provided access token, user ID, username, email, and role.
     *
     * @param accessToken The access token.
     * @param id The user ID.
     * @param username The username.
     * @param email The email.
     * @param role The role assigned to the user.
     */
    public JwtResponse(String accessToken, Long id, String username, String email,
                       String role) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
