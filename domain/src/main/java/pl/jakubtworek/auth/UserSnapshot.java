package pl.jakubtworek.auth;

import pl.jakubtworek.common.vo.Role;

import java.util.Objects;

class UserSnapshot {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    UserSnapshot() {
    }

    UserSnapshot(final Long id, final String username, final String password, final Role role, final String firstName, final String lastName, final String email, final String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserSnapshot that = (UserSnapshot) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && role == that.role && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, firstName, lastName, email, phone);
    }
}
