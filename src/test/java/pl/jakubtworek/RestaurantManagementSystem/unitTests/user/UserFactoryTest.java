package pl.jakubtworek.RestaurantManagementSystem.unitTests.user;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.UserDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Authorities;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.UserFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserFactoryTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserFactory userFactory;

    @BeforeEach
    void setup(){
        passwordEncoder = mock(PasswordEncoder.class);

        userFactory = new UserFactory(
                passwordEncoder
        );
    }

    @Test
    void shouldReturnCreatedUser() {
        // given
        UserRequest userRequest = new UserRequest("user", "user", "user");
        Authorities authority = new Authorities(UUID.fromString("a1437b9c-798b-11ed-a1eb-0242ac120002"), "user", List.of());

        // when
        when(passwordEncoder.encode("user")).thenReturn("$2a$12$iWBt07UWbcIxuPpVUS2ssO78P9W0ezJUK5CW6c7b4X6PvT33vctv2");

        UserDTO userCreated = userFactory.createUser(userRequest, authority);

        assertEquals("user", userCreated.getUsername());
        assertEquals("$2a$12$iWBt07UWbcIxuPpVUS2ssO78P9W0ezJUK5CW6c7b4X6PvT33vctv2", userCreated.getPassword());
        assertEquals("user", userCreated.getAuthorities().getAuthority());
    }
}
