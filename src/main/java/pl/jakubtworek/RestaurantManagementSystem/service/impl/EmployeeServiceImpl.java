package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final CooksQueue cooksQueue;
    private final WaiterQueue waiterQueue;
    private final DeliveryQueue deliveryQueue;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, JobRepository jobRepository, CooksQueue cooksQueue, WaiterQueue waiterQueue, DeliveryQueue deliveryQueue) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
        this.cooksQueue = cooksQueue;
        this.waiterQueue = waiterQueue;
        this.deliveryQueue = deliveryQueue;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long theId) {
        return employeeRepository.findById(theId);
    }

    @Override
    public Employee save(Employee theEmployee) {
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
        return theEmployee;
    }

    @Override
    public void deleteById(Long theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public List<Employee> findByJob(String jobName) {
        if (!checkIfJobIsNull(jobName)){
            return employeeRepository.findByJob(findJobByName(jobName).get());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Job> findJobByName(String jobName){
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

    @Override
    public boolean checkIfEmployeeIsNull(Long id){
        return findById(id).isEmpty();
    }

    @Override
    public boolean checkIfJobIsNull(String name){
        return findJobByName(name).isEmpty();
    }
}