package pl.jakubtworek.restaurant.auth;

class UserDto {
    private Long id;
    private String username;
    private String password;

    UserDto() {
    }

    public UserDto(final User source) {
        this.id = source.getId();
        this.username = source.getUsername();
        this.password = source.getPassword();
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
