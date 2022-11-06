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

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long menuItemId) throws MenuItemNotFoundException {
        if(menuItemService.checkIsMenuItemIsNull(menuItemId)){
            throw new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + menuItemId);
        }
        MenuItem menuItemFound = menuItemService.findById(menuItemId).get();
        MenuItemDTO menuItemDTO = menuItemFound.convertEntityToDTO();

        return new ResponseEntity<>(menuItemDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO> saveMenuItem(@RequestBody GetMenuItemDTO dto) throws MenuNotFoundException {
        if(menuService.checkIsMenuIsNull(dto.getMenu())){
            throw new MenuNotFoundException("There are no menu in restaurant with that name: " + dto.getMenu());
        }
        MenuItem menuItemSaved = menuItemService.save(dto);
        MenuItemDTO menuItemDTO = menuItemSaved.convertEntityToDTO();

        return new ResponseEntity<>(menuItemDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long menuItemId) throws MenuItemNotFoundException {
        if(menuItemService.checkIsMenuItemIsNull(menuItemId)){
            throw new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + menuItemId);
        }
        menuItemService.deleteById(menuItemId);

        return new ResponseEntity<>("Deleted menu item has id: " + menuItemId, HttpStatus.OK);
    }
}