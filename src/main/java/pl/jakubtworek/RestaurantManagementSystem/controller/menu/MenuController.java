package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.*;
import java.util.stream.Collectors;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponse> saveMenu(@RequestBody MenuRequest menuRequest) {
        MenuResponse menuResponse = menuService.save(menuRequest).convertDTOToResponse();

        return new ResponseEntity<>(menuResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponse> updateMenu(@RequestBody MenuRequest menuRequest, @PathVariable UUID id) {
        MenuResponse menuResponse = menuService.update(menuRequest, id).convertDTOToResponse();

        return new ResponseEntity<>(menuResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable UUID id) throws MenuNotFoundException {
        menuService.deleteById(id);

        return new ResponseEntity<>("Menu with id: " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus() {
        List<MenuResponse> menuFound = menuService.findAll()
                .stream()
                .map(MenuDTO::convertDTOToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(menuFound, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getMenuById(@PathVariable UUID id) throws MenuNotFoundException {
        MenuResponse menuResponse = menuService.findById(id)
                .map(MenuDTO::convertDTOToResponse)
                .orElseThrow(() -> new MenuNotFoundException("There are no menu in restaurant with that id: " + id));

        return new ResponseEntity<>(menuResponse, HttpStatus.OK);
    }
}