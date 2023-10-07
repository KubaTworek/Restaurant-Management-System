package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface SqlMenuItemRepository extends JpaRepository<SqlMenuItem, Long> {

    <S extends SqlMenuItem> S save(S entity);

    void deleteById(Long id);
}

interface SqlMenuItemQueryRepository extends MenuItemQueryRepository, JpaRepository<SqlMenuItem, Long> {
}

@Repository
class MenuItemRepositoryImpl implements MenuItemRepository {

    private final SqlMenuItemRepository repository;

    MenuItemRepositoryImpl(final SqlMenuItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public MenuItem save(final MenuItem entity) {
        return repository.save(SqlMenuItem.fromMenuItem(entity)).toMenuItem();
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}