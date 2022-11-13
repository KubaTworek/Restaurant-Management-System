package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuController menuController;

    @BeforeEach
    public void setup() {
        mock(MenuService.class);

        menuController = new MenuController(
                menuService
        );

        when(menuService.findAll()).thenReturn(List.of(spy(new Menu(1L, "Drinks", List.of()))));
        when(menuService.findById(eq(1L))).thenReturn(Optional.of(spy(new Menu(1L, "Drinks", List.of()))));
        when(menuService.checkIsMenuIsNull(eq(3L))).thenReturn(true);
    }

    @Test
    void shouldReturnAllMenu() throws Exception {
        mockMvc.perform(get("/menu"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturnMenuById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/menu/1"))
                .andExpect(status().is(200))
                .andReturn();
        MenuDTO menuReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuDTO.class);

        assertThat(menuReturned).isNotNull();
        assertThat(menuReturned.getId()).isEqualTo(1L);
        assertThat(menuReturned.getName()).isEqualTo("Drinks");
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenu() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/menu/3"))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no menu in restaurant with that id: 3");
    }

/*    @Test
    void shouldReturnCreatedMenu() throws Exception {
        GetMenuDTO menu = new GetMenuDTO(0L, "Alcohol");

        MvcResult mvcResult = mockMvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menu)))
                .andExpect(status().isCreated())
                .andReturn();
        MenuDTO menuReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuDTO.class);

        assertThat(menuReturned).isNotNull();
        assertThat(menuReturned.getName()).isEqualTo("Alcohol");
    }*/

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/menu/1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted menu has id: 1");
    }
}
