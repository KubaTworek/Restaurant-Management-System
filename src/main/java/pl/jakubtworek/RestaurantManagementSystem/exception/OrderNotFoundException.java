package pl.jakubtworek.RestaurantManagementSystem.exception;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(){
        super();
    }

    public OrderNotFoundException(String msg){
        super(msg);
    }

    public OrderNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}