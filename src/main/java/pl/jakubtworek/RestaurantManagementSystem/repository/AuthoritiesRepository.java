package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Authorities;

import java.util.*;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, UUID> {
    Optional<Authorities> findAuthoritiesByAuthority(String role);
}