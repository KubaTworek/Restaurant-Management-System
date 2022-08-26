package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

import java.util.List;

public interface JobService {
    List<Job> findAll();
    Job findById(int theId);
    void save(Job theJob);
    void deleteById(int theId);

    Job findByName(String jobName);
}
