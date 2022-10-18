package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.response.Response;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("")
    public ResponseEntity<Response<List<MenuDTO>>> getMenus() throws MenuNotFoundException {
        Response<List<MenuDTO>> response = new Response<>();
        List<Menu> menus = menuService.findAll();

        if (menus.isEmpty()) {
            throw new MenuNotFoundException("There are no menus in restaurant");
        }

        List<MenuDTO> menuDTOS = new ArrayList<>();
        menus.forEach(e -> menuDTOS.add(e.convertEntityToDTO()));

        menuDTOS.forEach(dto -> dto.add(WebMvcLinkBuilder.linkTo(MenuController.class).slash(dto.getId()).withSelfRel()));

        response.setData(menuDTOS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<Response<MenuDTO>> getMenuById(@PathVariable Long menuId) throws MenuNotFoundException {
        Response<MenuDTO> response = new Response<>();
        Optional<Menu> menu = menuService.findById(menuId);

        if(menu.isPresent()) {
            Menu menuFound = menu.get();
            MenuDTO dto = menuFound.convertEntityToDTO();

            dto.add(WebMvcLinkBuilder.linkTo(MenuController.class).slash(dto.getId()).withSelfRel());
            response.setData(dto);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new MenuNotFoundException("There are no menu in restaurant with that id: " + menuId);
    }

    @PostMapping("")
    public ResponseEntity<Response<MenuDTO>> saveMenu(@RequestBody MenuDTO dto, BindingResult result){
        Response<MenuDTO> response = new Response<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        dto.setId(Long.parseLong("0"));
        Menu menu = menuService.save(dto.convertDTOToEntity());
        MenuDTO menuDTO = menu.convertEntityToDTO();
        dto.add(WebMvcLinkBuilder.linkTo(MenuController.class).slash(dto.getId()).withSelfRel());
        response.setData(menuDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Response<String>> deleteMenu(@PathVariable Long menuId) throws MenuNotFoundException {
        if(menuService.findById(menuId).isPresent()){
            menuService.deleteById(menuId);

            Response<String> response = new Response<>();

            response.setData("Deleted menu is - " + menuId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new MenuNotFoundException("Menu id not found - " + menuId);
    }
}