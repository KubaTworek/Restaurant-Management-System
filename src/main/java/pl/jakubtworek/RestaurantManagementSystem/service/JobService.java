package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;

import java.util.Optional;

public interface JobService {
    Optional<JobDTO> findByName(String name);
}
