package pl.jakubtworek.restaurant.menu;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class MenuFacade {
    private final MenuRepository menuRepository;

    MenuFacade(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    MenuDto save(MenuRequest toSave) {
        Menu menu = new Menu();
        menu.setName(toSave.getName());
        return new MenuDto(menuRepository.save(menu));
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