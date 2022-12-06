package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuService menuService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) throws MenuItemNotFoundException {

        MenuItemResponse menuItemResponse = menuItemService.findById(id)
                .map(MenuItemDTO::convertDTOToResponse)
                .orElseThrow(() -> new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + id));

        return new ResponseEntity<>(menuItemResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> saveMenuItem(@RequestBody MenuItemRequest menuItemRequest) throws MenuNotFoundException {

        MenuDTO menuDTO = menuService.findByName(menuItemRequest.getMenu()).orElseThrow(MenuNotFoundException::new);
        MenuItemResponse menuItemResponse = menuItemService.save(menuItemRequest, menuDTO).convertDTOToResponse();

        return new ResponseEntity<>(menuItemResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MenuItemResponse> deleteMenuItem(@PathVariable Long id) throws MenuItemNotFoundException {

        MenuItemResponse menuItemResponse = menuItemService.findById(id)
                .map(MenuItemDTO::convertDTOToResponse)
                .orElseThrow(() -> new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + id));

        menuItemService.deleteById(id);

        return new ResponseEntity<>(menuItemResponse, HttpStatus.OK);
    }
}