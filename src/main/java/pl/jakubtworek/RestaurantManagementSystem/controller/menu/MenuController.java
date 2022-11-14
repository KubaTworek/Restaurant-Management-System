package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus() {
        List<Menu> menuFound = menuService.findAll();
        List<MenuResponse> menuResponse = createDTOList(menuFound);

        return new ResponseEntity<>(menuResponse, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<MenuResponse> getMenuById(@RequestParam Long id) throws MenuNotFoundException {
        if(menuService.checkIsMenuIsNull(id)){
            throw new MenuNotFoundException("There are no menu in restaurant with that id: " + id);
        }
        Menu menuFound = menuService.findById(id).get();
        MenuResponse menuResponse = menuFound.convertEntityToResponse();
        addLinkToDTO(menuResponse);

        return new ResponseEntity<>(menuResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuResponse> saveMenu(@RequestBody MenuRequest dto) {
        Menu menuSaved = menuService.save(dto);
        MenuResponse menuResponse = menuSaved.convertEntityToResponse();
        addLinkToDTO(menuResponse);

        return new ResponseEntity<>(menuResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteMenu(@RequestParam Long id) throws MenuNotFoundException {
        if(menuService.checkIsMenuIsNull(id)) {
            throw new MenuNotFoundException("There are menu in restaurant with that id: " + id);
        }
        menuService.deleteById(id);

        return new ResponseEntity<>("Deleted menu has id: " + id, HttpStatus.OK);
    }

    private List<MenuResponse> createDTOList(List<Menu> menuEntities){
        List<MenuResponse> menuResponse = menuEntities
                .stream()
                .map(Menu::convertEntityToResponse)
                .collect(Collectors.toList());

        menuResponse.forEach(this::addLinkToDTO);

        return menuResponse;
    }

    private void addLinkToDTO(MenuResponse dto){
        dto.add(WebMvcLinkBuilder.linkTo(MenuController.class).slash(dto.getId()).withSelfRel());
    }
}