package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.*;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    private final String USER_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJSZXN0YXVyYW50Iiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJ1c2VyIiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NzA5OTA3MTEsImV4cCI6MTAwMTY3MDk5MDcxMX0.FQXWI59l0axX413c5H4F1lByE2mSK4fe4pBvvR-gYV8";
    private final String ADMIN_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJSZXN0YXVyYW50Iiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJhZG1pbiIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY3MDk5MDgwMSwiZXhwIjoxMDAxNjcwOTkwODAxfQ.-3BdaIuq5rSWzf9zIdkR3S1ftAk9SSGY5iaX0zCYO08";

    @BeforeEach
    void setup() {
        UserRequest userRequest = new UserRequest("user", "user", "ROLE_USER");
        UserRequest adminRequest = new UserRequest("admin", "admin", "ROLE_ADMIN");
        userController.registerUser(userRequest);
        userController.registerUser(adminRequest);
    }

    @WithAnonymousUser()
    @Test
    public void employeesGet_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(get("/employees")).andExpect(status().isUnauthorized());
    }

    @WithAnonymousUser()
    @Test
    public void employeesPost_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(post("/employees")).andExpect(status().isUnauthorized());
    }

    @WithAnonymousUser()
    @Test
    public void employeesDelete_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(delete("/employees")).andExpect(status().isUnauthorized());
    }

    @WithMockUser("user")
    @Test
    public void employeesGet_userAuth_returnsForbidden() throws Exception {
        this.mockMvc.perform(get("/employees").header("Authorization", USER_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("user")
    @Test
    public void employeesPost_userAuth_returnsForbidden() throws Exception {
        this.mockMvc.perform(post("/employees").header("Authorization", USER_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("user")
    @Test
    public void employeesDelete_userAuth_returnsForbidden() throws Exception {
        this.mockMvc.perform(delete("/employees").header("Authorization", USER_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("admin")
    @Test
    public void employeesGet_withValidJwtToken_returnsOk() throws Exception {
        this.mockMvc.perform(get("/employees").header("Authorization", ADMIN_JWT)).andExpect(status().isOk());
    }

    @WithMockUser("admin")
    @Test
    public void employeesPost_withValidJwtToken_returnsOk() throws Exception {
        this.mockMvc.perform(post("/employees")
                        .content(objectMapper.writeValueAsString(new EmployeeRequest("Jakub", "Tworek", "Cook")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", ADMIN_JWT))
                .andExpect(status().isCreated());
    }

    @WithMockUser("admin")
    @Test
    public void employeesDelete_withValidJwtToken_returnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/employees/79ff0068-7c12-11ed-a1eb-0242ac120002").header("Authorization", ADMIN_JWT)).andExpect(status().isNotFound());
    }

    // order

    @WithAnonymousUser()
    @Test
    public void ordersGet_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(get("/orders")).andExpect(status().isUnauthorized());
    }

    @WithAnonymousUser()
    @Test
    public void ordersPost_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(post("/orders")).andExpect(status().isUnauthorized());
    }

    @WithAnonymousUser()
    @Test
    public void ordersDelete_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(delete("/orders")).andExpect(status().isUnauthorized());
    }

    @WithMockUser("user")
    @Test
    public void ordersGet_userAuth_returnsOk() throws Exception {
        this.mockMvc.perform(get("/orders").header("Authorization", USER_JWT)).andExpect(status().isOk());
    }

    @WithMockUser("user")
    @Test
    public void ordersDelete_userAuth_returnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/orders/79ff0068-7c12-11ed-a1eb-0242ac120002").header("Authorization", USER_JWT)).andExpect(status().isNotFound());
    }

    @WithMockUser("admin")
    @Test
    public void ordersGet_withValidJwtToken_returnsForbidden() throws Exception {
        this.mockMvc.perform(get("/orders").header("Authorization", ADMIN_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("admin")
    @Test
    public void ordersPost_withValidJwtToken_returnsForbidden() throws Exception {
        this.mockMvc.perform(post("/orders")
                        .header("Authorization", ADMIN_JWT))
                .andExpect(status().isForbidden());
    }

    @WithMockUser("admin")
    @Test
    public void ordersDelete_withValidJwtToken_returnsForbidden() throws Exception {
        this.mockMvc.perform(delete("/orders").header("Authorization", ADMIN_JWT)).andExpect(status().isForbidden());
    }

    // admin-orders

    @WithAnonymousUser()
    @Test
    public void ordersAdminGet_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(get("/admin-orders")).andExpect(status().isUnauthorized());
    }

    @WithAnonymousUser()
    @Test
    public void ordersAdminPost_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(post("/admin-orders")).andExpect(status().isUnauthorized());
    }

    @WithAnonymousUser()
    @Test
    public void ordersAdminDelete_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(delete("/admin-orders")).andExpect(status().isUnauthorized());
    }

    @WithMockUser("user")
    @Test
    public void ordersAdminGet_userAuth_returnsForbidden() throws Exception {
        this.mockMvc.perform(get("/admin-orders").header("Authorization", USER_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("user")
    @Test
    public void ordersAdminPost_userAuth_returnsForbidden() throws Exception {
        this.mockMvc.perform(post("/admin-orders")
                .content(objectMapper.writeValueAsString(new OrderRequest("On-site", List.of())))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", USER_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("user")
    @Test
    public void ordersAdminDelete_userAuth_returnsForbidden() throws Exception {
        this.mockMvc.perform(delete("/admin-orders").header("Authorization", USER_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("admin")
    @Test
    public void ordersAdminGet_withValidJwtToken_returnsOk() throws Exception {
        this.mockMvc.perform(get("/admin-orders").header("Authorization", ADMIN_JWT)).andExpect(status().isOk());
    }

    // menu

    @WithAnonymousUser()
    @Test
    public void menuGet_noAuth_returnsOk() throws Exception {
        this.mockMvc.perform(get("/menu")).andExpect(status().isOk());
    }

    @WithAnonymousUser()
    @Test
    public void menuPost_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(post("/menu")).andExpect(status().isUnauthorized());
    }

    @WithAnonymousUser()
    @Test
    public void menuDelete_noAuth_returnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/menu/79ff0068-7c12-11ed-a1eb-0242ac120002")).andExpect(status().isNotFound());
    }

    @WithMockUser("user")
    @Test
    public void menuGet_userAuth_returnsOk() throws Exception {
        this.mockMvc.perform(get("/menu").header("Authorization", USER_JWT)).andExpect(status().isOk());
    }

    @WithMockUser("user")
    @Test
    public void menuPost_userAuth_returnsForbidden() throws Exception {
        this.mockMvc.perform(post("/menu")
                .header("Authorization", USER_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("user")
    @Test
    public void menuDelete_userAuth_returnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/menu/79ff0068-7c12-11ed-a1eb-0242ac120002").header("Authorization", USER_JWT)).andExpect(status().isNotFound());
    }

    @WithMockUser("admin")
    @Test
    public void menuGet_withValidJwtToken_returnsOk() throws Exception {
        this.mockMvc.perform(get("/menu").header("Authorization", ADMIN_JWT)).andExpect(status().isOk());
    }

    @WithMockUser("admin")
    @Test
    public void menuPost_withValidJwtToken_returnsCreated() throws Exception {
        this.mockMvc.perform(post("/menu")
                        .content(objectMapper.writeValueAsString(new MenuRequest("Desserts")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", ADMIN_JWT))
                .andExpect(status().isCreated());
    }

    @WithMockUser("admin")
    @Test
    public void menuDelete_withValidJwtToken_returnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/menu/79ff0068-7c12-11ed-a1eb-0242ac120002").header("Authorization", ADMIN_JWT)).andExpect(status().isNotFound());
    }

    // menu-items

    @WithAnonymousUser()
    @Test
    public void menuItemGet_noAuth_returnsNotFound() throws Exception {
        this.mockMvc.perform(get("/menu-items/79ff0068-7c12-11ed-a1eb-0242ac120002")).andExpect(status().isNotFound());
    }

    @WithAnonymousUser()
    @Test
    public void menuItemPost_noAuth_returnsUnauthorized() throws Exception {
        this.mockMvc.perform(post("/menu-items")).andExpect(status().isUnauthorized());
    }

    @WithAnonymousUser()
    @Test
    public void menuItemDelete_noAuth_returnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/menu-items/79ff0068-7c12-11ed-a1eb-0242ac120002")).andExpect(status().isNotFound());
    }

    @WithMockUser("user")
    @Test
    public void menuItemGet_userAuth_returnsNotFound() throws Exception {
        this.mockMvc.perform(get("/menu-items/79ff0068-7c12-11ed-a1eb-0242ac120002").header("Authorization", USER_JWT)).andExpect(status().isNotFound());
    }

    @WithMockUser("user")
    @Test
    public void menuItemPost_userAuth_returnsForbidden() throws Exception {
        this.mockMvc.perform(post("/menu-items")
                .header("Authorization", USER_JWT)).andExpect(status().isForbidden());
    }

    @WithMockUser("user")
    @Test
    public void menuItemDelete_userAuth_returnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/menu-items/79ff0068-7c12-11ed-a1eb-0242ac120002").header("Authorization", USER_JWT)).andExpect(status().isNotFound());
    }

    @WithMockUser("admin")
    @Test
    public void menuItemGet_withValidJwtToken_returnsNotFound() throws Exception {
        this.mockMvc.perform(get("/menu-items/79ff0068-7c12-11ed-a1eb-0242ac120002").header("Authorization", ADMIN_JWT)).andExpect(status().isNotFound());
    }

    @WithMockUser("admin")
    @Test
    public void menuItemPost_withValidJwtToken_returnsCreated() throws Exception {
        this.mockMvc.perform(post("/menu")
                .content(objectMapper.writeValueAsString(new MenuRequest("Desserts")))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", ADMIN_JWT));
        this.mockMvc.perform(post("/menu-items")
                        .content(objectMapper.writeValueAsString(new MenuItemRequest("Tiramisu", 4.99, "Desserts")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", ADMIN_JWT))
                .andExpect(status().isCreated());
    }
}