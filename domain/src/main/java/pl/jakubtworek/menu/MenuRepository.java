package pl.jakubtworek.menu;

import java.util.Optional;

interface MenuRepository {
    Optional<Menu> findById(Long id);

    Menu save(Menu entity);

    void deleteById(Long id);
}