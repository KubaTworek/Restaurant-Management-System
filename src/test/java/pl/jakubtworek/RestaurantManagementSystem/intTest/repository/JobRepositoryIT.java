package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JobRepositoryIT {

    @Autowired
    private JobRepository jobRepository;

    @Test
    void shouldReturnCookJob_whenPassName() {
        // when
        Optional<Job> job = jobRepository.findByName("Cook");

        // then
        assertNotNull(job.get());
        assertEquals("Cook", job.get().getName());
        assertEquals(1, job.get().getEmployees().size());
        assertEquals(1, job.get().getEmployees().get(0).getId());
        assertEquals("John", job.get().getEmployees().get(0).getFirstName());
        assertEquals("Smith", job.get().getEmployees().get(0).getLastName());
        assertEquals(2, job.get().getEmployees().get(0).getOrders().size());
    }

    @Test
    void shouldReturnWaiterJob_whenPassName() {
        // when
        Optional<Job> job = jobRepository.findByName("Waiter");

        // then
        assertNotNull(job.get());
        assertEquals("Waiter", job.get().getName());
        assertEquals(1, job.get().getEmployees().size());
        assertEquals("James", job.get().getEmployees().get(0).getFirstName());
        assertEquals("Patel", job.get().getEmployees().get(0).getLastName());
        assertEquals(0, job.get().getEmployees().get(0).getOrders().size());
    }

    @Test
    void shouldReturnDeliveryManJob_whenPassName() {
        // when
        Optional<Job> job = jobRepository.findByName("DeliveryMan");

        // then
        assertNotNull(job.get());
        assertEquals("DeliveryMan", job.get().getName());
        assertEquals(1, job.get().getEmployees().size());
        assertEquals("Ann", job.get().getEmployees().get(0).getFirstName());
        assertEquals("Mary", job.get().getEmployees().get(0).getLastName());
        assertEquals(0, job.get().getEmployees().get(0).getOrders().size());
    }
}
