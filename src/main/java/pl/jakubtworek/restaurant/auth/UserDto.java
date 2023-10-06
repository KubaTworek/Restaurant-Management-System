package pl.jakubtworek.restaurant.auth;

class UserDto {
    private Long id;
    private String username;
    private String password;

    UserDto() {
    }

    Long getId() {
        return id;
    }
}
