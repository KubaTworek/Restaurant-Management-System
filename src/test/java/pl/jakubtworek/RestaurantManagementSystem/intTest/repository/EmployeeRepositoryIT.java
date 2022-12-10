package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

@SpringBootTest
@Transactional
public class EmployeeRepositoryIT {

    @Autowired
    private EmployeeRepository employeeRepository;

    /*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnCreatedEmployee(){

    }*/

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne(){
        // when
        employeeRepository.deleteById(2L);
        List<Employee> employees = employeeRepository.findAll();

        // then
        assertEquals(2, employees.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnAllEmployees(){
        // when
        List<Employee> employeesReturned = employeeRepository.findAll();

        // then
        EmployeeAssertions.checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOneEmployee(){
        // when
        Employee employeeReturned = employeeRepository.findById(1L).orElse(null);

        // then
        EmployeeAssertions.checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployees_whenJobNamePass(){
        // given
        Job job = createJobCook();

        // when
        List<Employee> employeesReturned = employeeRepository.findByJob(job);

        // then
        EmployeeAssertions.checkAssertionsForCooks(employeesReturned);
    }
}
