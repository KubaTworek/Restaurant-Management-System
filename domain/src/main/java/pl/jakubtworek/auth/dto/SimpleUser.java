package pl.jakubtworek.auth.dto;

public class SimpleUser {
    private Long id;
    private String username;

    public SimpleUser(final Long id, final String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
