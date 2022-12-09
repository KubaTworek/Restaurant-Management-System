package pl.jakubtworek.RestaurantManagementSystem.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest){
        UserResponse userCreated = userService.save(userRequest).convertDTOToResponse();

        return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
    }
}
