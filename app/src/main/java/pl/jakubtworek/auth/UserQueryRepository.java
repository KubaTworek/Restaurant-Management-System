package pl.jakubtworek.auth;

import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.auth.dto.UserDto;

import java.util.Optional;

public interface UserQueryRepository {
    Optional<UserDto> findDtoByUsername(String username);

    Optional<SimpleUser> findSimpleByUsername(String username);

    Boolean existsByUsername(String username);
}