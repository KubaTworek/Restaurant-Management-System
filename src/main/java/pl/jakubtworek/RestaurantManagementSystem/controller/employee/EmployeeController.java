package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.*;

import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> saveEmployee(@RequestBody EmployeeRequest employeeRequest) throws JobNotFoundException {
        EmployeeResponse employeeResponse = employeeService.save(employeeRequest).convertDTOToResponse();

        return new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable UUID id) throws EmployeeNotFoundException {
        employeeService.deleteById(id);

        return new ResponseEntity<>("Employee with id: " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        List<EmployeeResponse> employeesFound = employeeService.findAll()
                .stream()
                .map(EmployeeDTO::convertDTOToResponse)
                .map(EmployeeResponse::addLinkToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(employeesFound, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable UUID id) throws EmployeeNotFoundException {
        EmployeeResponse employeeResponse = employeeService.findById(id)
                .map(EmployeeDTO::convertDTOToResponse)
                .map(EmployeeResponse::addLinkToResponse)
                .orElseThrow(() -> new EmployeeNotFoundException("There are no employees in restaurant with that id: " + id));

        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    @GetMapping("/job")
    public ResponseEntity<List<EmployeeResponse>> getEmployeeByJobName(@RequestParam String jobName) throws JobNotFoundException {
        List<EmployeeResponse> employeesResponse = employeeService.findByJob(jobName)
                .stream()
                .map(EmployeeDTO::convertDTOToResponse)
                .map(EmployeeResponse::addLinkToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(employeesResponse, HttpStatus.OK);
    }
}