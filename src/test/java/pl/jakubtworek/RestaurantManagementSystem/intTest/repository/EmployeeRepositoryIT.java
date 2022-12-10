package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

@SpringBootTest
@Transactional
public class EmployeeRepositoryIT {

    @Autowired
    private EmployeeRepository employeeRepository;

    /*    @Test
    @Sql({"/deleting-data.sql", "/data.sql"})
    public void shouldReturnCreatedEmployee(){

    }*/

    @Test
    void shouldReturnLowerSizeOfList_whenDeleteOne(){
        // when
        employeeRepository.deleteById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"));
        List<Employee> employees = employeeRepository.findAll();

        // then
        assertEquals(2, employees.size());
    }

    @Test
    public void shouldReturnAllEmployees(){
        // when
        List<Employee> employeesReturned = employeeRepository.findAll();

        // then
        EmployeeAssertions.checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    public void shouldReturnOneEmployee(){
        // when
        Employee employeeReturned = employeeRepository.findById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        EmployeeAssertions.checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    void shouldReturnEmployees_whenJobNamePass(){
        // given
        Job job = createJobCook();

        // when
        List<Employee> employeesReturned = employeeRepository.findByJob(job);

        // then
        EmployeeAssertions.checkAssertionsForCooks(employeesReturned);
    }
}
