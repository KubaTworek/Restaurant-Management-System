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
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.*;

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
        Optional<Authorities> authority = Optional.of(createUserRole());
        UserDTO userDTO = createUser().convertEntityToDTO();

        // when
        when(authoritiesRepository.findAuthoritiesByAuthority("user")).thenReturn(authority);
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
        Optional<User> user = Optional.of(createUser());

        // when
        when(userRepository.findByUsername("user")).thenReturn(user);

        userService.deleteByUsername("user");

        // then
        verify(userRepository).delete(user.get());
    }
}