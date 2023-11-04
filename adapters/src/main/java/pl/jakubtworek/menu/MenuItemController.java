package pl.jakubtworek.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/menu-items")
class MenuItemController {
    private static final Logger logger = LoggerFactory.getLogger(MenuItemController.class);
    private final MenuItemFacade menuItemFacade;

    MenuItemController(final MenuItemFacade menuItemFacade) {
        this.menuItemFacade = menuItemFacade;
    }

    @PostMapping
    ResponseEntity<MenuItemDto> saveMenuItem(@RequestBody MenuItemRequest menuItemRequest) {
        logger.info("Received a request to save a new menu item for menu: {}", menuItemRequest.getMenu());
        MenuItemDto result = menuItemFacade.save(menuItemRequest);
        logger.info("Menu item {} saved successfully.", result.getName());
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    List<MenuDto> get() {
        logger.info("Received a request to get the list of all menus.");
        return menuItemFacade.findAll();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<MenuItemDto> deleteMenuItem(@PathVariable Long id) {
        logger.info("Received a request to delete a menu item with ID: {}", id);
        menuItemFacade.deleteById(id);
        logger.info("Menu item with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<MenuItemDto> getMenuItemById(@PathVariable Long id) {
        logger.info("Received a request to get menu item details for ID: {}", id);
        return menuItemFacade.findById(id)
                .map(menuItem -> {
                    logger.info("Found menu item with ID {}: {}", id, menuItem.getName());
                    return ResponseEntity.ok(menuItem);
                })
                .orElseGet(() -> {
                    logger.warn("Menu item with ID {} not found.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/menu/{menuName}")
    List<MenuItemDto> getMenuItemsByMenu(@PathVariable String menuName) {
        logger.info("Received a request to get menu items for menu: {}", menuName);
        return menuItemFacade.findByMenu(menuName);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        logger.error("An error occurred: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}