package pl.jakubtworek.menu;

interface MenuItemRepository {
    MenuItem save(MenuItem entity);

    void deleteById(Long id);
}