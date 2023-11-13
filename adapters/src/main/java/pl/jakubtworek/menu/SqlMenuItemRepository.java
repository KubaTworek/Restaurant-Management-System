package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface SqlMenuItemRepository extends JpaRepository<MenuItemSnapshot, Long> {

    Optional<MenuItemSnapshot> findById(Long id);

    <S extends MenuItemSnapshot> S save(S entity);

    void deleteById(Long id);
}

interface SqlMenuRepository extends JpaRepository<MenuSnapshot, Long> {
    <S extends MenuSnapshot> S save(S entity);
}

interface SqlMenuItemQueryRepository extends MenuItemQueryRepository, JpaRepository<MenuItemSnapshot, Long> {
}

interface SqlMenuQueryRepository extends MenuQueryRepository, JpaRepository<MenuSnapshot, Long> {
}

@Repository
class MenuItemRepositoryImpl implements MenuItemRepository {

    private final SqlMenuItemRepository menuItemRepository;
    private final SqlMenuRepository menuRepository;

    MenuItemRepositoryImpl(
            final SqlMenuItemRepository menuItemRepository,
            final SqlMenuRepository menuRepository
    ) {
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public Optional<MenuItem> findById(final Long id) {
        return menuItemRepository.findById(id).map(MenuItem::restore);
    }

    @Override
    public MenuItem save(final MenuItem entity) {
        return MenuItem.restore(menuItemRepository.save(entity.getSnapshot()));
    }

    @Override
    public void deleteById(final Long id) {
        menuItemRepository.deleteById(id);
    }

    @Override
    public MenuItem.Menu save(final MenuItem.Menu entity) {
        return MenuItem.Menu.restore(menuRepository.save(entity.getSnapshot()));
    }
}
