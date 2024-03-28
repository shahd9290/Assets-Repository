package assetsystem.backend.api.model;

/**
 * Represents the different possible roles that a user may have.
 */
public enum ERole {

    ROLE_VIEWER("ROLE_VIEWER"),
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String str;

    private ERole(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
