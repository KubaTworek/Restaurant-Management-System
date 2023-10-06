package pl.jakubtworek.restaurant.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    UserDto saveAndReturnDto(User entity);
}