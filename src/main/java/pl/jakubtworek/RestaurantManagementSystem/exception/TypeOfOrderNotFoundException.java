package pl.jakubtworek.RestaurantManagementSystem.exception;

public class TypeOfOrderNotFoundException extends Exception{
    public TypeOfOrderNotFoundException(){
        super();
    }

    public TypeOfOrderNotFoundException(String msg){
        super(msg);
    }

    public TypeOfOrderNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}