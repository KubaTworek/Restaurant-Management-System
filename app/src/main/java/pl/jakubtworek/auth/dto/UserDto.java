package pl.jakubtworek.auth.dto;

public class UserDto {

    private Long id;
    private String username;
    private String password;

    UserDto() {
    }

    UserDto(final Long id, final String username, final String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    static public Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public static class Builder {
        private Long id;
        private String username;
        private String password;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, username, password);
        }
    }
}
