package pl.jakubtworek.menu;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.order.dto.Status;

import java.util.Optional;
import java.util.Set;

interface SqlMenuItemRepository extends JpaRepository<MenuItemSnapshot, Long> {

    Optional<MenuItemSnapshot> findById(Long id);

    <S extends MenuItemSnapshot> S save(S entity);

    @Modifying
    @Query("UPDATE MenuItemSnapshot m SET m.status = 'INACTIVE' WHERE m.id = :id AND m.status = 'ACTIVE'")
    @Transactional
    int deactivateMenuItem(@Param("id") Long id);
}

interface SqlMenuRepository extends JpaRepository<MenuSnapshot, Long> {
    <S extends MenuSnapshot> S save(S entity);
}

interface SqlMenuItemQueryRepository extends MenuItemQueryRepository, JpaRepository<MenuItemSnapshot, Long> {
}

interface SqlMenuQueryRepository extends MenuQueryRepository, JpaRepository<MenuSnapshot, Long> {
    @Override
    @Query("SELECT DISTINCT m FROM MenuSnapshot m LEFT JOIN FETCH m.menuItems mi WHERE mi.status = :status")
    Set<MenuDto> findDtoByMenuItems_Status(Status status);
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
        return menuItemRepository.findById(id).map(mi -> MenuItem.restore(mi, 1));
    }

    @Override
    public MenuItem save(final MenuItem entity) {
        final var e = entity.getSnapshot(1);
        return MenuItem.restore(menuItemRepository.save(entity.getSnapshot(1)), 1);
    }

    @Override
    public int deactivateMenuItem(Long id) {
        return menuItemRepository.deactivateMenuItem(id);
    }

    @Override
    public MenuItem.Menu save(final MenuItem.Menu entity) {
        return MenuItem.Menu.restore(menuRepository.save(entity.getSnapshot(1)), 1);
    }
}
