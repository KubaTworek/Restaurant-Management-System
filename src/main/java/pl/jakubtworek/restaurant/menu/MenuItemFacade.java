package pl.jakubtworek.restaurant.menu;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class MenuItemFacade {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemQueryRepository menuItemQueryRepository;
    private final MenuQueryRepository menuQueryRepository;

    MenuItemFacade(final MenuItemRepository menuItemRepository, final MenuItemQueryRepository menuItemQueryRepository,
                   final MenuQueryRepository menuQueryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemQueryRepository = menuItemQueryRepository;
        this.menuQueryRepository = menuQueryRepository;
    }

    Optional<MenuItemDto> findById(Long theId) {
        return menuItemQueryRepository.findDtoById(theId);
    }

    MenuItemDto save(MenuItemRequest toSave) {
        Menu menu = menuQueryRepository.findByName(toSave.getMenu())
                .orElseThrow(() -> new IllegalStateException("There are no menu in restaurant with that name: " + toSave.getMenu()));

        MenuItem menuItem = new MenuItem();
        menuItem.setMenu(menu);

        return menuItemRepository.saveAndReturnDto(menuItem);
    }

    MenuItemDto update(Long menuItemId, MenuItemRequest newMenuItem) {
        // todo:
        return null;
    }

    void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    List<MenuItemDto> findByMenu(String menuName) {
        return menuItemQueryRepository.findDtoByMenuName(menuName);
    }
}