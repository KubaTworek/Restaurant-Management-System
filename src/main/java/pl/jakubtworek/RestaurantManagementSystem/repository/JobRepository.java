package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

@Repository

public interface JobRepository extends JpaRepository<Job, Integer> {
    Job findByName(String jobName);
}
