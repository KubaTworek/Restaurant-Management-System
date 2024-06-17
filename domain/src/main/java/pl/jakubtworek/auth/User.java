package pl.jakubtworek.auth;

import pl.jakubtworek.common.vo.Role;

import java.util.Objects;

class User {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    User() {
    }

    private User(final Long id, final String username, final String password, final Role role, final String firstName, final String lastName, final String email, final String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    static User restore(UserSnapshot snapshot) {
        return new User(
                snapshot.getId(),
                snapshot.getUsername(),
                snapshot.getPassword(),
                snapshot.getRole(),
                snapshot.getFirstName(),
                snapshot.getLastName(),
                snapshot.getEmail(),
                snapshot.getPhone()
        );
    }

    UserSnapshot getSnapshot() {
        return new UserSnapshot(
                id,
                username,
                password,
                role,
                firstName,
                lastName,
                email,
                phone
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, firstName, lastName, email, phone);
    }

    void updateInfo(final String username, final String password, final String role, final String firstName, final String lastName, final String email, final String phone) {
        this.username = username;
        this.password = password;
        this.role = Role.valueOf(role);
        if (this.role.equals(Role.USER)) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
        }
    }
}
