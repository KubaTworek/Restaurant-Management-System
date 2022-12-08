package pl.jakubtworek.RestaurantManagementSystem.model.factories.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.UserDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

@RequiredArgsConstructor
public class UserFormulaImpl implements UserFormula{
    private final UserRequest userRequest;
    private final Authorities authority;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser() {
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
