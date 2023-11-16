package pl.jakubtworek.menu;

import java.util.Optional;

interface MenuItemRepository {
    Optional<MenuItem> findById(Long id);

    MenuItem save(MenuItem entity);

    int deactivateMenuItem(Long id);

    Menu save(Menu entity);
}
