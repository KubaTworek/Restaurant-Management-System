package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

@SpringBootTest
@Transactional
class OrderServiceIT {

    @Autowired
    private OrderService orderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private TypeOfOrderRepository typeOfOrderRepository;
    @Autowired
    private AuthoritiesRepository authoritiesRepository;


    @MockBean
    private Authentication authentication;
    @MockBean
    private SecurityContext securityContext;

    private static UUID idOrder;

    @BeforeEach
    public void setup() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");

        UserRequest userRequest = new UserRequest("user", "user", "user");
        userService.save(userRequest);

        MenuRequest menu1 = new MenuRequest("Drinks");
        MenuRequest menu2 = new MenuRequest("Food");
        menuService.save(menu1);
        menuService.save(menu2);

        MenuItemRequest menuItem1 = new MenuItemRequest("Chicken", 10.99, "Food");
        MenuItemRequest menuItem2 = new MenuItemRequest("Coke", 1.99, "Drinks");
        MenuItemRequest menuItem3 = new MenuItemRequest("Tiramisu", 5.99, "Food");
        menuItemService.save(menuItem1);
        menuItemService.save(menuItem2);
        menuItemService.save(menuItem3);

        Job job1 = new Job(null, "Cook", new ArrayList<>());
        Job job2 = new Job(null, "Waiter", new ArrayList<>());
        Job job3 = new Job(null, "DeliveryMan", new ArrayList<>());
        jobRepository.save(job1);
        jobRepository.save(job2);
        jobRepository.save(job3);

        EmployeeRequest employeeRequest1 = new EmployeeRequest("John", "Smith", "Cook");
        EmployeeRequest employeeRequest2 = new EmployeeRequest("James", "Patel", "Waiter");
        EmployeeRequest employeeRequest3 = new EmployeeRequest("Ann", "Mary", "DeliveryMan");

        employeeService.save(employeeRequest1);
        employeeService.save(employeeRequest2);
        employeeService.save(employeeRequest3);

        TypeOfOrder onsite = new TypeOfOrder(null, "On-site", List.of());
        TypeOfOrder delivery = new TypeOfOrder(null, "Delivery", List.of());
        typeOfOrderRepository.save(onsite);
        typeOfOrderRepository.save(delivery);

        OrderRequest onsiteOrder = new OrderRequest("On-site", List.of(menuItem1, menuItem2));
        OrderRequest deliveryOrder = new OrderRequest("Delivery", List.of(menuItem3, menuItem2));

