package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ResponseEntity<List<MenuDTO>> getMenus() {
        List<Menu> menuFound = menuService.findAll();
        List<MenuDTO> menuDTO = createDTOList(menuFound);

        return new ResponseEntity<>(menuDTO, HttpStatus.OK);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Long menuId) throws MenuNotFoundException {
        if(menuService.checkIsMenuIsNull(menuId)){
            throw new MenuNotFoundException("There are no menu in restaurant with that id: " + menuId);
        }
        Menu menuFound = menuService.findById(menuId).get();
        MenuDTO menuDTO = menuFound.convertEntityToDTO();
        addLinkToDTO(menuDTO);

        return new ResponseEntity<>(menuDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuDTO> saveMenu(@RequestBody GetMenuDTO dto) {
        Menu menuSaved = menuService.save(dto);
        MenuDTO menuDTO = menuSaved.convertEntityToDTO();
        addLinkToDTO(menuDTO);

        return new ResponseEntity<>(menuDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long menuId) throws MenuNotFoundException {
        if(menuService.checkIsMenuIsNull(menuId)) {
            throw new MenuNotFoundException("There are menu in restaurant with that id: " + menuId);
        }
        menuService.deleteById(menuId);

        return new ResponseEntity<>("Deleted menu has id: " + menuId, HttpStatus.OK);
    }

    private List<MenuDTO> createDTOList(List<Menu> menuEntities){
        List<MenuDTO> menusDTO = menuEntities
                .stream()
                .map(Menu::convertEntityToDTO)
                .collect(Collectors.toList());

        menusDTO.forEach(this::addLinkToDTO);

        return menusDTO;
    }

    private void addLinkToDTO(MenuDTO dto){
        dto.add(WebMvcLinkBuilder.linkTo(MenuController.class).slash(dto.getId()).withSelfRel());
    }
}