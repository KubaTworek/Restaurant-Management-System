package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TypeOfOrderRepositoryIT {

    @Autowired
    private TypeOfOrderRepository typeOfOrderRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup(){
        TypeOfOrder onsite = new TypeOfOrder(null, "On-site", List.of());
        TypeOfOrder delivery = new TypeOfOrder(null, "Delivery", List.of());
        typeOfOrderRepository.save(onsite);
        typeOfOrderRepository.save(delivery);
        typeOfOrderRepository.flush();

        Menu menu1 = new Menu(null, "Food", List.of());
        Menu menu2 = new Menu(null, "Drinks", List.of());

        MenuItem menuItem1 = new MenuItem(null, "Chicken", 10.99, menu1, List.of());
        MenuItem menuItem2 = new MenuItem(null, "Coke", 1.99, menu2, List.of());
        MenuItem menuItem3 = new MenuItem(null, "Tiramisu", 5.99, menu1, List.of());

        Job job1 = new Job(null, "Cook", List.of());
        Job job2 = new Job(null, "Waiter", List.of());
        Job job3 = new Job(null, "DeliveryMan", List.of());

        Employee employee1 = new Employee(null, "John", "Smith", job1, List.of());
        Employee employee2 = new Employee(null, "James", "Patel", job2, List.of());
        Employee employee3 = new Employee(null, "Mary", "Ann", job3, List.of());

        Order onsiteOrder = new Order(null, 12.98, "2022-08-22", "12:00:00", "12:15:00", typeOfOrderRepository.findByType("On-site").get(), List.of(menuItem1, menuItem2), List.of(employee1), null);
        Order deliveryOrder = new Order(null, 7.98, "2022-08-22", "12:05:00", null, typeOfOrderRepository.findByType("Delivery").get(), List.of(menuItem3, menuItem2), List.of(employee1), null);

        menuRepository.save(menu1);
        menuRepository.save(menu2);
        menuRepository.flush();

        menuItemRepository.save(menuItem1);
        menuItemRepository.save(menuItem2);
        menuItemRepository.save(menuItem3);
        menuItemRepository.flush();

        jobRepository.save(job1);
        jobRepository.save(job2);
        jobRepository.save(job3);
        jobRepository.flush();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
        employeeRepository.flush();

        orderRepository.save(onsiteOrder);
        orderRepository.save(deliveryOrder);
        orderRepository.flush();
    }

    @Test
    void shouldReturnOnSiteTypeOfOrder_whenPassOnSiteString(){
        // when
        Optional<TypeOfOrder> typeOfOrder = typeOfOrderRepository.findByType("On-site");

        // then
        assertNotNull(typeOfOrder.get());
        assertEquals("On-site", typeOfOrder.get().getType());
    }

    @Test
    void shouldReturnDeliveryTypeOfOrder_whenPassDeliveryString(){
        // when
        Optional<TypeOfOrder> typeOfOrder = typeOfOrderRepository.findByType("Delivery");

        // then
        assertNotNull(typeOfOrder.get());
        assertEquals("Delivery", typeOfOrder.get().getType());
    }
}
