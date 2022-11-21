package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemController;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemResponse;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuItemUtils.createChickenMenuItem;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenu;

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

    @Test
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = createChickenMenuItem();

        // when
        when(menuItemRepository.findById(eq(1L))).thenReturn(expectedMenuItem);

        MvcResult mvcResult = mockMvc.perform(get("/menu-items/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        MenuItemResponse menuItem = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemResponse.class);

        // then
        assertEquals("Chicken", menuItem.getName());
        assertEquals(10.99, menuItem.getPrice());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenuItem() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/menu-items/id")
                        .param("id", String.valueOf(4L)))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        // then
        assertEquals(404, response.getStatus());
        assertEquals("There are no menu item in restaurant with that id: 4", response.getMessage());
    }

    @Test
    void shouldReturnCreatedMenuItem() throws Exception {
        // given
        MenuItemRequest menuItem = new MenuItemRequest("Beer", 5.99, "Drinks");
        Optional<Menu> expectedMenu = createMenu();

        // when
        when(menuRepository.findByName(eq("Drinks"))).thenReturn(expectedMenu);

        MvcResult mvcResult = mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isCreated())
                .andReturn();
        MenuItemResponse menuItemResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemResponse.class);

        // then
        assertEquals("Beer", menuItemResponse.getName());
        assertEquals(5.99, menuItemResponse.getPrice());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = createChickenMenuItem();

        // when
        when(menuItemRepository.findById(eq(1L))).thenReturn(expectedMenuItem);

        MvcResult mvcResult = mockMvc.perform(delete("/menu-items/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals("Deleted menu item has id: 1", response);
    }
}
