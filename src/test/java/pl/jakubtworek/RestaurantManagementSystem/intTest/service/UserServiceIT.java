package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class UserServiceIT {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    @Autowired
    private TypeOfOrderRepository typeOfOrderRepository;

    @MockBean
    private Authentication authentication;
    @MockBean
    private SecurityContext securityContext;

    @BeforeEach
    void setup() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");

        UserRequest userRequest = new UserRequest("user", "user", "user");
        userService.save(userRequest);

        TypeOfOrder onsite = new TypeOfOrder(null, "On-site", List.of());
        TypeOfOrder delivery = new TypeOfOrder(null, "Delivery", List.of());
        typeOfOrderRepository.save(onsite);
        typeOfOrderRepository.save(delivery);

        OrderRequest onsiteOrder = new OrderRequest("On-site", List.of());
        OrderRequest deliveryOrder = new OrderRequest("Delivery", List.of());

        orderService.save(onsiteOrder);
        orderService.save(deliveryOrder);
    }

    @Test
    void shouldReturnCreatedUser() {
        // given
        UserRequest userRequest = new UserRequest("admin", "admin", "admin");

        // when
        UserDTO userCreated = userService.save(userRequest);

        // then
        assertEquals("admin", userCreated.getUsername());
    }

    @Test
    void shouldDeleteUser() {
        // when
        UserDTO user1 = userService.findByUsername("user").orElse(null);
        userService.deleteByUsername("user");

        // then
        assertNotNull(user1);
        assertThrows(RuntimeException.class, () -> userService.findByUsername("user"));
    }

    @Test
    void shouldNotDeleteOrders_whenUserDelete() {
        // when
        List<OrderDTO> orders1 = orderService.findAll();
        userService.deleteByUsername("user");
        List<OrderDTO> orders2 = orderService.findAll();

        // then
        assertEquals(2, orders1.size());
        assertEquals(2, orders2.size());
    }
}
