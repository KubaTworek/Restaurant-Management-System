package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeFactory employeeFactory;
    private final CooksQueue cooksQueue;
    private final WaiterQueue waiterQueue;
    private final DeliveryQueue deliveryQueue;

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
    public EmployeeDTO save(EmployeeRequest employeeRequest, JobDTO jobDTO) throws JobNotFoundException {
        EmployeeDTO employeeDTO = createEmployee(employeeRequest, jobDTO);
        Employee employee = employeeDTO.convertDTOToEntity();
        Employee employeeCreated = employeeRepository.save(employee);
        pushEmployeeToProperQueue(employeeCreated);
        return employeeCreated.convertEntityToDTO();
    }

    @Override
    public void deleteById(Long theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public List<EmployeeDTO> findByJob(Job theJob) {
        return employeeRepository.findByJob(theJob)
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    private void pushEmployeeToProperQueue(Employee employee){
        EmployeeDTO employeeDTOx = employee.convertEntityToDTO();
        if(Objects.equals(employeeDTOx.getJob().getName(), "Cook")) cooksQueue.add(employeeDTOx);
        if(Objects.equals(employeeDTOx.getJob().getName(), "Waiter")) waiterQueue.add(employeeDTOx);
        if(Objects.equals(employeeDTOx.getJob().getName(), "DeliveryMan")) deliveryQueue.add(employeeDTOx);
    }

    private EmployeeDTO createEmployee(EmployeeRequest employeeRequest, JobDTO jobDTO){
        return employeeFactory.createEmployeeFormula(employeeRequest, jobDTO).createEmployee();
    }
}