package pl.jakubtworek;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.LoginResponse;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.UserDto;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.OrderResponse;

import java.math.BigDecimal;
import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIT {

    @LocalServerPort
    public int port;

    @Autowired
    public TestRestTemplate restTemplate;
    public String userToken;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        registerUser(new RegisterRequest("testuser", "password"));
        userToken = loginUserAndGetToken(new LoginRequest("testuser", "password"));
        createMenuItems();
    }

    @AfterEach
    void clean() {
        jdbcTemplate.execute("DROP ALL OBJECTS DELETE FILES");
    }

    private void createMenuItems() {
        postMenuItem(new MenuItemRequest("Burger", new BigDecimal("10.99"), "Food"));
        postMenuItem(new MenuItemRequest("Pasta", new BigDecimal("12.99"), "Food"));
        postMenuItem(new MenuItemRequest("Cola", new BigDecimal("5.99"), "Drinks"));
        postMenuItem(new MenuItemRequest("Sprite", new BigDecimal("4.99"), "Drinks"));
    }

    private String loginUserAndGetToken(LoginRequest request) {
        return loginUser(request).getBody().getToken();
    }

    // EMPLOYEE

    public ResponseEntity<EmployeeDto> postEmployee(EmployeeRequest request) {
        return restTemplate.postForEntity(
                "http://localhost:" + port + "/employees",
                request,
                EmployeeDto.class);
    }

    public ResponseEntity<EmployeeDto[]> getEmployees() {
        return restTemplate.getForEntity(
                "http://localhost:" + port + "/employees",
                EmployeeDto[].class
        );
    }

    public ResponseEntity<Void> deleteEmployeeById(Long employeeId) {
        return restTemplate.exchange(
                "http://localhost:" + port + "/employees/" + employeeId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }

    public ResponseEntity<EmployeeDto> getEmployeeById(Long employeeId) {
        return restTemplate.getForEntity(
                "http://localhost:" + port + "/employees/" + employeeId,
                EmployeeDto.class
        );
    }

    public ResponseEntity<EmployeeDto[]> getEmployeeByJob(String jobName) {
        return restTemplate.getForEntity(
                "http://localhost:" + port + "/employees/job?job=" + jobName,
                EmployeeDto[].class
        );
    }

    // USER

    public ResponseEntity<UserDto> registerUser(RegisterRequest request) {
        return restTemplate.postForEntity(
                "http://localhost:" + port + "/users/register",
                request,
                UserDto.class
        );
    }

    public ResponseEntity<LoginResponse> loginUser(LoginRequest request) {
        return restTemplate.postForEntity(
                "http://localhost:" + port + "/users/login",
                request,
                LoginResponse.class
        );
    }

    // MENU ITEM

    public ResponseEntity<MenuItemDto> postMenuItem(MenuItemRequest request) {
        return restTemplate.postForEntity(
                "http://localhost:" + port + "/menu-items",
                request,
                MenuItemDto.class
        );
    }

    public ResponseEntity<MenuDto[]> getMenus() {
        return restTemplate.getForEntity(
                "http://localhost:" + port + "/menu-items",
                MenuDto[].class
        );
    }

    public ResponseEntity<Void> deleteMenuItemById(Long menuItemId) {
        return restTemplate.exchange(
                "http://localhost:" + port + "/menu-items/" + menuItemId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }

    public ResponseEntity<MenuItemDto> getMenuItemById(Long menuItemId) {
        return restTemplate.getForEntity(
                "http://localhost:" + port + "/menu-items/" + menuItemId,
                MenuItemDto.class
        );
    }

    public ResponseEntity<MenuItemDto[]> getMenuItemByMenuName(String menuItemName) {
        return restTemplate.getForEntity(
                "http://localhost:" + port + "/menu-items/menu/" + menuItemName,
                MenuItemDto[].class
        );
    }

    // ORDER

    public ResponseEntity<OrderResponse> postOrder(OrderRequest request, String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(
                "http://localhost:" + port + "/orders",
                HttpMethod.POST,
                requestEntity,
                OrderResponse.class
        );
    }

    public ResponseEntity<OrderDto[]> getOrders(String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:" + port + "/orders",
                HttpMethod.GET,
                requestEntity,
                OrderDto[].class
        );
    }

    public ResponseEntity<OrderResponse> getOrderById(Long orderId, String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:" + port + "/orders/" + orderId,
                HttpMethod.GET,
                requestEntity,
                OrderResponse.class
        );
    }

    public ResponseEntity<OrderDto[]> getOrderByParam(String token, Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/orders/filter");
        params.forEach(builder::queryParam);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                OrderDto[].class
        );
    }
}
