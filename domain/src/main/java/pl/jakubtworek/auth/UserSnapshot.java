package pl.jakubtworek.auth;

class UserSnapshot {
    private Long id;
    private String username;
    private String password;

    public UserSnapshot() {
    }

    UserSnapshot(final Long id,
                 final String username,
                 final String password
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    Long getId() {
        return id;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }
}
