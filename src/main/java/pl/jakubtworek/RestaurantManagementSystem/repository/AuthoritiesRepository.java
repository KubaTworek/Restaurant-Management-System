package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.Optional;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
    Optional<Authorities> findAuthoritiesByAuthority(String role);
}

