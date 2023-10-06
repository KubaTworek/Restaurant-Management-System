package pl.jakubtworek.restaurant.menu;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class MenuService {
    private final MenuRepository menuRepository;

    MenuService(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    MenuDto save(MenuRequest toSave) {
        Menu menu = new Menu();
        menu.setName(toSave.getName());
        return new MenuDto(menuRepository.save(menu));
    }

    MenuDto update(Long menuId, MenuRequest menuRequest) {
        // todo:
        return null;
    }

    void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    List<MenuDto> findAll() {
        return menuRepository.findAll()
                .stream()
                .map(MenuDto::new)
                .collect(Collectors.toList());
    }

    Optional<MenuDto> findById(Long id) {
        return menuRepository.findById(id)
                .map(MenuDto::new);
    }
}