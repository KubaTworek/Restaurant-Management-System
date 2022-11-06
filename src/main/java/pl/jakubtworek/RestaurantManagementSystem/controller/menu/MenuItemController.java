package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.response.Response;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long menuItemId) throws MenuItemNotFoundException {
        if(menuItemService.checkIsMenuItemIsNull(menuItemId)){
            throw new MenuItemNotFoundException("There are no menu item in restaurant with that id: " + menuItemId);
        }

        MenuItem menuItemFound = menuItemService.findById(menuItemId).get();
        MenuItemDTO dto = menuItemFound.convertEntityToDTO();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO> saveMenuItem(@RequestBody GetMenuItemDTO dto) {
        dto.setId(0L);
        MenuItem menuItem = menuItemService.save(dto.convertDTOToEntity());
        menuItem.setMenu(dto.getMenu().convertDTOToEntity());
        menuItem.getMenu().add(menuItem);
        MenuItemDTO menuItemDTO = menuItem.convertEntityToDTO();

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