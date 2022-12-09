package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) throws MenuItemNotFoundException {
        MenuItemResponse menuItemResponse = menuItemService.findById(id)
                .map(MenuItemDTO::convertDTOToResponse)
                .orElseThrow(() -> new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + id));

        return new ResponseEntity<>(menuItemResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> saveMenuItem(@RequestBody MenuItemRequest menuItemRequest) throws MenuNotFoundException {
        MenuItemResponse menuItemResponse = menuItemService.save(menuItemRequest).convertDTOToResponse();

        return new ResponseEntity<>(menuItemResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) throws MenuItemNotFoundException {
        menuItemService.deleteById(id);

        return new ResponseEntity<>("Menu item with id: " + id + " was deleted", HttpStatus.OK);
    }
}