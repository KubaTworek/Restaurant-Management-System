package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.response.Response;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.Optional;

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
    public ResponseEntity<Response<MenuItemDTO>> getMenuItemById(@PathVariable Long menuItemId) throws Exception {
        Response<MenuItemDTO> response = new Response<>();
        Optional<MenuItem> menuItem = menuItemService.findById(menuItemId);

        if(menuItem.isPresent()) {
            MenuItem menuItemFound = menuItem.get();
            MenuItemDTO dto = menuItemFound.convertEntityToDTO();

            response.setData(dto);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new MenuNotFoundException("There are no menu item in restaurant with that id: " + menuItemId);
    }

    @PostMapping("/{menuName}")
    public ResponseEntity<Response<MenuItemDTO>> saveMenuItem(@PathVariable String menuName, @RequestBody MenuItemDTO dto, BindingResult result) throws MenuNotFoundException {
        Response<MenuItemDTO> response = new Response<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        if(menuService.findByName(menuName).isPresent()){
            dto.setId(Long.parseLong("0"));
            dto.setMenu(menuService.findByName(menuName).get());
            MenuItem menuItem = menuItemService.save(dto.convertDTOToEntity());
            MenuItemDTO menuItemDTO = menuItem.convertEntityToDTO();
            response.setData(menuItemDTO);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        throw new MenuNotFoundException("There are no menu in restaurant with that name: " + menuName);
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Response<String>> deleteMenuItem(@PathVariable Long menuItemId) throws Exception {
        if(menuItemService.findById(menuItemId).isPresent()){
            menuItemService.deleteById(menuItemId);

            Response<String> response = new Response<>();

            response.setData("Deleted menu is - " + menuItemId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new MenuNotFoundException("Menu item id not found - " + menuItemId);
    }
}