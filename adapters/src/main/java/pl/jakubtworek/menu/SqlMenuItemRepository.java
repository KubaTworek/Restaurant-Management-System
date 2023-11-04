package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface SqlMenuItemRepository extends JpaRepository<MenuItemSnapshot, Long> {

    <S extends MenuItemSnapshot> S save(S entity);

    void deleteById(Long id);
}

interface SqlMenuItemQueryRepository extends MenuItemQueryRepository, JpaRepository<MenuItemSnapshot, Long> {
}

@Repository
class MenuItemRepositoryImpl implements MenuItemRepository {

    private final SqlMenuItemRepository repository;

    MenuItemRepositoryImpl(final SqlMenuItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<MenuItem> findById(final Long id) {
        return repository.findById(id).map(MenuItem::restore);
    }

    @Override
    public MenuItem save(final MenuItem entity) {
        return MenuItem.restore(repository.save(entity.getSnapshot()));
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}