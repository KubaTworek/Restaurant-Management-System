package pl.jakubtworek.restaurant.menu;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class MenuItemFacade {
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    MenuItemFacade(final MenuItemRepository menuItemRepository, final MenuRepository menuRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
    }

    Optional<MenuItemDto> findById(Long theId) {
        return menuItemRepository.findById(theId)
                .map(MenuItemDto::new);
    }

    MenuItemDto save(MenuItemRequest toSave) {
        Menu menu = menuRepository.findByName(toSave.getMenu())
                .orElseThrow(() -> new IllegalStateException("There are no menu in restaurant with that name: " + toSave.getMenu()));

        MenuItem menuItem = new MenuItem();
        menuItem.setMenu(menu);

        return new MenuItemDto(menuItemRepository.save(menuItem));
    }

    MenuItemDto update(Long menuItemId, MenuItemRequest newMenuItem) {
        // todo:
        return null;
    }

    void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    List<MenuItemDto> findByMenu(String menuName) {
        return menuItemRepository.findByMenuName(menuName)
                .stream()
                .map(MenuItemDto::new)
                .collect(Collectors.toList());
    }
}