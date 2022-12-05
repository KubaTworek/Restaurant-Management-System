package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.EmployeeNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;

import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;
import pl.jakubtworek.RestaurantManagementSystem.service.JobService;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {

        List<EmployeeResponse> employeesFound = employeeService.findAll()
                .stream()
                .map(EmployeeDTO::convertDTOToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(employeesFound, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@RequestParam Long id) throws EmployeeNotFoundException {

        EmployeeResponse employeeResponse = employeeService.findById(id)
                .map(EmployeeDTO::convertDTOToResponse)
                .orElseThrow(() -> new EmployeeNotFoundException("There are no employees in restaurant with that id: " + id));

        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> saveEmployee(@RequestBody EmployeeRequest employeeRequest) throws JobNotFoundException {

        JobDTO jobDTO = jobService.findByName(employeeRequest.getJob()).orElseThrow(JobNotFoundException::new);
        EmployeeResponse employeeResponse = employeeService.save(employeeRequest, jobDTO).convertDTOToResponse();

        return new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/id")
    public ResponseEntity<EmployeeResponse> deleteEmployee(@RequestParam Long id) throws EmployeeNotFoundException {

        EmployeeResponse employeeResponse = employeeService.findById(id)
                .map(EmployeeDTO::convertDTOToResponse)
                .orElseThrow(() -> new EmployeeNotFoundException("There are no employees in restaurant with that id: " + id));

        employeeService.deleteById(id);

        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    @GetMapping("/job")
    public ResponseEntity<List<EmployeeResponse>> getEmployeeByJobName(@RequestParam String jobName) {

        Job jobFound = jobService.findByName(jobName).map(JobDTO::convertDTOToEntity).orElse(null);

        List<EmployeeResponse> employeesResponse = employeeService.findByJob(jobFound)
                .stream()
                .map(EmployeeDTO::convertDTOToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(employeesResponse, HttpStatus.OK);
    }
}