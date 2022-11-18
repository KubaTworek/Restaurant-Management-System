package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.spy;

public class EmployeeUtils {
    public static List<Employee> createEmployees(){
        Employee employee1 = spy(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()));
        Employee employee2 = spy(new Employee(2L, "James", "Patel", new Job(2L,"Waiter",List.of()), List.of()));
        Employee employee3 = spy(new Employee(3L, "Ann", "Mary", new Job(3L,"DeliveryMan",List.of()), List.of()));
        return List.of(employee1, employee2, employee3);
    }

    public static List<Employee> createCooks(){
        Employee cook = spy(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()));
        return List.of(cook);
    }

    public static Optional<Employee> createEmployee(){
        return Optional.of(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()));
    }

    public static Job createCook(){
        return new Job(1L,"Cook",List.of());
    }
    public static Job createWaiter(){
        return new Job(2L,"Waiter",List.of());
    }
    public static Job createDeliveryMan(){
        return spy(new Job(3L,"DeliveryMan",List.of()));
    }
}
