package pl.jakubtworek.menu;

import java.util.Optional;

interface MenuItemRepository {
    Optional<MenuItem> findById(Long id);

    Optional<Menu> findMenuById(Long id);

    MenuItem save(MenuItem entity);

    Menu save(Menu entity);
}
