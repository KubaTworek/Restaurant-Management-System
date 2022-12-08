package pl.jakubtworek.RestaurantManagementSystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.User;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody LoginRequest userRequest){
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .authorities(authoritiesRepository.findAuthoritiesByAuthority(userRequest.getRole()).get())
                .orders(null)
                .build();
        ResponseEntity response = null;
        try{
            User savedUser = userRepository.save(user);
            if(savedUser.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("User successfully registered");
            }
        } catch (Exception e) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to " + e.getMessage());
        }
        return response;
    }
}
