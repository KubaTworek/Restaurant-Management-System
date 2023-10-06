package pl.jakubtworek.restaurant.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}