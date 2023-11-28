package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface SqlMenuItemRepository extends JpaRepository<MenuItemSnapshot, Long> {
    @Query("SELECT mi FROM MenuItemSnapshot mi " +
            "LEFT JOIN FETCH mi.menu " +
            "WHERE mi.id = :id")
    Optional<MenuItemSnapshot> findById(Long id);

    @Query("SELECT m FROM MenuSnapshot m " +
            "LEFT JOIN FETCH m.menuItems " +
            "WHERE m.id = :id")
    Optional<MenuSnapshot> findMenuById(Long id);
}

interface SqlMenuRepository extends JpaRepository<MenuSnapshot, Long> {}

interface SqlMenuItemQueryRepository extends MenuItemQueryRepository, JpaRepository<MenuItemSnapshot, Long> {
    @Query("SELECT DISTINCT mi FROM MenuItemSnapshot mi " +
            "LEFT JOIN FETCH mi.menu m " +
            "WHERE m.name = :menuName")
    List<MenuItemDto> findByMenuName(@Param("menuName") String menuName);

    @Query("SELECT mi FROM MenuItemSnapshot mi " +
            "WHERE mi.id = :id")
    Optional<MenuItemDto> findDtoById(@Param("id") Long id);

    @Query("SELECT mi FROM MenuItemSnapshot mi " +
            "WHERE mi.name = :name")
    Optional<MenuItemDto> findDtoByName(@Param("name") String name);

    @Query("SELECT mi FROM MenuItemSnapshot mi " +
            "WHERE mi.name IN :names")
    List<MenuItemDto> findAllDtoByNames(@Param("names") Set<String> names);
}

interface SqlMenuQueryRepository extends MenuQueryRepository, JpaRepository<MenuSnapshot, Long> {
    @Query("SELECT m FROM MenuSnapshot m " +
            "LEFT JOIN FETCH m.menuItems " +
            "WHERE m.name = :name")
    Optional<MenuDto> findDtoByName(@Param("name") String name);

    @Query("SELECT DISTINCT m FROM MenuSnapshot m " +
            "LEFT JOIN FETCH m.menuItems mi " +
            "WHERE mi.status = :status")
    Set<MenuDto> findDtoByMenuItems_Status(@Param("status") Status status);
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
    public Optional<Menu> findMenuById(final Long id) {
        return menuItemRepository.findMenuById(id).map(m -> Menu.restore(m, 1));
    }

    @Override
    public MenuItem save(final MenuItem entity) {
        return MenuItem.restore(menuItemRepository.save(entity.getSnapshot(1)), 1);
    }

    @Override
    public Menu save(final Menu entity) {
        return Menu.restore(menuRepository.save(entity.getSnapshot(1)), 1);
    }
}
