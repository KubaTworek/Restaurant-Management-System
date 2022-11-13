package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuService menuService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService, MenuService menuService) {
        this.menuItemService = menuItemService;
        this.menuService = menuService;
    }

    @GetMapping("/id")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@RequestParam Long id) throws MenuItemNotFoundException {
        if(menuItemService.checkIsMenuItemIsNull(id)){
            throw new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + id);
        }
        MenuItem menuItemFound = menuItemService.findById(id).get();
        MenuItemResponse menuItemResponse = menuItemFound.convertEntityToResponse();

        return new ResponseEntity<>(menuItemResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> saveMenuItem(@RequestBody MenuItemRequest dto) throws MenuNotFoundException {
        if(menuService.checkIsMenuIsNull(dto.getMenu())){
            throw new MenuNotFoundException("There are no menu in restaurant with that name: " + dto.getMenu());
        }
        MenuItem menuItemSaved = menuItemService.save(dto);
        MenuItemResponse menuItemResponse = menuItemSaved.convertEntityToResponse();

        return new ResponseEntity<>(menuItemResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteMenuItem(@RequestParam Long id) throws MenuItemNotFoundException {
        if(menuItemService.checkIsMenuItemIsNull(id)){
            throw new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + id);
        }
        menuItemService.deleteById(id);

        return new ResponseEntity<>("Deleted menu item has id: " + id, HttpStatus.OK);
    }
}