package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.UserDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.UserFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserFactory userFactory;

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(User::convertEntityToDTO);
    }

    @Override
    public UserDTO save(UserRequest userRequest) {
        Authorities authority = getAuthority(userRequest.getRole());

        User userCreated = userFactory.createUser(userRequest, authority).convertDTOToEntity();
        return userRepository.save(userCreated).convertEntityToDTO();
    }

    private Authorities getAuthority(String authority){
        return authoritiesRepository.findAuthoritiesByAuthority(authority)
                .orElse(authoritiesRepository.findAuthoritiesByAuthority("user")
                .orElse(null));
    }
}
