package pl.jakubtworek.auth.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.jakubtworek.common.vo.Role;

@JsonDeserialize(as = UserDto.DeserializationImpl.class)
public interface UserDto {

    static UserDto create(final Long id, final String username, final String password, final Role role) {
        return new UserDto.DeserializationImpl(id, username, password, role);
    }

    Long getId();

    String getUsername();

    String getPassword();

    Role getRole();

    class DeserializationImpl implements UserDto {
        private final Long id;
        private final String username;
        private final String password;
        private final Role role;

        DeserializationImpl(final Long id, final String username, final String password, final Role role) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
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

        @Override
        public Role getRole() {
            return role;
        }
    }
}
