package pl.jakubtworek.auth.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserDto.DeserializationImpl.class)
public interface UserDto {

    static UserDto create(final Long id, final String username, final String password) {
        return new UserDto.DeserializationImpl(id, username, password);
    }

    Long getId();
    String getUsername();
    String getPassword();

    class DeserializationImpl implements UserDto {
        private Long id;
        private String username;
        private String password;

        DeserializationImpl(final Long id, final String username, final String password) {
            this.id = id;
            this.username = username;
            this.password = password;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }
    }
}
