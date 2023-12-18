package pl.jakubtworek.auth;

import pl.jakubtworek.common.vo.Role;

import java.util.Objects;

class User {
    private Long id;
    private String username;
    private String password;
    private Role role;

    User() {
    }

    private User(final Long id,
                 final String username,
                 final String password,
                 final Role role
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    static User restore(UserSnapshot snapshot) {
        return new User(
                snapshot.getId(),
                snapshot.getUsername(),
                snapshot.getPassword(),
                snapshot.getRole()
        );
    }

    UserSnapshot getSnapshot() {
        return new UserSnapshot(
                id,
                username,
                password,
                role
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role);
    }

    void updateInfo(final String username, final String password,  final String role) {
        this.username = username;
        this.password = password;
        this.role = Role.valueOf(role);
    }
}
