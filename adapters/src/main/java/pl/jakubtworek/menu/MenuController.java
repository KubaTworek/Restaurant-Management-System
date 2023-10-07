package pl.jakubtworek.menu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final MenuFacade menuFacade;

    MenuController(final MenuFacade menuFacade) {
        this.menuFacade = menuFacade;
    }

    @PostMapping
    ResponseEntity<MenuDto> create(@RequestBody MenuRequest menuRequest) {
        MenuDto result = menuFacade.save(menuRequest);

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<MenuDto> delete(@PathVariable Long id) {
        menuFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    List<MenuDto> get() {
        return menuFacade.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<MenuDto> getById(@PathVariable Long id) {
        return menuFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}