package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFactory;
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
    private final EmployeeFactory employeeFactory;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, JobRepository jobRepository, EmployeeFactory employeeFactory) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
        this.employeeFactory = employeeFactory;
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
    public Employee save(EmployeeRequest employeeDTO) {
        Employee employee = employeeFactory.createEmployeeFormula(employeeDTO).createEmployee();
        employeeRepository.save(employee);
        return employee;
    }

    @Override
    public void deleteById(Long theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public List<Employee> findByJob(String jobName) {
        if (!checkIfJobIsNull(jobName)){
            Job job = findJobByName(jobName).get();
            return employeeRepository.findByJob(job);
        }
        return Collections.emptyList();
    }

    private Optional<Job> findJobByName(String jobName){
        return jobRepository.findByName(jobName);
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