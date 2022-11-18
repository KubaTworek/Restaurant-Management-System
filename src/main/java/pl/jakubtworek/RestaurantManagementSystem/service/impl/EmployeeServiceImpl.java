package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final EmployeeFactory employeeFactory;
    private final CooksQueue cooksQueue;
    private final WaiterQueue waiterQueue;
    private final DeliveryQueue deliveryQueue;

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
        Job job = findJobByName(employeeDTO.getJob()).orElseThrow();
        EmployeeDTO dto = createEmployee(employeeDTO, job);
        Employee employee = dto.convertDTOToEntity();
        Employee employeeCreated = employeeRepository.save(employee);
        pushEmployeeToProperQueue(employeeCreated);
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

    private void pushEmployeeToProperQueue(Employee employee){
        EmployeeDTO employeeDTOx = employee.convertEntityToDTO();
        if(Objects.equals(employeeDTOx.getJob().getName(), "Cook")) cooksQueue.add(employeeDTOx);
        if(Objects.equals(employeeDTOx.getJob().getName(), "Waiter")) waiterQueue.add(employeeDTOx);
        if(Objects.equals(employeeDTOx.getJob().getName(), "DeliveryMan")) deliveryQueue.add(employeeDTOx);
    }

    private EmployeeDTO createEmployee(EmployeeRequest employeeDTO, Job job){
        return employeeFactory.createEmployeeFormula(employeeDTO, job).createEmployee();
    }
}