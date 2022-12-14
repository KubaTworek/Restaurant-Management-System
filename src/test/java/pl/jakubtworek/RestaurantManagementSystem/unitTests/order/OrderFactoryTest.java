package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.OrderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenuItemListForFood;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.createUser;

class OrderFactoryTest {
    private OrderFactory orderFactory;

    @BeforeEach
    void setUp() {
        orderFactory = new OrderFactory();
    }

    @Test
    void shouldReturnCreatedOrder() {
        // given
        OrderRequest orderRequest = createOnsiteOrderRequest();
        TypeOfOrderDTO typeOfOrderDTO = createOnsiteType().convertEntityToDTO();
        UserDTO userDTO = createUser().convertEntityToDTO();
        List<MenuItemDTO> menuItemDTOList = createMenuItemListForFood()
                .stream()
                .map(MenuItem::convertEntityToDTO)
                .collect(Collectors.toList());


        // when
        OrderDTO orderCreated = orderFactory.createOrder(orderRequest, typeOfOrderDTO, menuItemDTOList, userDTO);

        // then
        assertEquals("On-site", orderCreated.getTypeOfOrder().getType());
        assertEquals(12.98, orderCreated.getPrice());
        assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), orderCreated.getDate());
        assertNotNull(orderCreated.getHourOrder());
        assertEquals("Chicken", orderCreated.getMenuItems().get(0).getName());
        assertEquals("Tiramisu", orderCreated.getMenuItems().get(1).getName());
    }
}