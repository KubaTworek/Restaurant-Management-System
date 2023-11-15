package pl.jakubtworek.auth;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    void shouldRestoreUserFromSnapshot() {
        // given
        final var snapshot = new UserSnapshot(1L, "john.doe", "password123", new HashSet<>());

        // when
        final var user = User.restore(snapshot);

        // then
        final var result = user.getSnapshot();
        assertEquals(snapshot.getId(), result.getId());
        assertEquals(snapshot.getUsername(), result.getUsername());
        assertEquals(snapshot.getPassword(), result.getPassword());
        assertEquals(snapshot.getOrders(), result.getOrders());
    }

    @Test
    void shouldUpdateUserInfo() {
        // given
        final var user = new User();

        // when
        user.updateInfo("john.doe", "password123");

        // then
        final var result = user.getSnapshot();
        assertEquals("john.doe", result.getUsername());
        assertEquals("password123", result.getPassword());
    }
}
