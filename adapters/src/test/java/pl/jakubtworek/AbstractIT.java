package pl.jakubtworek;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import pl.jakubtworek.order.dto.OrderConfirmRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderReceiveRequest;
import pl.jakubtworek.order.dto.OrderRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIT {

    @LocalServerPort
    public int port;

    @Autowired
    public TestRestTemplate restTemplate;
    public String userToken;
    public String adminToken;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        registerUser(new RegisterRequest("testuser", "password", "USER", "John", "Doe", "john.doe@mail.com", "450642154"));
        registerUser(new RegisterRequest("admin", "admin", "ADMIN", null, null, null, null));
        userToken = loginUserAndGetToken(new LoginRequest("testuser", "password"));
        adminToken = loginUserAndGetToken(new LoginRequest("admin", "admin"));
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
        return loginUser(request).token();
    }

    // EMPLOYEE

    public EmployeeDto postEmployee(EmployeeRequest request) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", adminToken);
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(request, headers);

        final var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/employees",
                requestEntity,
                EmployeeDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    public List<EmployeeDto> getEmployees() {
        final var headers = new HttpHeaders();
        headers.add("Authorization", adminToken);
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(headers);

        final var response = restTemplate.exchange(
                "http://localhost:" + port + "/employees",
                HttpMethod.GET,
                requestEntity,
                EmployeeDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }

    public ResponseEntity<Void> deactivateEmployeeById(Long employeeId) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", adminToken);
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:" + port + "/employees/" + employeeId,
                HttpMethod.PUT,
                requestEntity,
                Void.class
        );
    }

    public EmployeeDto getEmployeeById(Long employeeId) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", adminToken);
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(headers);

        final var response = restTemplate.exchange(
                "http://localhost:" + port + "/employees/" + employeeId,
                HttpMethod.GET,
                requestEntity,
                EmployeeDto.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    // USER

    public UserDto registerUser(RegisterRequest request) {
        final var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/users/register",
                request,
                UserDto.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    public LoginResponse loginUser(LoginRequest request) {
        final var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/users/login",
                request,
                LoginResponse.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();

    }

    // MENU ITEM

    public MenuItemDto postMenuItem(MenuItemRequest request) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", adminToken);
        HttpEntity<MenuItemRequest> requestEntity = new HttpEntity<>(request, headers);

        final var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/menu-items",
                requestEntity,
                MenuItemDto.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    public List<MenuDto> getMenus() {
        final var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/menu-items",
                MenuDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }

    public ResponseEntity<Void> deleteMenuItemById(Long menuItemId) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", adminToken);
        HttpEntity<MenuItemRequest> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:" + port + "/menu-items/" + menuItemId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );
    }

    public ResponseEntity<Void> deactivateMenuItemById(Long menuItemId) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", adminToken);
        HttpEntity<MenuItemRequest> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:" + port + "/menu-items/" + menuItemId,
                HttpMethod.PUT,
                requestEntity,
                Void.class
        );
    }

    public MenuItemDto getMenuItemById(Long menuItemId) {
        final var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/menu-items/" + menuItemId,
                MenuItemDto.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    // ORDER

    public OrderDto postOrder(OrderRequest request, String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(request, headers);

        final var response = restTemplate.exchange(
                "http://localhost:" + port + "/orders",
                HttpMethod.POST,
                requestEntity,
                OrderDto.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    public OrderDto confirmOrder(OrderConfirmRequest request, String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderConfirmRequest> requestEntity = new HttpEntity<>(request, headers);

        final var response = restTemplate.exchange(
                "http://localhost:" + port + "/orders/confirm",
                HttpMethod.POST,
                requestEntity,
                OrderDto.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    public OrderDto receiveOrder(OrderReceiveRequest request, String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderReceiveRequest> requestEntity = new HttpEntity<>(request, headers);

        final var response = restTemplate.exchange(
                "http://localhost:" + port + "/orders/receive",
                HttpMethod.POST,
                requestEntity,
                OrderDto.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    public List<OrderDto> getOngoingOrders(String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(headers);

        final var response = restTemplate.exchange(
                "http://localhost:" + port + "/orders/ongoing",
                HttpMethod.GET,
                requestEntity,
                OrderDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }

    public List<OrderDto> getOrders(String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(headers);

        final var response = restTemplate.exchange(
                "http://localhost:" + port + "/orders",
                HttpMethod.GET,
                requestEntity,
                OrderDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }

    public OrderDto getOrderById(Long orderId, String token) {
        final var headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(headers);

        final var response = restTemplate.exchange(
                "http://localhost:" + port + "/orders/" + orderId,
                HttpMethod.GET,
                requestEntity,
                OrderDto.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    public List<OrderDto> getOrderByParam(Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", adminToken);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/orders/filter");
        params.forEach(builder::queryParam);

        final var response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                OrderDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }
}
