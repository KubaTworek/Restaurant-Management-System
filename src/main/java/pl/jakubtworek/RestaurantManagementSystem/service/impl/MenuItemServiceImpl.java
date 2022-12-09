package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
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
    public MenuItemDTO save(MenuItemRequest menuItemRequest) throws MenuNotFoundException {
        MenuDTO menuDTO = getMenuDTO(menuItemRequest.getMenu());

        MenuItem menuItem = menuItemFactory.createMenuItem(menuItemRequest, menuDTO).convertDTOToEntity();
        return menuItemRepository.save(menuItem).convertEntityToDTO();
    }

    @Override
    public void deleteById(Long theId) throws MenuItemNotFoundException {
        menuItemRepository.findById(theId)
                .orElseThrow(() -> new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + theId))
                .remove();
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

    private MenuDTO getMenuDTO(String menuName) throws MenuNotFoundException {
        return menuRepository.findByName(menuName)
                .orElseThrow(() -> new MenuNotFoundException("There are no menu in restaurant with that name: " + menuName))
                .convertEntityToDTO();
    }
}