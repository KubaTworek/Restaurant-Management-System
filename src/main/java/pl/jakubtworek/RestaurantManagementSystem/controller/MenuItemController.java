package pl.jakubtworek.RestaurantManagementSystem.controller;

import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/")
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final MenuService menuService;

    public MenuItemController(MenuItemService menuItemService, MenuService menuService) {
        this.menuItemService = menuItemService;
        this.menuService = menuService;
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

    @PostMapping("/{menuName}/menuItem")
    public MenuItem saveMenuItem(@PathVariable String menuName, @RequestBody MenuItem theMenuItem){
        theMenuItem.setId(0);
        theMenuItem.setMenu(menuService.findByName(menuName));
        menuItemService.save(theMenuItem);

        return theMenuItem;
    }

    @DeleteMapping("/menuItem/{menuItemId}")
    public String deleteMenuItem(@PathVariable int menuItemId) throws Exception {
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
