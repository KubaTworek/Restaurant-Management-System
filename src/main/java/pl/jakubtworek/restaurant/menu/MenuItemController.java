package pl.jakubtworek.restaurant.menu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/menu-items")
class MenuItemController {
    private final MenuItemService menuItemService;

    MenuItemController(final MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    ResponseEntity<MenuItemDto> saveMenuItem(@RequestBody MenuItemRequest menuItemRequest) {
        MenuItemDto result = menuItemService.save(menuItemRequest);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemRequest menuItemRequest) {

        // todo:
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<MenuItemDto> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<MenuItemDto> getMenuItemById(@PathVariable Long id) {
        return menuItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu/{menuName}")
    List<MenuItemDto> getMenuItemsByMenu(@PathVariable String menuName) {
        return menuItemService.findByMenu(menuName);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}