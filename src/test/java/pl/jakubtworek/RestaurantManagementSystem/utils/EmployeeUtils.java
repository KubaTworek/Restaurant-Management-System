package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    public static class EmployeeAssertions<T extends Employee>{

        public static void checkAssertionsForEmployee(T employee){
            assertEquals("John", employee.getFirstName());
            assertEquals("Smith", employee.getLastName());
            assertEquals("Cook", employee.getJob().getName());
        }

        public static void checkAssertionsForCooks(List<T> cooks){
            assertEquals("John", cooks.get(0).getFirstName());
            assertEquals("Smith", cooks.get(0).getLastName());
            assertEquals("Cook", cooks.get(0).getJob().getName());
        }

        public static void checkAssertionsForEmployees(List<T> employees){
            assertEquals("John", employees.get(0).getFirstName());
            assertEquals("Smith", employees.get(0).getLastName());
            assertEquals("Cook", employees.get(0).getJob().getName());

            assertEquals("James", employees.get(1).getFirstName());
            assertEquals("Patel", employees.get(1).getLastName());
            assertEquals("Waiter", employees.get(1).getJob().getName());

            assertEquals("Ann", employees.get(2).getFirstName());
            assertEquals("Mary", employees.get(2).getLastName());
            assertEquals("DeliveryMan", employees.get(2).getJob().getName());
        }
    }

    public static class EmployeeDTOAssertions<T extends EmployeeDTO>{

        public static void checkAssertionsForEmployee(T employee){
            assertEquals("John", employee.getFirstName());
            assertEquals("Smith", employee.getLastName());
            assertEquals("Cook", employee.getJob().getName());
        }

        public static void checkAssertionsForCooks(List<T> cooks){
            assertEquals("John", cooks.get(0).getFirstName());
            assertEquals("Smith", cooks.get(0).getLastName());
            assertEquals("Cook", cooks.get(0).getJob().getName());
        }

        public static void checkAssertionsForEmployees(List<T> employees){
            assertEquals("John", employees.get(0).getFirstName());
            assertEquals("Smith", employees.get(0).getLastName());
            assertEquals("Cook", employees.get(0).getJob().getName());

            assertEquals("James", employees.get(1).getFirstName());
            assertEquals("Patel", employees.get(1).getLastName());
            assertEquals("Waiter", employees.get(1).getJob().getName());

            assertEquals("Ann", employees.get(2).getFirstName());
            assertEquals("Mary", employees.get(2).getLastName());
            assertEquals("DeliveryMan", employees.get(2).getJob().getName());
        }
    }

    public static class EmployeeResponseAssertions<T extends EmployeeResponse>{

        public static void checkAssertionsForEmployee(T employee){
            assertEquals("John", employee.getFirstName());
            assertEquals("Smith", employee.getLastName());
            assertEquals("Cook", employee.getJob().getName());
        }

        public static void checkAssertionsForCooks(List<T> cooks){
            assertEquals("John", cooks.get(0).getFirstName());
            assertEquals("Smith", cooks.get(0).getLastName());
            assertEquals("Cook", cooks.get(0).getJob().getName());
        }

        public static void checkAssertionsForEmployees(List<T> employees){
            assertEquals("John", employees.get(0).getFirstName());
            assertEquals("Smith", employees.get(0).getLastName());
            assertEquals("Cook", employees.get(0).getJob().getName());

            assertEquals("James", employees.get(1).getFirstName());
            assertEquals("Patel", employees.get(1).getLastName());
            assertEquals("Waiter", employees.get(1).getJob().getName());

            assertEquals("Ann", employees.get(2).getFirstName());
            assertEquals("Mary", employees.get(2).getLastName());
            assertEquals("DeliveryMan", employees.get(2).getJob().getName());
        }
    }
}
