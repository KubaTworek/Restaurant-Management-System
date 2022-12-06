package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuFactory menuFactory;

    @Override
    public List<MenuDTO> findAll() {
        return menuRepository.findAll()
                .stream()
                .map(Menu::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MenuDTO> findById(Long theId) {
        return menuRepository.findById(theId).map(Menu::convertEntityToDTO);
    }

    @Override
    public MenuDTO save(MenuRequest menuRequest) {
        MenuDTO menuDTO = menuFactory.createMenu(menuRequest);
        Menu menu = menuDTO.convertDTOToEntity();
        return menuRepository.save(menu).convertEntityToDTO();
    }

    @Override
    public void deleteById(Long theId) {
        menuRepository.deleteById(theId);
    }

    @Override
    public Optional<MenuDTO> findByName(String menuName) {
        return menuRepository.findByName(menuName).map(Menu::convertEntityToDTO);
    }
}