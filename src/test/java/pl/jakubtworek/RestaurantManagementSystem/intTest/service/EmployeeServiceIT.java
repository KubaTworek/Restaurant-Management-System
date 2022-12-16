package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EmployeeServiceIT {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TypeOfOrderRepository typeOfOrderRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JobRepository jobRepository;

    static private UUID idEmployee;

    @BeforeEach
    public void setup() throws MenuNotFoundException, JobNotFoundException {
        EmployeeRequest employeeRequest1 = createCookRequest();
        EmployeeRequest employeeRequest2 = createWaiterRequest();
        EmployeeRequest employeeRequest3 = createDeliveryRequest();

        Employee employee1 = employeeService.save(employeeRequest1).convertDTOToEntity();
        employeeService.save(employeeRequest2).convertDTOToEntity();
        employeeService.save(employeeRequest3).convertDTOToEntity();
        idEmployee = employee1.getId();

        Order onsiteOrder = new Order(null, 12.98, "2022-08-22", "12:00:00", "12:15:00", typeOfOrderRepository.findByType("On-site").get(), List.of(), List.of(employee1), null);
        Order deliveryOrder = new Order(null, 7.98, "2022-08-22", "12:05:00", null, typeOfOrderRepository.findByType("Delivery").get(), List.of(), List.of(employee1), null);

        orderRepository.save(onsiteOrder);
        orderRepository.save(deliveryOrder);
    }

    @Test
    void shouldReturnCreatedEmployee() throws JobNotFoundException {
        // given
        EmployeeRequest employee = createCookRequest();

        // when
        EmployeeDTO employeeCreated = employeeService.save(employee);
        List<EmployeeDTO> employeeDTOList = employeeService.findAll();

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployee(employeeCreated);
        assertEquals(4, employeeDTOList.size());
    }

    @Test
    void shouldDeleteEmployee() throws EmployeeNotFoundException {
        // when
        EmployeeDTO employeeDTO1 = employeeService.findById(idEmployee).orElse(null);
        employeeService.deleteById(idEmployee);
        EmployeeDTO employeeDTO2 = employeeService.findById(idEmployee).orElse(null);

        // then
        assertNotNull(employeeDTO1);
        assertNull(employeeDTO2);
    }

    @Test
    void shouldNotDeleteJobAndOrder_whenDeleteEmployee() throws EmployeeNotFoundException {
        // when
        long jobsBeforeDelete = jobRepository.count();
        long ordersBeforeDelete = orderRepository.count();
        employeeService.deleteById(idEmployee);
        long ordersAfterDelete = orderRepository.count();
        long jobsAfterDelete = jobRepository.count();

        // then
        assertEquals(2, ordersBeforeDelete);
        assertEquals(2, ordersAfterDelete);
        assertEquals(3, jobsBeforeDelete);
        assertEquals(3, jobsAfterDelete);
    }

    @Test
    void shouldReturnAllEmployees() {
        // when
        List<EmployeeDTO> employeesReturned = employeeService.findAll();

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    void shouldReturnEmployee_whenPassId() {
        // when
        EmployeeDTO employeeReturned = employeeService.findById(idEmployee).orElse(null);

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    void shouldReturnEmployees_whenPassJobName() throws JobNotFoundException {
        // when
        List<EmployeeDTO> employeesReturned = employeeService.findByJob("Cook");

        // then
        EmployeeDTOAssertions.checkAssertionsForCooks(employeesReturned);
    }
}