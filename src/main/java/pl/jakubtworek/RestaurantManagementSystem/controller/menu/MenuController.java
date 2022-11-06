package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeController;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.EmployeeNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.response.Response;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        List<Menu> menus = menuService.findAll();
        List<MenuDTO> menuDTOS = createDTOList(menus);

        return new ResponseEntity<>(menuDTOS, HttpStatus.OK);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Long menuId) throws MenuNotFoundException {
        if(menuService.checkIsMenuIsNull(menuId)){
            throw new MenuNotFoundException("There are no menu in restaurant with that id: " + menuId);
        }

        Menu menuFound = menuService.findById(menuId).get();
        MenuDTO dto = menuFound.convertEntityToDTO();
        addLinkToDTO(dto);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuDTO> saveMenu(@RequestBody MenuDTO dto) {

        dto.setId(0L);
        Menu menu = menuService.save(dto.convertDTOToEntity());
        MenuDTO menuDTO = menu.convertEntityToDTO();
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