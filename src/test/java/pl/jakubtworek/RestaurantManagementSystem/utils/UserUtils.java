package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;

public class UserUtils {
    public static User createAdmin() {
        return new User(UUID.fromString("edd25302-7846-11ed-a1eb-0242ac120002"), "admin", "admin", createAdminRole(), List.of());
    }

    public static User createUser() {
        return new User(UUID.fromString("f050048a-7846-11ed-a1eb-0242ac120002"), "user", "user", createUserRole(), List.of());
    }

    public static Authorities createAdminRole() {
        return new Authorities(UUID.fromString("dc2f8084-7846-11ed-a1eb-0242ac120002"), "admin", List.of());
    }

    public static Authorities createUserRole() {
        return new Authorities(UUID.fromString("df24bfc0-7846-11ed-a1eb-0242ac120002"), "user", List.of());
    }
}
