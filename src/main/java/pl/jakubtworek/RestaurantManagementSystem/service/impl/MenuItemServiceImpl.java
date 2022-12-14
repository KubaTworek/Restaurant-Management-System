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
    public MenuItemDTO save(MenuItemRequest menuItemRequest) throws MenuNotFoundException {
        MenuDTO menuDTO = getMenuDTO(menuItemRequest.getMenu());

        MenuItem menuItem = menuItemFactory.createMenuItem(menuItemRequest, menuDTO).convertDTOToEntity();
        Menu menu = menuRepository.getReferenceById(menuDTO.getId());
        menu.add(menuItem);

        return menuItemRepository.save(menuItem).convertEntityToDTO();
    }

    @Override
    public MenuItemDTO update(MenuItemRequest newMenuItem, UUID menuItemId) throws MenuNotFoundException {

        MenuItem oldMenuItem = menuItemRepository.getReferenceById(menuItemId);
        MenuItem menuItemCreated = menuItemFactory.updateMenuItem(oldMenuItem, newMenuItem).convertDTOToEntity();

        return menuItemRepository.save(menuItemCreated).convertEntityToDTO();
    }

    @Override
    public void deleteById(UUID theId) throws MenuItemNotFoundException {
        MenuItem menuItem = menuItemRepository.findById(theId)
                .orElseThrow(() -> new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + theId));
        menuItem.remove();
        menuItemRepository.delete(menuItem);
    }

    @Override
    public Optional<MenuItemDTO> findById(UUID theId) {
        return menuItemRepository.findById(theId).map(MenuItem::convertEntityToDTO);
    }

    @Override
    public List<MenuItemDTO> findByMenu(String menuName) {
        return menuItemRepository.findByMenuName(menuName)
                .stream()
                .map(MenuItem::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    private MenuDTO getMenuDTO(String menuName) throws MenuNotFoundException {
        return menuRepository.findByName(menuName)
                .orElseThrow(() -> new MenuNotFoundException("There are no menu in restaurant with that name: " + menuName))
                .convertEntityToDTO();
    }
}