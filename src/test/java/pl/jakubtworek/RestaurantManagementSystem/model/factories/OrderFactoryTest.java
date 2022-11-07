package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.GetEmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.GetOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
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
        Optional<MenuItemDTO> menuItem1 = Optional.of(new MenuItemDTO(1L, "Apple", 2.99));
        Optional<MenuItemDTO> menuItem2 = Optional.of(new MenuItemDTO(2L, "Coke", 1.99));
        Optional<MenuItemDTO> menuItem3 = Optional.of(new MenuItemDTO(3L, "Meat", 10.99));

        when(typeOfOrderRepository.findByType("On-site")).thenReturn(onsite);
        when(typeOfOrderRepository.findByType("Delivery")).thenReturn(delivery);
    }

    @Test
    public void shouldReturnOnsiteOrder(){
        // given
        GetMenuItemDTO menuItem1 = new GetMenuItemDTO(1L, "Apple", 2.99, "Food");
        GetMenuItemDTO menuItem2 = new GetMenuItemDTO(2L, "Coke", 1.99, "Drinks");
        GetOrderDTO orderDTO = new GetOrderDTO(1L, "On-site", List.of(menuItem1, menuItem2));

        // when
        Order order = orderFactory.createOrder(orderDTO);

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
        GetMenuItemDTO menuItem = new GetMenuItemDTO(1L, "Meat", 10.99, "Food");
        GetOrderDTO orderDTO = new GetOrderDTO(1L, "Delivery", List.of(menuItem));

        // when
        Order order = orderFactory.createOrder(orderDTO);

        // then
        assertEquals("Delivery", order.getTypeOfOrder().getType());
        assertEquals(10.99, order.getPrice());
        assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), order.getDate());
        assertNotNull(order.getHourOrder());
        assertEquals("Meat", order.getMenuItems().get(0).getName());
    }
}
