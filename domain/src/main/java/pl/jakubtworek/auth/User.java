package pl.jakubtworek.auth;

class User {
    private Long id;
    private String username;
    private String password;

    public User() {
    }

    private User(final Long id,
                 final String username,
                 final String password
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    static User restore(UserSnapshot snapshot) {
        return new User(
                snapshot.getId(),
                snapshot.getUsername(),
                snapshot.getPassword()
        );
    }

    UserSnapshot getSnapshot() {
        return new UserSnapshot(
                id,
                username,
                password
        );
    }

    void updateInfo(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
