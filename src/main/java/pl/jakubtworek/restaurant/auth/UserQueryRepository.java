package pl.jakubtworek.restaurant.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.restaurant.auth.query.SimpleUserQueryDto;

import java.util.Optional;
import java.util.UUID;

@Repository
interface UserQueryRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<SimpleUserQueryDto> findDtoByUsername(String username);

    Boolean existsByUsername(String username);
}