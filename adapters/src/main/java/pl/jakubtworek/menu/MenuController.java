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
import pl.jakubtworek.menu.dto.MenuRequest;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/menu")
class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
    private final MenuFacade menuFacade;

    MenuController(final MenuFacade menuFacade) {
        this.menuFacade = menuFacade;
    }

    @PostMapping
    ResponseEntity<MenuDto> create(@RequestBody MenuRequest menuRequest) {
        logger.info("Received a request to create a new menu with name: {}", menuRequest.getName());
        MenuDto result = menuFacade.save(menuRequest);
        logger.info("Menu {} created successfully.", result.getName());
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<MenuDto> delete(@PathVariable Long id) {
        logger.info("Received a request to delete a menu with ID: {}", id);
        menuFacade.deleteById(id);
        logger.info("Menu with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    List<MenuDto> get() {
        logger.info("Received a request to get the list of all menus.");
        return menuFacade.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<MenuDto> getById(@PathVariable Long id) {
        logger.info("Received a request to get menu details for ID: {}", id);
        return menuFacade.findById(id)
                .map(menu -> {
                    logger.info("Found menu with ID {}: {}", id, menu.getName());
                    return ResponseEntity.ok(menu);
                })
                .orElseGet(() -> {
                    logger.warn("Menu with ID {} not found.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        logger.error("An error occurred: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}