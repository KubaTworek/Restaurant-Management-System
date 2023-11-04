package pl.jakubtworek.auth.dto;

public class SimpleUserSnapshot {
    private Long id;
    private String username;

    public SimpleUserSnapshot() {
    }

    public SimpleUserSnapshot(final Long id, final String username) {
        this.id = id;
        this.username = username;
    }

    Long getId() {
        return id;
    }

    String getUsername() {
        return username;
    }
}
