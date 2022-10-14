package pl.jakubtworek.RestaurantManagementSystem.controller;

import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menus")
    public List<Menu> getMenus(){
        return menuService.findAll();
    }

    @GetMapping("/menu/id/{menuId}")
    public Menu getMenuById(@PathVariable int menuId) throws Exception {
        if(menuService.findById(menuId) == null) throw new Exception("Menu id not found - " + menuId);

        return menuService.findById(menuId);
    }

    @PostMapping("/menu")
    public Menu saveMenu(@RequestBody Menu theMenu){
        theMenu.setId(0);
        menuService.save(theMenu);

        return theMenu;
    }

    @DeleteMapping("/menu/{menuId}")
    public String deleteMenu(@PathVariable int menuId) throws Exception {
        if(menuService.findById(menuId) == null) throw new Exception("Employee id not found - " + menuId);
        menuService.deleteById(menuId);

        return "Deleted menu is - " + menuId;
    }

    @GetMapping("/menu/name/{name}")
    public Menu getMenuByName(@PathVariable String name) throws Exception {
        if(menuService.findByName(name) == null) throw new Exception("Menu id not found - " + name);

        return menuService.findByName(name);
    }
}
