package pl.jakubtworek.queue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.LoginResponse;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.UserDto;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.MenuRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QueueControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    void testCreateOrder() throws InterruptedException {
        // given
        EmployeeRequest request1 = new EmployeeRequest("John", "Doe", "COOK");
        EmployeeRequest request2 = new EmployeeRequest("Jane", "Smith", "WAITER");
        ResponseEntity<EmployeeDto> employee1 = restTemplate.postForEntity("http://localhost:" + port + "/employees", request1, EmployeeDto.class);
        ResponseEntity<EmployeeDto> employee2 = restTemplate.postForEntity("http://localhost:" + port + "/employees", request2, EmployeeDto.class);
        createUserAndLogin();

        createMenuAndMenuItems();

        // Create order request with menu items
        OrderRequest request = new OrderRequest(
                "ON_SITE",
                List.of("Burger", "Cola")
        );

        HttpHeaders headers = new HttpHeaders();
        String token = loginUserAndGetToken();
        headers.add("Authorization", token);

        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<OrderDto> response = restTemplate.exchange(
                "http://localhost:" + port + "/orders",
                HttpMethod.POST,
                requestEntity,
                OrderDto.class
        );

        restTemplate.exchange(
                "http://localhost:" + port + "/orders",
                HttpMethod.POST,
                new HttpEntity<>(new OrderRequest(
                        "DELIVERY",
                        List.of("Burger", "Cola")
                ), headers),
                OrderDto.class
        );
        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());

        Thread.sleep(1000);

        //////////////////////////////////////
        ResponseEntity<OrderDto[]> retrievedResponse1 = restTemplate.exchange(
                "http://localhost:" + port + "/orders/filter?employeeId=" + employee1.getBody().getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto[].class
        );

        assertEquals(HttpStatus.OK, retrievedResponse1.getStatusCode());
        List<OrderDto> orders1 = List.of(retrievedResponse1.getBody());
        assertEquals(2, orders1.size());
        assertEquals(response.getBody().getId(), orders1.get(0).getId());
        assertNotNull(orders1.get(0).getHourAway());

        ResponseEntity<OrderDto[]> retrievedResponse2 = restTemplate.exchange(
                "http://localhost:" + port + "/orders/filter?employeeId=" + employee2.getBody().getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto[].class
        );

        assertEquals(HttpStatus.OK, retrievedResponse2.getStatusCode());
        List<OrderDto> orders2 = List.of(retrievedResponse2.getBody());
        assertEquals(1, orders2.size());
        assertEquals(response.getBody().getId(), orders2.get(0).getId());
        assertNotNull(orders2.get(0).getHourAway());
    }
    @AfterEach
    void clean() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders__employee");
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders__menu_items");
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders");
        jdbcTemplate.execute("DROP TABLE IF EXISTS employees");
        jdbcTemplate.execute("DROP TABLE IF EXISTS menu_items");
        jdbcTemplate.execute("DROP TABLE IF EXISTS menu");
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
    }

    private void createMenuAndMenuItems() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", loginUserAndGetToken());

        restTemplate.exchange(
                "http://localhost:" + port + "/menu",
                HttpMethod.POST,
                new HttpEntity<>(new MenuRequest("Food"), headers),
                MenuDto.class
        );

        restTemplate.exchange(
                "http://localhost:" + port + "/menu",
                HttpMethod.POST,
                new HttpEntity<>(new MenuRequest("Drinks"), headers),
                MenuDto.class
        );

        createMenuItem("Burger", 1099, "Food");
        createMenuItem("Pasta", 1299, "Food");
        createMenuItem("Cola", 599, "Drinks");
        createMenuItem("Sprite", 499, "Drinks");
    }

    private void createMenuItem(String name, int price, String menuName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", loginUserAndGetToken());

        restTemplate.exchange(
                "http://localhost:" + port + "/menu-items",
                HttpMethod.POST,
                new HttpEntity<>(new MenuItemRequest(name, price, menuName), headers),
                MenuItemDto.class
        );
    }

    private void createUserAndLogin() {
        restTemplate.exchange(
                "http://localhost:" + port + "/users/register",
                HttpMethod.POST,
                new HttpEntity<>(new RegisterRequest("testuser", "password")),
                UserDto.class
        );

        restTemplate.exchange(
                "http://localhost:" + port + "/users/login",
                HttpMethod.POST,
                new HttpEntity<>(new LoginRequest("testuser", "password")),
                LoginResponse.class
        );
    }

    private String loginUserAndGetToken() {
        ResponseEntity<LoginResponse> loginResponse = restTemplate.exchange(
                "http://localhost:" + port + "/users/login",
                HttpMethod.POST,
                new HttpEntity<>(new LoginRequest("testuser", "password")),
                LoginResponse.class
        );

        return loginResponse.getBody().getToken();
    }
}
