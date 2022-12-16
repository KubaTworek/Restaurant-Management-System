package pl.jakubtworek.RestaurantManagementSystem.exception;

public class JobNotFoundException extends Exception {
    public JobNotFoundException() {
        super();
    }

    public JobNotFoundException(String msg) {
        super(msg);
    }

    public JobNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}