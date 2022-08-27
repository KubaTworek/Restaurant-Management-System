package pl.jakubtworek.RestaurantManagementSystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.List;

@RestController
@RequestMapping("/")
public class MenuItemRestController {

    private final MenuItemService menuItemService;

    public MenuItemRestController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/menuItems")
    public List<MenuItem> getMenuItems(){
        return menuItemService.findAll();
    }

    @GetMapping("/menuItem/id/{menuItemId}")
    public MenuItem getMenuItemById(@PathVariable int menuItemId) throws Exception {
        if(menuItemService.findById(menuItemId) == null) throw new Exception("Menu id not found - " + menuItemId);

        return menuItemService.findById(menuItemId);
    }

    @PostMapping("/menuItem")
    public MenuItem saveMenu(@RequestBody MenuItem theMenuItem){
        theMenuItem.setId(0);
        menuItemService.save(theMenuItem);

        return theMenuItem;
    }

    @DeleteMapping("/menuItem/{menuItemId}")
    public String deleteMenu(@PathVariable int menuItemId) throws Exception {
        if(menuItemService.findById(menuItemId) == null) throw new Exception("Employee id not found - " + menuItemId);
        menuItemService.deleteById(menuItemId);

        return "Deleted menu is - " + menuItemId;
    }

    @GetMapping("/menuItems/menu/{menuName}")
    public List<MenuItem> getMenuItemsByMenu(@PathVariable String menuName) throws Exception {
        if(menuItemService.findByMenu(menuName) == null) throw new Exception("Menu id not found - " + menuName);

        return menuItemService.findByMenu(menuName);
    }
}
