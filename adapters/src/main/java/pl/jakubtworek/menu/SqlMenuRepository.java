package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface SqlMenuRepository extends JpaRepository<SqlMenu, Long> {
    <S extends SqlMenu> S save(S entity);

    void deleteById(Long id);
}

interface SqlMenuQueryRepository extends MenuQueryRepository, JpaRepository<SqlMenu, Long> {
}

@Repository
class MenuRepositoryImpl implements MenuRepository {

    private final SqlMenuRepository repository;

    MenuRepositoryImpl(final SqlMenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return repository.findById(id).map(SqlMenu::toMenu);
    }

    @Override
    public Menu save(final Menu entity) {
        return repository.save(SqlMenu.fromMenu(entity)).toMenu();
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}
