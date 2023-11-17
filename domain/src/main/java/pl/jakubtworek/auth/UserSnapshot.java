package pl.jakubtworek.auth;

import java.util.Objects;

class UserSnapshot {
    private Long id;
    private String username;
    private String password;

    UserSnapshot() {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserSnapshot that = (UserSnapshot) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
