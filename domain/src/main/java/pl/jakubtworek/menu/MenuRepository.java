package pl.jakubtworek.menu;

interface MenuRepository {
    Menu save(Menu entity);

    void deleteById(Long id);
}