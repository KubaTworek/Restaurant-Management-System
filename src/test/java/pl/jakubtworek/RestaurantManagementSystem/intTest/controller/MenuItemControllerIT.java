package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemController;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuItemServiceImp;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuItemControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuItemRepository menuItemRepository;
    @MockBean
    private MenuRepository menuRepository;

    @Autowired
    private MenuItemFactory menuItemFactory;
    @Autowired
    private MenuFactory menuFactory;
    @Autowired
    private MenuItemController menuItemController;
    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private MenuService menuService;

    @BeforeEach
    public void setup() {
        mock(MenuItemRepository.class);

        menuFactory = new MenuFactory();
        menuItemService = new MenuItemServiceImp(
                menuItemRepository,
                menuRepository,
                menuItemFactory
        );
        menuItemController = new MenuItemController(
                menuItemService,
                menuService
        );
        menuService = new MenuServiceImpl(
                menuRepository,
                menuFactory
        );
        menuItemFactory = new MenuItemFactory(menuService);

        when(menuItemRepository.findById(eq(1L))).thenReturn(Optional.of(spy(new MenuItem(1L, "Chicken", 10.99, null, List.of()))));
        when(menuRepository.findByName(eq("Drinks"))).thenReturn(Optional.of(spy(new Menu(1L, "Drinks", List.of()))));
    }

    @Test
    void shouldReturnMenuItemById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/menu-items/1"))
                .andExpect(status().is(200))
                .andReturn();
        MenuItemDTO menuItem = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemDTO.class);

        assertThat(menuItem).isNotNull();
        assertThat(menuItem.getId()).isEqualTo(1L);
        assertThat(menuItem.getName()).isEqualTo("Chicken");
        assertThat(menuItem.getPrice()).isEqualTo(10.99);
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenuItem() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/menu-items/4"))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no menu item in restaurant with that id: 4");
    }

    @Test
    void shouldReturnCreatedMenuItem() throws Exception {
        GetMenuItemDTO menuItem = new GetMenuItemDTO(4L, "Beer", 5.99, "Drinks");

        MvcResult mvcResult = mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isCreated())
                .andReturn();
        MenuItemDTO menuItemGet = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemDTO.class);

        assertThat(menuItemGet).isNotNull();
        assertThat(menuItemGet.getName()).isEqualTo("Beer");
        assertThat(menuItemGet.getPrice()).isEqualTo(5.99);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/menu-items/1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted menu item has id: 1");
    }
}
