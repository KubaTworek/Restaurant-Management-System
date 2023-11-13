package pl.jakubtworek.menu;

import java.util.Optional;

interface MenuItemRepository {
    Optional<MenuItem> findById(Long id);

    MenuItem save(MenuItem entity);

    int deactivateMenuItem(Long id);

    MenuItem.Menu save(MenuItem.Menu entity);
}
