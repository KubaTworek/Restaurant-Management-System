package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuResponse;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Test
    void shouldReturnAllMenu() throws Exception {
        // given
        List<MenuDTO> expectedMenu = createMenuListDTO();

        // when
        when(menuService.findAll()).thenReturn(expectedMenu);

        MvcResult mvcResult = mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();
        List<MenuResponse> menuReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertEquals("Drinks", menuReturned.get(0).getName());
        assertEquals("Coke", menuReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(1.99, menuReturned.get(0).getMenuItems().get(0).getPrice());

        assertEquals("Food", menuReturned.get(1).getName());
        assertEquals("Chicken", menuReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(10.99, menuReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Tiramisu", menuReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(5.99, menuReturned.get(1).getMenuItems().get(1).getPrice());
    }

    @Test
    void shouldReturnMenuById() throws Exception {
        // given
        Optional<MenuDTO> expectedMenu = createMenuDTO();

        // when
        when(menuService.findById(eq(1L))).thenReturn(expectedMenu);

        MvcResult mvcResult = mockMvc.perform(get("/menu/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        MenuResponse menuReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuResponse.class);

        // then
        assertEquals("Drinks", menuReturned.getName());
        assertEquals("Coke", menuReturned.getMenuItems().get(0).getName());
        assertEquals(1.99, menuReturned.getMenuItems().get(0).getPrice());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenu() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/menu/id")
                        .param("id", String.valueOf(3L)))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        // then
        assertEquals(404, response.getStatus());
        assertEquals("There are no menu in restaurant with that id: 3", response.getMessage());
    }

/*    @Test
    void shouldReturnCreatedMenu() throws Exception {
        // given
        MenuRequest menu = new MenuRequest("Alcohol");
        MenuDTO expectedMenu = new MenuDTO(0L, "Alcohol", List.of());

        // when
        when(menuService.save(menu)).thenReturn(expectedMenu);

        MvcResult mvcResult = mockMvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menu)))
                .andExpect(status().isCreated())
                .andReturn();
        MenuResponse menuReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuResponse.class);

        // then
        assertEquals("Alcohol", menuReturned.getName());
    }*/

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuDTO> expectedMenu = createMenuDTO();

        // when
        when(menuService.findById(eq(1L))).thenReturn(expectedMenu);

        MvcResult mvcResult = mockMvc.perform(delete("/menu/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        MenuResponse menuDeleted = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuResponse.class);

        // then
        assertEquals("Drinks", menuDeleted.getName());
        assertEquals("Coke", menuDeleted.getMenuItems().get(0).getName());
        assertEquals(1.99, menuDeleted.getMenuItems().get(0).getPrice());
    }
}
