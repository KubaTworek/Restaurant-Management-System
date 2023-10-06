package pl.jakubtworek.restaurant.menu;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class MenuFacade {
    private final MenuRepository menuRepository;
    private final MenuQueryRepository menuQueryRepository;

    MenuFacade(final MenuRepository menuRepository, final MenuQueryRepository menuQueryRepository) {
        this.menuRepository = menuRepository;
        this.menuQueryRepository = menuQueryRepository;
    }

    MenuDto save(MenuRequest toSave) {
        Menu menu = new Menu();
        menu.setName(toSave.getName());
        return menuRepository.saveAndReturnDto(menu);
    }

    void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    List<MenuDto> findAll() {
        return menuQueryRepository.findAllDtoBy();
    }

    Optional<MenuDto> findById(Long id) {
        return menuQueryRepository.findDtoById(id);
    }
}