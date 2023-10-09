package pl.jakubtworek.menu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/menu-items")
class MenuItemController {
    private final MenuItemFacade menuItemFacade;

    MenuItemController(final MenuItemFacade menuItemFacade) {
        this.menuItemFacade = menuItemFacade;
    }

    @PostMapping
    ResponseEntity<MenuItemDto> saveMenuItem(@RequestBody MenuItemRequest menuItemRequest) {
        MenuItemDto result = menuItemFacade.save(menuItemRequest);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<MenuItemDto> deleteMenuItem(@PathVariable Long id) {
        menuItemFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<MenuItemDto> getMenuItemById(@PathVariable Long id) {
        return menuItemFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu/{menuName}")
    List<MenuItemDto> getMenuItemsByMenu(@PathVariable String menuName) {
        return menuItemFacade.findByMenu(menuName);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}