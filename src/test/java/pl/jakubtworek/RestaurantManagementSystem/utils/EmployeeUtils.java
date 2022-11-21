package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.List;
import java.util.Optional;

public class EmployeeUtils {
    public static List<Employee> createEmployees(){
        Employee employee1 = new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of());
        Employee employee2 = new Employee(2L, "James", "Patel", new Job(2L,"Waiter",List.of()), List.of());
        Employee employee3 = new Employee(3L, "Ann", "Mary", new Job(3L,"DeliveryMan",List.of()), List.of());
        return List.of(employee1, employee2, employee3);
    }

    public static List<EmployeeDTO> createEmployeesDTO(){
        EmployeeDTO employee1 = new EmployeeDTO(1L, "John", "Smith", createCookDTO(), List.of());
        EmployeeDTO employee2 = new EmployeeDTO(2L, "James", "Patel", createWaiterDTO(), List.of());
        EmployeeDTO employee3 = new EmployeeDTO(3L, "Ann", "Mary", createDeliveryManDTO(), List.of());
        return List.of(employee1, employee2, employee3);
    }

    public static Optional<Employee> createCooks(){
        return Optional.of(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()));
    }

    public static Optional<EmployeeDTO> createCooksDTO(){
        return Optional.of(new EmployeeDTO(1L, "John", "Smith", createCookDTO(), List.of()));
    }

    public static Optional<Employee> createEmployee(){
        return Optional.of(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()));
    }

    public static Optional<EmployeeDTO> createEmployeeDTO(){
        return Optional.of(new EmployeeDTO(1L, "John", "Smith", createCookDTO(), List.of()));
    }

    public static EmployeeRequest createCookRequest(){
        return new EmployeeRequest("James", "Smith", "Cook");
    }

    public static EmployeeRequest createWaiterRequest(){
        return new EmployeeRequest("James", "Smith", "Waiter");
    }

    public static EmployeeRequest createDeliveryManRequest(){
        return new EmployeeRequest("James", "Smith", "DeliveryMan");
    }

    public static Optional<Job> createCook(){
        return Optional.of(new Job(1L, "Cook", List.of()));
    }
    public static JobDTO createCookDTO(){
        return new JobDTO(1L, "Cook", List.of());
    }
    public static Optional<Job> createWaiter(){
        return Optional.of(new Job(2L,"Waiter",List.of()));
    }
    public static JobDTO createWaiterDTO(){
        return new JobDTO(2L,"Waiter",List.of());
    }
    public static Optional<Job> createDeliveryMan(){
        return Optional.of(new Job(3L,"DeliveryMan",List.of()));
    }
    public static JobDTO createDeliveryManDTO(){
        return new JobDTO(3L,"DeliveryMan",List.of());
    }
}
