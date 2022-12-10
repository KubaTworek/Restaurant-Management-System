package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.MenuAssertions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenu;

@SpringBootTest
@Transactional
class MenuRepositoryIT {

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

    static private UUID idToDelete = UUID.randomUUID();

    @BeforeEach
    public void setup(){
        TypeOfOrder onsite = new TypeOfOrder(null, "On-site", List.of());
        TypeOfOrder delivery = new TypeOfOrder(null, "Delivery", List.of());
        typeOfOrderRepository.save(onsite);
        typeOfOrderRepository.save(delivery);
        typeOfOrderRepository.flush();

        Menu menu1 = new Menu(null, "Drinks", List.of());
        Menu menu2 = new Menu(null, "Food", List.of());
        idToDelete = menuRepository.save(menu1).getId();
        menuRepository.save(menu2);
        menuRepository.flush();

        MenuItem menuItem1 = new MenuItem(null, "Chicken", 10.99, menuRepository.findByName("Food").get(), List.of());
        MenuItem menuItem2 = new MenuItem(null, "Coke", 1.99, menuRepository.findByName("Drinks").get(), List.of());
        MenuItem menuItem3 = new MenuItem(null, "Tiramisu", 5.99, menuRepository.findByName("Food").get(), List.of());

        Job job1 = new Job(null, "Cook", List.of());
        Job job2 = new Job(null, "Waiter", List.of());
        Job job3 = new Job(null, "DeliveryMan", List.of());

        Employee employee1 = new Employee(null, "John", "Smith", job1, List.of());
        Employee employee2 = new Employee(null, "James", "Patel", job2, List.of());
        Employee employee3 = new Employee(null, "Mary", "Ann", job3, List.of());

        Order onsiteOrder = new Order(null, 12.98, "2022-08-22", "12:00:00", "12:15:00", typeOfOrderRepository.findByType("On-site").get(), List.of(menuItem1, menuItem2), List.of(employee1), null);
        Order deliveryOrder = new Order(null, 7.98, "2022-08-22", "12:05:00", null, typeOfOrderRepository.findByType("Delivery").get(), List.of(menuItem3, menuItem2), List.of(employee1), null);




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
    void shouldReturnCreatedMenu(){
        // given
        Menu menu = createMenu();

        // when
        Menu menuCreated = menuRepository.save(menu);

        // then
        assertEquals("Food", menuCreated.getName());
    }

    @Test
    void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        Menu menu1 = menuRepository.findById(idToDelete).orElse(null);
        menuRepository.deleteById(idToDelete);
        Menu menu2 = menuRepository.findById(idToDelete).orElse(null);

        // then
        assertNotNull(menu1);
        assertNull(menu2);
    }

    @Test
    void shouldReturnAllMenu() {
        // when
        List<Menu> menuListReturned = menuRepository.findAll();
        for(Menu menu : menuListReturned){
            System.out.println(menu.getId());
        }

        // then
        MenuAssertions.checkAssertionsForMenus(menuListReturned);
    }

    @Test
    void shouldReturnOneMenu_whenPassId() {
        // when
        Menu menuReturned = menuRepository.findById(UUID.fromString("340a81aa-7847-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        MenuAssertions.checkAssertionsForMenu(menuReturned);
    }

    @Test
    void shouldReturnOneMenu_whenPassName() {
        // when
        Menu menuReturned = menuRepository.findByName("Food").orElse(null);

        // then
        MenuAssertions.checkAssertionsForMenu(menuReturned);
    }
}
