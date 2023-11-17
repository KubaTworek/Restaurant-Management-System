package pl.jakubtworek.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.auth.dto.UserDto;

import java.util.Optional;

interface SqlUserRepository extends JpaRepository<UserSnapshot, Long> {}

interface SqlUserQueryRepository extends UserQueryRepository, JpaRepository<UserSnapshot, Long> {
    @Query("SELECT u FROM UserSnapshot u WHERE u.username = :username")
    Optional<UserDto> findDtoByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 " +
            "THEN true ELSE false END " +
            "FROM UserSnapshot u WHERE u.username = :username")
    Boolean existsByUsername(String username);
}

@Repository
class UserRepositoryImpl implements UserRepository {

    private final SqlUserRepository repository;

    UserRepositoryImpl(final SqlUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(final User entity) {
        return User.restore(repository.save(entity.getSnapshot()));
    }
}
