package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

    @Autowired
    private CooksQueue cooksQueue;

    @Autowired
    private WaiterQueue waiterQueue;

    @Autowired
    private DeliveryQueue deliveryQueue;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(int theId) {
        Optional<Employee> result = employeeRepository.findById(theId);

        Employee theEmployee = null;

        if (result.isPresent()) {
            theEmployee = result.get();
        }

        return theEmployee;
    }

    @Override
    public void save(Employee theEmployee) {
        employeeRepository.save(theEmployee);
        if(theEmployee.getJob().getId()==1) {
            cooksQueue.add(theEmployee);
        }
        if(theEmployee.getJob().getId()==2) {
            waiterQueue.add(theEmployee);
        }
        if(theEmployee.getJob().getId()==3) {
            deliveryQueue.add(theEmployee);
        }
    }

    @Override
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public List<Employee> findByJob(String jobName) {
        return employeeRepository.findByJob(findJobByName(jobName));
    }

    @Override
    public Job findJobByName(String jobName){
        return jobRepository.findByName(jobName);
    }

    @Override
    public void addCooksToKitchen(){
        for(Employee employee : findByJob("Cook")){
            cooksQueue.add(employee);
        }
    }

    @Override
    public void addWaitersToKitchen(){
        for(Employee employee : findByJob("Waiter")){
            waiterQueue.add(employee);
        }
    }

    @Override
    public void addDeliveriesToKitchen(){
        for(Employee employee : findByJob("DeliveryMan")){
            deliveryQueue.add(employee);
        }
    }
}
