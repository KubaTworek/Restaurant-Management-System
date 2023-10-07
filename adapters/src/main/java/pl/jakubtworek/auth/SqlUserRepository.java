package pl.jakubtworek.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


interface SqlUserRepository extends JpaRepository<SqlUser, Long> {
    <S extends SqlUser> S save(S entity);
}

interface SqlUserQueryRepository extends UserQueryRepository, JpaRepository<SqlUser, Long> {
}

@Repository
class UserRepositoryImpl implements UserRepository {

    private final SqlUserRepository repository;

    UserRepositoryImpl(final SqlUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(final User entity) {
        return repository.save(SqlUser.fromUser(entity)).toUser();
    }
}