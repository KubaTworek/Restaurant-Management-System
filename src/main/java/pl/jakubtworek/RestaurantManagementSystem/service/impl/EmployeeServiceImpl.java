package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.EmployeeQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final EmployeeFactory employeeFactory;
    private final EmployeeQueueFacade employeeQueueFacade;

    @Override
    public EmployeeDTO save(EmployeeRequest employeeRequest) throws JobNotFoundException {
        JobDTO jobDTO = getJobDTO(employeeRequest.getJob());

        Employee employee = employeeFactory.createEmployee(employeeRequest, jobDTO).convertDTOToEntity();
        Job job = jobRepository.getReferenceById(jobDTO.getId());
        job.add(employee);
        EmployeeDTO employeeCreated = employeeRepository.save(employee).convertEntityToDTO();
        employeeQueueFacade.addEmployeeToProperQueue(employeeCreated);

        return employeeCreated;
    }

    @Override
    public void deleteById(UUID theId) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(theId)
                .orElseThrow(() -> new EmployeeNotFoundException("There are no employees in restaurant with that id: " + theId));
        employee.remove();
        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeDTO> findById(UUID theId) {
        return employeeRepository.findById(theId).map(Employee::convertEntityToDTO);
    }

    @Override
    public List<EmployeeDTO> findByJob(String jobName) throws JobNotFoundException {
        Job jobFound = jobRepository.findByName(jobName)
                .orElseThrow(() -> new JobNotFoundException("There are no job in restaurant with that name: " + jobName));

        return jobFound.getEmployees()
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    private JobDTO getJobDTO(String jobName) throws JobNotFoundException {
        return jobRepository.findByName(jobName)
                .orElseThrow(() -> new JobNotFoundException("There are no job in restaurant with that name: " + jobName))
                .convertEntityToDTO();
    }
}