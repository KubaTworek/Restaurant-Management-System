package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.order.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

public class OrderFactoryTest {
    private TypeOfOrderRepository typeOfOrderRepository;

    private OrderFactory orderFactory;

    @BeforeEach
    public void setUp() {
        typeOfOrderRepository = mock(TypeOfOrderRepository.class);
        orderFactory = new OrderFactory();
    }

    @Test
    public void shouldReturnOnsiteOrder(){
        // given
        MenuItemRequest menuItem1 = new MenuItemRequest("Apple", 2.99, "Food");
        MenuItemRequest menuItem2 = new MenuItemRequest("Coke", 1.99, "Drinks");
        OrderRequest orderDTO = new OrderRequest("On-site", List.of(menuItem1, menuItem2));
        Optional<TypeOfOrder> onsite = Optional.of(new TypeOfOrder(1L, "On-site", null));


        // when
        when(typeOfOrderRepository.findByType("On-site")).thenReturn(onsite);

        OrderDTO order = orderFactory.createOrder(orderDTO, createOnsiteTypeDTO()).createOrder();

        // then
        assertEquals("On-site", order.getTypeOfOrder().getType());
        assertEquals(4.98, order.getPrice());
        assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), order.getDate());
        assertNotNull(order.getHourOrder());
        assertEquals("Apple", order.getMenuItems().get(0).getName());
        assertEquals("Coke", order.getMenuItems().get(1).getName());
    }

    @Test
    public void shouldReturnDeliveryOrder(){
        // given
        MenuItemRequest menuItem = new MenuItemRequest("Meat", 10.99, "Food");
        OrderRequest orderDTO = new OrderRequest("Delivery", List.of(menuItem));
        Optional<TypeOfOrder> delivery = Optional.of(new TypeOfOrder(2L, "Delivery", null));

        // when
        when(typeOfOrderRepository.findByType("Delivery")).thenReturn(delivery);

        OrderDTO order = orderFactory.createOrder(orderDTO, createDeliveryTypeDTO()).createOrder();

        // then
        assertEquals("Delivery", order.getTypeOfOrder().getType());
        assertEquals(10.99, order.getPrice());
        assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), order.getDate());
        assertNotNull(order.getHourOrder());
        assertEquals("Meat", order.getMenuItems().get(0).getName());
    }
}
