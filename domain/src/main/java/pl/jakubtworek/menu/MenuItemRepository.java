package pl.jakubtworek.menu;

import java.util.Optional;

interface MenuItemRepository {
    Optional<MenuItem> findById(Long id);

    MenuItem save(MenuItem entity);

    void deleteById(Long id);

    MenuItem.Menu save(MenuItem.Menu entity);
}
