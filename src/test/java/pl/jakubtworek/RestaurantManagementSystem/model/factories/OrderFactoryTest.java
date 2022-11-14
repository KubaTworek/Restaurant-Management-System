package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemResponse;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.order.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderFactoryTest {
    @Mock
    private TypeOfOrderRepository typeOfOrderRepository;
    @Mock
    private OrdersQueue ordersQueue;

    private OrderFactory orderFactory;

    @BeforeEach
    public void setUp() {
        typeOfOrderRepository = mock(TypeOfOrderRepository.class);
        ordersQueue = mock(OrdersQueue.class);

        orderFactory = new OrderFactory(
                typeOfOrderRepository,
                ordersQueue
        );

        Optional<TypeOfOrder> onsite = Optional.of(new TypeOfOrder(1L, "On-site", null));
        Optional<TypeOfOrder> delivery = Optional.of(new TypeOfOrder(2L, "Delivery", null));
        Optional<MenuItemResponse> menuItem1 = Optional.of(new MenuItemResponse(1L, "Apple", 2.99));
        Optional<MenuItemResponse> menuItem2 = Optional.of(new MenuItemResponse(2L, "Coke", 1.99));
        Optional<MenuItemResponse> menuItem3 = Optional.of(new MenuItemResponse(3L, "Meat", 10.99));

        when(typeOfOrderRepository.findByType("On-site")).thenReturn(onsite);
        when(typeOfOrderRepository.findByType("Delivery")).thenReturn(delivery);
    }

    @Test
    public void shouldReturnOnsiteOrder(){
        // given
        MenuItemRequest menuItem1 = new MenuItemRequest(1L, "Apple", 2.99, "Food");
        MenuItemRequest menuItem2 = new MenuItemRequest(2L, "Coke", 1.99, "Drinks");
        OrderRequest orderDTO = new OrderRequest(1L, "On-site", List.of(menuItem1, menuItem2));

        // when
        Order order = orderFactory.createOrder(orderDTO).createOrder();

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
        MenuItemRequest menuItem = new MenuItemRequest(1L, "Meat", 10.99, "Food");
        OrderRequest orderDTO = new OrderRequest(1L, "Delivery", List.of(menuItem));

        // when
        Order order = orderFactory.createOrder(orderDTO).createOrder();

        // then
        assertEquals("Delivery", order.getTypeOfOrder().getType());
        assertEquals(10.99, order.getPrice());
        assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), order.getDate());
        assertNotNull(order.getHourOrder());
        assertEquals("Meat", order.getMenuItems().get(0).getName());
    }
}
