package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.List;

public class EmployeeUtils {
    public static List<Employee> createEmployees(){
        Employee employee1 = new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of());
        Employee employee2 = new Employee(2L, "James", "Patel", new Job(2L,"Waiter",List.of()), List.of());
        Employee employee3 = new Employee(3L, "Ann", "Mary", new Job(3L,"DeliveryMan",List.of()), List.of());
        return List.of(employee1, employee2, employee3);
    }

    public static Employee createCook(){
        return new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of());
    }

    public static List<Employee> createCooks(){
        return List.of(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()));
    }

    public static Job createJobCook(){
        return new Job(1L, "Cook", List.of());
    }
    public static Job createJobWaiter(){
        return new Job(2L, "Waiter", List.of());
    }
    public static Job createJobDeliveryman(){
        return new Job(3L, "DeliveryMan", List.of());
    }

    public static EmployeeRequest createCookRequest(){
        return new EmployeeRequest("James", "Smith", "Cook");
    }
    public static EmployeeRequest createWaiterRequest(){
        return new EmployeeRequest("James", "Smith", "Waiter");
    }
    public static EmployeeRequest createDeliveryRequest(){
        return new EmployeeRequest("James", "Smith", "DeliveryMan");
    }
}
