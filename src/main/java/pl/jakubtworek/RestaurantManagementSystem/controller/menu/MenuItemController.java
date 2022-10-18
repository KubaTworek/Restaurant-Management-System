package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.response.Response;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.Optional;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
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

    @PostMapping("")
    public ResponseEntity<Response<MenuItemDTO>> saveMenuItem(@RequestBody GetMenuItemDTO dto, BindingResult result) {
        Response<MenuItemDTO> response = new Response<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        dto.setId(Long.parseLong("0"));
        MenuItem menuItem = menuItemService.save(dto.convertDTOToEntity());
        menuItem.setMenu(dto.getMenu().convertDTOToEntity());
        menuItem.getMenu().add(menuItem);
        MenuItemDTO menuItemDTO = menuItem.convertEntityToDTO();
        response.setData(menuItemDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Response<String>> deleteMenuItem(@PathVariable Long menuItemId) throws Exception {
        if(menuItemService.findById(menuItemId).isPresent()){
            menuItemService.deleteById(menuItemId);

            Response<String> response = new Response<>();

            response.setData("Deleted menu item is - " + menuItemId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new MenuNotFoundException("Menu item id not found - " + menuItemId);
    }
}