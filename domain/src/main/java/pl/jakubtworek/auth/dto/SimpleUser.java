package pl.jakubtworek.auth.dto;

public class SimpleUser {
    public static SimpleUser restore(final SimpleUserSnapshot snapshot) {
        return new SimpleUser(snapshot.getId(), snapshot.getUsername());
    }

    private Long id;
    private String username;

    public SimpleUser(final Long id, final String username) {
        this.id = id;
        this.username = username;
    }

    public SimpleUserSnapshot getSnapshot() {
        return new SimpleUserSnapshot(id, username);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
