package pl.jakubtworek.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.auth.dto.SimpleUserQueryDto;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserQueryRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<SimpleUserQueryDto> findDtoByUsername(String username);

    Boolean existsByUsername(String username);
}