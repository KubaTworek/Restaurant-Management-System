package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.List;

public class UserUtils {
    public static User createAdmin(){
        return new User(1L, "admin", "admin", createAdminRole(), List.of());
    }

    public static User createUser(){
        return new User(2L, "user", "user", createUserRole(), List.of());
    }

    public static Authorities createAdminRole(){
        return new Authorities(1L, "admin", List.of());
    }

    public static Authorities createUserRole(){
        return new Authorities(2L, "user", List.of());
    }
}
