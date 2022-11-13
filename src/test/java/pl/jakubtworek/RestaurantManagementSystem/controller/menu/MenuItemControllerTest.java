package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuItemUtils.createMenuItem;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuItemService menuItemService;
    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuItemController menuItemController;

    @BeforeEach
    public void setup() {
        mock(MenuItemService.class);
        mock(MenuService.class);

        menuItemController = new MenuItemController(
                menuItemService,
                menuService
        );
    }

    @Test
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = createMenuItem();

        // when
        when(menuItemService.findById(eq(1L))).thenReturn(expectedMenuItem);

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
        when(menuItemService.checkIsMenuItemIsNull(eq(4L))).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(get("/menu-items/id")
                        .param("id", String.valueOf(4L)))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        // then
        assertEquals(404, response.getStatus());
        assertEquals("There are no menu item in restaurant with that id: 4", response.getMessage());
    }

/*    @Test
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
    }*/

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = createMenuItem();

        // when
        when(menuItemService.findById(eq(1L))).thenReturn(expectedMenuItem);

        MvcResult mvcResult = mockMvc.perform(delete("/menu-items/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals("Deleted menu item has id: 1", response);
    }
}
