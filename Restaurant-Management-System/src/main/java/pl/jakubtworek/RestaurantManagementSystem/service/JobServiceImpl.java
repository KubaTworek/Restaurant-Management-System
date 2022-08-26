package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.JobDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.Job;

import java.util.List;

@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private JobDAO jobDAO;

    @Override
    public List<Job> findAll() {
        return null;
    }

    @Override
    public Job findById(int theId) {
        return null;
    }

    @Override
    public void save(Job theJob) {

    }

    @Override
    public void deleteById(int theId) {

    }

    @Override
    public Job findByName(String jobName) {
        return null;
    }
}
