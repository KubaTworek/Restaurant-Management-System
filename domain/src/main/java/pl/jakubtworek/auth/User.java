package pl.jakubtworek.auth;

import java.util.Objects;

class User {
    private Long id;
    private String username;
    private String password;

    User() {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

    void updateInfo(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