        idOrder = orderService.save(onsiteOrder).getId();
        orderService.save(deliveryOrder);
    }

    @Test
    void shouldReturnCreatedOrder() throws Exception {
        // given
        OrderRequest order = createOnsiteOrderRequest();

        // when
        OrderDTO orderCreated = orderService.save(order);
        List<OrderDTO> orders = orderService.findAll();

        // then
        assertEquals(12.98, orderCreated.getPrice());
        assertNull(orderCreated.getHourAway());
        assertEquals("On-site", orderCreated.getTypeOfOrder().getType());
        assertEquals("Chicken", orderCreated.getMenuItems().get(0).getName());
        assertEquals(10.99, orderCreated.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderCreated.getMenuItems().get(1).getName());
        assertEquals(1.99, orderCreated.getMenuItems().get(1).getPrice());

        assertEquals(3, orders.size());
    }

    @Test
    void shouldDeleteOrder() throws OrderNotFoundException {
        // when
        OrderDTO order1 = orderService.findById(idOrder).orElse(null);
        orderService.deleteById(idOrder);
        OrderDTO order2 = orderService.findById(idOrder).orElse(null);

        // then
        assertNotNull(order1);
        assertNull(order2);
    }

    @Test
    void shouldNotDeleteUserAndMenuItems_whenDeleteOrder() throws OrderNotFoundException, MenuNotFoundException {
        // when
        UserDTO user1 = userService.findByUsername("user").orElse(null);
        List<MenuItemDTO> menuItem1 = menuItemService.findByMenu("Food");
        orderService.deleteById(idOrder);
        UserDTO user2 = userService.findByUsername("user").orElse(null);
        List<MenuItemDTO> menuItem2 = menuItemService.findByMenu("Food");

        // then
        assertNotNull(user1);
        assertEquals(2, menuItem1.size());
        assertNotNull(user2);
        assertEquals(2, menuItem2.size());
    }

    @Test
    void shouldReturnAllOrders() {
        // when
        List<OrderDTO> ordersReturned = orderService.findAll();

        // then
        assertEquals(12.98, ordersReturned.get(0).getPrice());
        assertNull(ordersReturned.get(0).getHourAway());
        assertEquals("On-site", ordersReturned.get(0).getTypeOfOrder().getType());
        assertEquals("Chicken", ordersReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(10.99, ordersReturned.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(0).getMenuItems().get(1).getPrice());

        assertEquals(7.98, ordersReturned.get(1).getPrice());
        assertNull(ordersReturned.get(1).getHourAway());
        assertEquals("Delivery", ordersReturned.get(1).getTypeOfOrder().getType());
        assertEquals("Tiramisu", ordersReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(5.99, ordersReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(1).getMenuItems().get(1).getPrice());
    }

    @Test
    void shouldReturnOneOrder() {
        // when
        OrderDTO orderReturned = orderService.findById(idOrder).orElse(null);

        // then
        assertEquals(12.98, orderReturned.getPrice());
        assertNull(orderReturned.getHourAway());
        assertEquals("On-site", orderReturned.getTypeOfOrder().getType());
        assertEquals("Chicken", orderReturned.getMenuItems().get(0).getName());
        assertEquals(10.99, orderReturned.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderReturned.getMenuItems().get(1).getName());
        assertEquals(1.99, orderReturned.getMenuItems().get(1).getPrice());
    }

    @ParameterizedTest
    @MethodSource("params")
    void shouldReturnOrdersByParams(int expectedSize, String date, String typeOfOrder, UUID employeeId, String username) {
        // when
        List<OrderDTO> ordersReturned = orderService.findByParams(date, typeOfOrder, employeeId, username);

        // then
        assertEquals(expectedSize, ordersReturned.size());
    }

    private static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of(2, null, null, null, null),
                Arguments.of(2, getTodayDate(), null, null, null),
                Arguments.of(1, null, "On-site", null, null),
                Arguments.of(0, null, null, UUID.randomUUID(), null),
                Arguments.of(0, getTodayDate(), null, UUID.randomUUID(), null),
                Arguments.of(1, getTodayDate(), "On-site", null, null),
                Arguments.of(0, null, "On-site", UUID.randomUUID(), null),
                Arguments.of(0, getTodayDate(), "On-site", UUID.randomUUID(), null)
        );
    }

    @Test
    void shouldReturnMadeOrders() {
        // when
        List<OrderDTO> ordersReturned = orderService.findMadeOrders();

        // then
        assertEquals(0, ordersReturned.size());
    }

    @Test
    void shouldReturnUnmadeOrders() {
        // when
        List<OrderDTO> ordersReturned = orderService.findUnmadeOrders();

        // then
        assertEquals(12.98, ordersReturned.get(0).getPrice());
        assertNull(ordersReturned.get(0).getHourAway());
        assertEquals("On-site", ordersReturned.get(0).getTypeOfOrder().getType());
        assertEquals("Chicken", ordersReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(10.99, ordersReturned.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(0).getMenuItems().get(1).getPrice());

        assertEquals(7.98, ordersReturned.get(1).getPrice());
        assertNull(ordersReturned.get(1).getHourAway());
        assertEquals("Delivery", ordersReturned.get(1).getTypeOfOrder().getType());
        assertEquals("Tiramisu", ordersReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(5.99, ordersReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(1).getMenuItems().get(1).getPrice());
    }

    @Test
    void shouldDeleteOrderByUsername() throws OrderNotFoundException {
        // when
        OrderDTO order1 = orderService.findByIdAndUsername(idOrder, "user").orElse(null);
        orderService.deleteByIdAndUsername(idOrder, "user");
        assertThrows(OrderNotFoundException.class, () -> orderService.findByIdAndUsername(idOrder, "user"));

        // then
        assertNotNull(order1);
    }

    @Test
    void shouldReturnAllOrdersByUser() {
        // when
        List<OrderDTO> ordersReturned = orderService.findAllByUsername("user");

        // then
        assertEquals(12.98, ordersReturned.get(0).getPrice());
        assertNull(ordersReturned.get(0).getHourAway());
        assertEquals("On-site", ordersReturned.get(0).getTypeOfOrder().getType());
        assertEquals("Chicken", ordersReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(10.99, ordersReturned.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(0).getMenuItems().get(1).getPrice());

        assertEquals(7.98, ordersReturned.get(1).getPrice());
        assertNull(ordersReturned.get(1).getHourAway());
        assertEquals("Delivery", ordersReturned.get(1).getTypeOfOrder().getType());
        assertEquals("Tiramisu", ordersReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(5.99, ordersReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(1).getMenuItems().get(1).getPrice());
    }

    @Test
    void shouldReturnOrderByIdByUser() throws OrderNotFoundException {
        // when
        OrderDTO orderReturned = orderService.findByIdAndUsername(idOrder, "user").orElse(null);

        // then
        assertEquals(12.98, orderReturned.getPrice());
        assertNull(orderReturned.getHourAway());
        assertEquals("On-site", orderReturned.getTypeOfOrder().getType());
        assertEquals("Chicken", orderReturned.getMenuItems().get(0).getName());
        assertEquals(10.99, orderReturned.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderReturned.getMenuItems().get(1).getName());
        assertEquals(1.99, orderReturned.getMenuItems().get(1).getPrice());
    }

    @Test
    void shouldReturnMadeOrdersByUser() {
        // when
        List<OrderDTO> ordersReturned = orderService.findMadeOrdersAndUsername("user");

        // then
        assertEquals(0, ordersReturned.size());
    }

    @Test
    void shouldReturnUnmadeOrdersByUser() {
        // when
        List<OrderDTO> ordersReturned = orderService.findUnmadeOrdersAndUsername("user");

        // then
        assertEquals(12.98, ordersReturned.get(0).getPrice());
        assertNull(ordersReturned.get(0).getHourAway());
        assertEquals("On-site", ordersReturned.get(0).getTypeOfOrder().getType());
        assertEquals("Chicken", ordersReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(10.99, ordersReturned.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(0).getMenuItems().get(1).getPrice());

        assertEquals(7.98, ordersReturned.get(1).getPrice());
        assertNull(ordersReturned.get(1).getHourAway());
        assertEquals("Delivery", ordersReturned.get(1).getTypeOfOrder().getType());
        assertEquals("Tiramisu", ordersReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(5.99, ordersReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(1).getMenuItems().get(1).getPrice());
    }

    private static String getTodayDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(localDateTime);
    }
}
