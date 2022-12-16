package pl.jakubtworek.RestaurantManagementSystem.exception;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException() {
        super();
    }

    public EmployeeNotFoundException(String msg) {
        super(msg);
    }

    public EmployeeNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}