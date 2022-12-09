package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.UserDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Authorities;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final PasswordEncoder passwordEncoder;

    public UserDTO createUser(
            UserRequest userRequest,
            Authorities authority
    ) {
        String username = userRequest.getUsername();
        String passwordEncrypted = passwordEncoder.encode(userRequest.getPassword());

        return UserDTO.builder()
                .username(username)
                .password(passwordEncrypted)
                .authorities(authority)
                .orders(null)
                .build();
    }
}
