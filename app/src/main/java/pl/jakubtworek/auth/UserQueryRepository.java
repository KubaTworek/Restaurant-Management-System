package pl.jakubtworek.auth;

import pl.jakubtworek.auth.dto.SimpleUser;

import java.util.Optional;

public interface UserQueryRepository {
    Optional<User> findByUsername(String username);

    Optional<SimpleUser> findDtoByUsername(String username);

    Boolean existsByUsername(String username);
}