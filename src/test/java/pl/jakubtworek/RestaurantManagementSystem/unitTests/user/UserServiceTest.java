package pl.jakubtworek.RestaurantManagementSystem.unitTests.user;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.UserFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.UserService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.UserServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthoritiesRepository authoritiesRepository;
    @Mock
    private UserFactory userFactory;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        authoritiesRepository = mock(AuthoritiesRepository.class);
        userFactory = mock(UserFactory.class);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);

        userService = new UserServiceImpl(
                userRepository,
                authoritiesRepository,
                userFactory
        );
    }

    @Test
    void shouldReturnCreatedUser() {
        // given
        UserRequest userRequest = new UserRequest("user", "user", "user");
        AuthoritiesDTO authority = new AuthoritiesDTO(UUID.fromString("a1437b9c-798b-11ed-a1eb-0242ac120002"), "user", List.of());
        UserDTO userDTO = new UserDTO(UUID.fromString("9c9b3ed6-798b-11ed-a1eb-0242ac120002"), "user", "$2a$12$iWBt07UWbcIxuPpVUS2ssO78P9W0ezJUK5CW6c7b4X6PvT33vctv2", authority, List.of());

        // when
        when(authoritiesRepository.findAuthoritiesByAuthority("user")).thenReturn(Optional.of(authority.convertDTOToEntity()));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");

        when(userFactory.createUser(any(), any())).thenReturn(userDTO);
        when(userRepository.save(any())).thenReturn(userDTO.convertDTOToEntity());

        UserDTO userCreated = userService.save(userRequest);

        // then
        assertEquals("user", userCreated.getUsername());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedOrder() {
        // given
        Authorities authority = new Authorities(UUID.fromString("a1437b9c-798b-11ed-a1eb-0242ac120002"), "user", List.of());
        Optional<User> user = Optional.of(new User(UUID.fromString("9c9b3ed6-798b-11ed-a1eb-0242ac120002"), "user", "$2a$12$iWBt07UWbcIxuPpVUS2ssO78P9W0ezJUK5CW6c7b4X6PvT33vctv2", authority, List.of()));

        // when
        when(userRepository.findByUsername("user")).thenReturn(user);

        userService.deleteByUsername("user");

        // then
        verify(userRepository).delete(user.get());
    }
}