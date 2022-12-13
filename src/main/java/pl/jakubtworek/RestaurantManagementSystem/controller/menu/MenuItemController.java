package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponse> saveMenuItem(@RequestBody MenuItemRequest menuItemRequest) throws MenuNotFoundException {
        MenuItemResponse menuItemResponse = menuItemService.save(menuItemRequest).convertDTOToResponse();

        return new ResponseEntity<>(menuItemResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(@RequestBody MenuItemRequest menuItemRequest, @PathVariable UUID id) throws MenuNotFoundException {
        MenuItemResponse menuItemResponse = menuItemService.update(menuItemRequest, id).convertDTOToResponse();

        return new ResponseEntity<>(menuItemResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable UUID id) throws MenuItemNotFoundException {
        menuItemService.deleteById(id);

        return new ResponseEntity<>("Menu item with id: " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable UUID id) throws MenuItemNotFoundException {
        MenuItemResponse menuItemResponse = menuItemService.findById(id)
                .map(MenuItemDTO::convertDTOToResponse)
                .map(MenuItemResponse::addLinkToResponse)
                .orElseThrow(() -> new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + id));

        return new ResponseEntity<>(menuItemResponse, HttpStatus.OK);
    }

    @GetMapping("/{menuName}")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByMenu(@PathVariable String menuName) throws MenuNotFoundException {
        List<MenuItemResponse> menuItemsResponse = menuItemService.findByMenu(menuName)
                .stream()
                .map(MenuItemDTO::convertDTOToResponse)
                .map(MenuItemResponse::addLinkToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(menuItemsResponse, HttpStatus.OK);
    }
}