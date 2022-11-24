package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuItemUtils.createChickenMenuItemDTO;

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
    private OrderRepository orderRepository;

    @Test
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItemDTO> expectedMenuItem = createChickenMenuItemDTO();

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
        // given
        MenuItemRequest menuItem = new MenuItemRequest(0L, "Pizza", 12.99, "Food");
        MenuItem expectedMenuItem = new MenuItem(0L, "Pizza", 12.99, new Menu(2L, "Food", List.of()), List.of());

        // when
        when(menuItemService.save(menuItem)).thenReturn(expectedMenuItem);

        MvcResult mvcResult = mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isCreated())
                .andReturn();
        MenuItemResponse menuItemResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemResponse.class);

        // then
        assertEquals("Pizza", menuItemResponse.getName());
        assertEquals(12.99, menuItemResponse.getPrice());
    }*/

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuItemDTO> expectedMenuItem = createChickenMenuItemDTO();

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
