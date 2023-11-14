package pl.jakubtworek.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface SqlUserRepository extends JpaRepository<UserSnapshot, Long> {
    <S extends UserSnapshot> S save(S entity);
}

interface SqlUserQueryRepository extends UserQueryRepository, JpaRepository<UserSnapshot, Long> {
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
