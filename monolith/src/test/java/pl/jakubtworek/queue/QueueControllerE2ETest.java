package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.MenuRequest;
import pl.jakubtworek.order.dto.OrderRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueueControllerE2ETest extends AbstractIT {

    private String userToken;

    @BeforeEach
    void setup() {
        registerUser(new RegisterRequest("testuser", "password"));
        userToken = loginUserAndGetToken(new LoginRequest("testuser", "password"));
        createMenuAndMenuItems();
    }

    @Test
    @DirtiesContext
    void shouldEveryOrderBePreparedBySpecificEmployee() throws InterruptedException {
        // given
        final var employee1 = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        final var employee2 = postEmployee(new EmployeeRequest("Jane", "Smith", "WAITER"));

        final var request1 = new OrderRequest(
                "ON_SITE",
                List.of("Burger", "Cola")
        );
        final var request2 = new OrderRequest(
                "DELIVERY",
                List.of("Burger", "Cola")
        );

        // when
        final var createdOrder1 = postOrder(request1, userToken);
        final var createdOrder2 = postOrder(request2, userToken);

        // then
        assertEquals(HttpStatus.CREATED, createdOrder1.getStatusCode());
        assertEquals(HttpStatus.CREATED, createdOrder2.getStatusCode());
        assertNotNull(createdOrder1.getBody().getId());
        assertNotNull(createdOrder2.getBody().getId());

        Thread.sleep(1000);

        assertOrdersByEmployee(userToken, employee1, createdOrder1.getBody().getId(), createdOrder2.getBody().getId());
        assertOrdersByEmployee(userToken, employee2, createdOrder1.getBody().getId());
    }

    private void assertOrdersByEmployee(String userToken, ResponseEntity<EmployeeDto> employee, Long... orderIds) {
        final var response = getOrderByParam(userToken, Map.of("employeeId", employee.getBody().getId().toString()));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var orders = List.of(response.getBody());
        assertEquals(orderIds.length, orders.size());
        for (var orderId : orderIds) {
            assertTrue(orders.stream().anyMatch(orderDto -> orderDto.getId().equals(orderId)));
        }
    }

    private void createMenuAndMenuItems() {
        postMenu(new MenuRequest("Food"));
        postMenu(new MenuRequest("Drinks"));

        postMenuItem(new MenuItemRequest("Burger", 1099, "Food"));
        postMenuItem(new MenuItemRequest("Pasta", 1299, "Food"));
        postMenuItem(new MenuItemRequest("Cola", 599, "Drinks"));
        postMenuItem(new MenuItemRequest("Sprite", 499, "Drinks"));
    }

    private String loginUserAndGetToken(LoginRequest request) {
        return loginUser(request).getBody().getToken();
    }
}
