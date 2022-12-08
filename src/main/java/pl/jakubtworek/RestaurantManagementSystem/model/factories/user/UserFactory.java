package pl.jakubtworek.RestaurantManagementSystem.model.factories.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Authorities;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final PasswordEncoder passwordEncoder;
    public UserFormula createUser(
            UserRequest userRequest,
            Authorities authority
    ){
        return new UserFormulaImpl(userRequest, authority, passwordEncoder);
    }
}
