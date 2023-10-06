package pl.jakubtworek.restaurant.menu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/menu")
class MenuController {
    private final MenuService menuService;

    MenuController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    ResponseEntity<MenuDto> create(@RequestBody MenuRequest menuRequest) {
        MenuDto result = menuService.save(menuRequest);

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    ResponseEntity<MenuDto> update(@PathVariable Long id, @RequestBody MenuRequest toUpdate) {

        // todo:
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<MenuDto> delete(@PathVariable Long id) {
        menuService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    List<MenuDto> get() {
        return menuService.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<MenuDto> getById(@PathVariable Long id) {
        return menuService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}