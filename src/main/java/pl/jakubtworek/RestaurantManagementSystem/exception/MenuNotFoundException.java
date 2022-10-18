package pl.jakubtworek.RestaurantManagementSystem.exception;

public class MenuNotFoundException extends Exception{
    public MenuNotFoundException(){
        super();
    }

    public MenuNotFoundException(String msg){
        super(msg);
    }

    public MenuNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}