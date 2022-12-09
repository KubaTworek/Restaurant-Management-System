package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.EmployeeQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final EmployeeFactory employeeFactory;
    private final EmployeeQueueFacade employeeQueueFacade;

    @Override
    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeDTO> findById(Long theId) {
        return employeeRepository.findById(theId).map(Employee::convertEntityToDTO);
    }

    @Override
    public EmployeeDTO save(EmployeeRequest employeeRequest) throws JobNotFoundException {
        String jobName = employeeRequest.getJob();
        JobDTO jobDTO = jobRepository.findByName(jobName)
                .orElseThrow(() -> new JobNotFoundException("There are no job in restaurant with that name: " + jobName))
                .convertEntityToDTO();

        Employee employee = employeeFactory.createEmployee(employeeRequest, jobDTO).convertDTOToEntity();
        EmployeeDTO employeeCreated = employeeRepository.save(employee).convertEntityToDTO();
        employeeQueueFacade.addEmployeeToProperQueue(employeeCreated);

        return employeeCreated;
    }

    @Override
    public void deleteById(Long theId) {
        employeeRepository.findById(theId).orElseThrow().remove();
        employeeRepository.deleteById(theId);
    }

    @Override
    public List<EmployeeDTO> findByJob(String jobName) throws JobNotFoundException {
        Job jobFound = jobRepository.findByName(jobName)
                .orElseThrow(() -> new JobNotFoundException("There are no job in restaurant with that name: " + jobName));

        return employeeRepository.findByJob(jobFound)
                .orElse(Collections.emptyList())
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());
    }
}