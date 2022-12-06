package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImp implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemFactory menuItemFactory;

    @Override
    public List<MenuItemDTO> findAll() {
        return menuItemRepository.findAll()
                .stream()
                .map(MenuItem::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MenuItemDTO> findById(Long theId) {
        return menuItemRepository.findById(theId).map(MenuItem::convertEntityToDTO);
    }

    @Override
    public MenuItemDTO save(MenuItemRequest menuItemRequest, MenuDTO menuDTO) {
        MenuItemDTO menuItemDTO = menuItemFactory.createMenuItem(menuItemRequest, menuDTO);
        MenuItem menuItem = menuItemDTO.convertDTOToEntity();
        return menuItemRepository.save(menuItem).convertEntityToDTO();
    }

    @Override
    public void deleteById(Long theId) {
        menuItemRepository.findById(theId).orElseThrow().remove();
        menuItemRepository.deleteById(theId);
    }

    @Override
    public List<MenuItemDTO> findByMenu(Menu theMenu) {
        return menuItemRepository.findByMenu(theMenu)
                .stream()
                .map(MenuItem::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MenuItemDTO> findByName(String name) {
        return menuItemRepository.findByName(name).map(MenuItem::convertEntityToDTO);
    }
}