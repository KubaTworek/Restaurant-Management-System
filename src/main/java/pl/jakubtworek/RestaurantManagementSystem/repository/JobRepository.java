package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.*;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    Optional<Job> findByName(String jobName);
}
