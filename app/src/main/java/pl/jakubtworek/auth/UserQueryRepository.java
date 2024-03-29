package pl.jakubtworek.auth;

import pl.jakubtworek.auth.dto.UserDto;

import java.util.Optional;

interface UserQueryRepository {
    Optional<UserDto> findDtoByUsername(String username);

    Boolean existsByUsername(String username);
}
