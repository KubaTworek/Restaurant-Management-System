package pl.jakubtworek.RestaurantManagementSystem.exception;

public class MenuItemNotFoundException extends Exception{
    public MenuItemNotFoundException(){
        super();
    }

    public MenuItemNotFoundException(String msg){
        super(msg);
    }

    public MenuItemNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}