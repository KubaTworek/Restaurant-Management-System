package pl.jakubtworek.orders;

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
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.MenuRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    void testCreateOrder() {
        // given
        registerUser();

        createMenuAndMenuItems();

        // Create order request with menu items
        OrderRequest request = new OrderRequest(
                "DELIVERY",
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

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DirtiesContext
    void testGetOrders() {
        // given
        registerUser();

        createMenuAndMenuItems();

        // Create order requests
        OrderRequest request1 = new OrderRequest(
                "DELIVERY",
                List.of("Burger", "Cola")
        );
        OrderRequest request2 = new OrderRequest(
                "ON_SITE",
                List.of("Pasta", "Sprite")
        );
        HttpHeaders headers = new HttpHeaders();
        String token = loginUserAndGetToken();
        headers.add("Authorization", token);
        restTemplate.exchange("http://localhost:" + port + "/orders", HttpMethod.POST,
                new HttpEntity<>(request1, headers), OrderDto.class);
        restTemplate.exchange("http://localhost:" + port + "/orders", HttpMethod.POST,
                new HttpEntity<>(request2, headers), OrderDto.class);

        // when
        ResponseEntity<OrderDto[]> response = restTemplate.exchange(
                "http://localhost:" + port + "/orders",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto[].class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<OrderDto> orders = List.of(response.getBody());
        assertEquals(2, orders.size());
    }

    @Test
    @DirtiesContext
    void testGetOrderById() {
        // given
        registerUser();

        createMenuAndMenuItems();

        // Create an order
        OrderRequest request = new OrderRequest(
                "DELIVERY",
                List.of("Burger", "Cola")
        );
        HttpHeaders headers = new HttpHeaders();
        String token = loginUserAndGetToken();
        headers.add("Authorization", token);
        ResponseEntity<OrderDto> createdResponse = restTemplate.exchange(
                "http://localhost:" + port + "/orders",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                OrderDto.class
        );
        Long orderId = createdResponse.getBody().getId();

        // when
        ResponseEntity<OrderDto> retrievedResponse = restTemplate.exchange(
                "http://localhost:" + port + "/orders/" + orderId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto.class
        );

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals(TypeOfOrder.DELIVERY, retrievedResponse.getBody().getTypeOfOrder());
    }

    @Test
    @DirtiesContext
    void testGetOrderByParams() {
        // given
        registerUser();

        createMenuAndMenuItems();

        // Create order requests
        OrderRequest request1 = new OrderRequest("DELIVERY", List.of("Burger", "Cola"));
        OrderRequest request2 = new OrderRequest("ON_SITE", List.of("Pasta", "Sprite"));
        HttpHeaders headers = new HttpHeaders();
        String token = loginUserAndGetToken();
        headers.add("Authorization", token);
        restTemplate.exchange("http://localhost:" + port + "/orders", HttpMethod.POST, new HttpEntity<>(request1, headers), OrderDto.class);
        restTemplate.exchange("http://localhost:" + port + "/orders", HttpMethod.POST, new HttpEntity<>(request2, headers), OrderDto.class);

        // when - Filter by typeOfOrder
        ResponseEntity<OrderDto[]> response1 = restTemplate.exchange(
                "http://localhost:" + port + "/orders/filter?typeOfOrder=DELIVERY",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto[].class
        );

        // then - Verify results for typeOfOrder
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        List<OrderDto> orders1 = List.of(response1.getBody());
        assertEquals(1, orders1.size());
        assertEquals(TypeOfOrder.DELIVERY, orders1.get(0).getTypeOfOrder());

        // when - Filter by fromDate and toDate
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        ZonedDateTime fromDate = ZonedDateTime.now().minusHours(2);
        ZonedDateTime toDate = ZonedDateTime.now();

        String fromDateStr = formatter.format(fromDate);
        String toDateStr = formatter.format(toDate);

        ResponseEntity<OrderDto[]> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/orders/filter?fromDate=" + fromDateStr + "&toDate=" + toDateStr,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto[].class
        );

        // then - Verify results for fromDate and toDate
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        List<OrderDto> orders2 = List.of(response2.getBody());
        assertEquals(2, orders2.size()); // Both orders should be within the specified time range

        // when - Filter by isReady
        ResponseEntity<OrderDto[]> response3 = restTemplate.exchange(
                "http://localhost:" + port + "/orders/filter?isReady=true",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto[].class
        );

        // then - Verify results for isReady
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        List<OrderDto> orders3 = List.of(response3.getBody());
        assertEquals(0, orders3.size()); // No orders are marked as ready in the test data

        // when - Filter by employeeId
        Long employeeId = 1L; // Replace with an actual employee ID
        ResponseEntity<OrderDto[]> response4 = restTemplate.exchange(
                "http://localhost:" + port + "/orders/filter?employeeId=" + employeeId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto[].class
        );

        // then - Verify results for employeeId
        assertEquals(HttpStatus.OK, response4.getStatusCode());
        List<OrderDto> orders4 = List.of(response4.getBody());
        // Add assertions for employee-specific filtering

        // when - Filter by username
        String username = "testuser"; // Replace with an actual username
        ResponseEntity<OrderDto[]> response5 = restTemplate.exchange(
                "http://localhost:" + port + "/orders/filter?username=" + username,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OrderDto[].class
        );

        // then - Verify results for username
        assertEquals(HttpStatus.OK, response5.getStatusCode());
        List<OrderDto> orders5 = List.of(response5.getBody());
        // Add assertions for username-specific filtering
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

    private void registerUser() {
        restTemplate.exchange(
                "http://localhost:" + port + "/users/register",
                HttpMethod.POST,
                new HttpEntity<>(new RegisterRequest("testuser", "password")),
                UserDto.class
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
