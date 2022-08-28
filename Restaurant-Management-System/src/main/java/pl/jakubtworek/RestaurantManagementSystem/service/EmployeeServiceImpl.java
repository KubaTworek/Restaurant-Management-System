package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.EmployeeDAO;
import pl.jakubtworek.RestaurantManagementSystem.dao.JobDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.model.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.Kitchen;
import pl.jakubtworek.RestaurantManagementSystem.model.OrdersQueue;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeDAO employeeDAO;
    private final JobDAO jobDAO;

    public EmployeeServiceImpl(EmployeeDAO employeeDAO, JobDAO jobDAO) {
        this.employeeDAO = employeeDAO;
        this.jobDAO = jobDAO;
    }

    @Autowired
    private CooksQueue cooksQueue;

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee findById(int theId) {
        Optional<Employee> result = employeeDAO.findById(theId);

        Employee theEmployee = null;

        if (result.isPresent()) {
            theEmployee = result.get();
        }

        return theEmployee;
    }

    @Override
    public void save(Employee theEmployee) {
        employeeDAO.save(theEmployee);
        if(theEmployee.getJob().getId()==1) {
            cooksQueue.add(theEmployee);
        }
    }

    @Override
    public void deleteById(int theId) {
        employeeDAO.deleteById(theId);
    }

    @Override
    public List<Employee> findByJob(String jobName) {
        return employeeDAO.findByJob(findJobByName(jobName));
    }

    @Override
    public Job findJobByName(String jobName){
        return jobDAO.findByName(jobName);
    }

    @Override
    public void addCooksToKitchen(){
        for(Employee employee : findByJob("Cook")){
            cooksQueue.add(employee);
        }
    }
}
