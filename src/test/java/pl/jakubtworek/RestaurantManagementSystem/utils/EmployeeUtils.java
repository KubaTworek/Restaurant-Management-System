package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeUtils {

    public static List<Employee> createEmployees() {
        Employee employee1 = createCook();
        Employee employee2 = createWaiter();
        Employee employee3 = createDelivery();
        return List.of(employee1, employee2, employee3);
    }

    public static Employee createCook() {
        return new Employee(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"), "John", "Smith", new Job(UUID.fromString("504ab5ae-7844-11ed-a1eb-0242ac120002"), "Cook", List.of()), List.of());
    }

    public static Employee createWaiter() {
        return new Employee(UUID.fromString("04b4f06c-7ad0-11ed-a1eb-0242ac120002"), "James", "Patel", new Job(UUID.fromString("504ab5ae-7844-11ed-a1eb-0242ac120002"), "Waiter", List.of()), List.of());
    }

    public static Employee createDelivery() {
        return new Employee(UUID.fromString("07df111e-7ad0-11ed-a1eb-0242ac120002"), "Ann", "Mary", new Job(UUID.fromString("504ab5ae-7844-11ed-a1eb-0242ac120002"), "DeliveryMan", List.of()), List.of());
    }

    public static List<Employee> createCooks() {
        return List.of(new Employee(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"), "John", "Smith", new Job(UUID.fromString("504ab5ae-7844-11ed-a1eb-0242ac120002"), "Cook", List.of(createCook())), List.of()));
    }

    public static Job createJobCook() {
        return new Job(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"), "Cook", new ArrayList<>(List.of(createCook())));
    }

    public static Job createJobWaiter() {
        return new Job(UUID.fromString("e62db8e2-7843-11ed-a1eb-0242ac120002"), "Waiter", List.of());
    }

    public static Job createJobDeliveryman() {
        return new Job(UUID.fromString("f2e9839a-7843-11ed-a1eb-0242ac120002"), "DeliveryMan", List.of());
    }

    public static EmployeeRequest createCookRequest() {
        return new EmployeeRequest("John", "Smith", "Cook");
    }

    public static EmployeeRequest createWaiterRequest() {
        return new EmployeeRequest("James", "Patel", "Waiter");
    }

    public static EmployeeRequest createDeliveryRequest() {
        return new EmployeeRequest("Ann", "Mary", "DeliveryMan");
    }

    public static class EmployeeAssertions {

        public static void checkAssertionsForEmployee(Employee employee) {
            assertEquals("John", employee.getFirstName());
            assertEquals("Smith", employee.getLastName());
            assertEquals("Cook", employee.getJob().getName());
        }

        public static void checkAssertionsForCooks(List<Employee> cooks) {
            assertEquals("John", cooks.get(0).getFirstName());
            assertEquals("Smith", cooks.get(0).getLastName());
            assertEquals("Cook", cooks.get(0).getJob().getName());
        }

        public static void checkAssertionsForEmployees(List<Employee> employees) {
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

    public static class EmployeeDTOAssertions {

        public static void checkAssertionsForEmployee(EmployeeDTO employee) {
            assertEquals("John", employee.getFirstName());
            assertEquals("Smith", employee.getLastName());
            assertEquals("Cook", employee.getJob().getName());
        }

        public static void checkAssertionsForCooks(List<EmployeeDTO> cooks) {
            assertEquals("John", cooks.get(0).getFirstName());
            assertEquals("Smith", cooks.get(0).getLastName());
            assertEquals("Cook", cooks.get(0).getJob().getName());
        }

        public static void checkAssertionsForEmployees(List<EmployeeDTO> employees) {
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

    public static class EmployeeResponseAssertions {

        public static void checkAssertionsForEmployee(EmployeeResponse employee) {
            assertEquals("John", employee.getFirstName());
            assertEquals("Smith", employee.getLastName());
            assertEquals("Cook", employee.getJob().getName());
        }

        public static void checkAssertionsForCooks(List<EmployeeResponse> cooks) {
            assertEquals("John", cooks.get(0).getFirstName());
            assertEquals("Smith", cooks.get(0).getLastName());
            assertEquals("Cook", cooks.get(0).getJob().getName());
        }

        public static void checkAssertionsForEmployees(List<EmployeeResponse> employees) {
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
