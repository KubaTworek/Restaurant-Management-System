package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.EmployeeNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        List<Employee> employeesFound = employeeService.findAll();
        List<EmployeeResponse> employeesResponse = createResponseList(employeesFound);

        return new ResponseEntity<>(employeesResponse, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@RequestParam Long id) throws EmployeeNotFoundException {
        if(employeeService.checkIfEmployeeIsNull(id)) {
            throw new EmployeeNotFoundException("There are no employees in restaurant with that id: " + id);
        }

        Employee employeeFound = employeeService.findById(id).get();
        EmployeeResponse response = employeeFound.convertEntityToResponse();
        addLinkToResponse(response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> saveEmployee(@RequestBody EmployeeRequest dto) {
        Employee employeeSaved = employeeService.save(dto);
        EmployeeResponse response = employeeSaved.convertEntityToResponse();
        addLinkToResponse(response);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteEmployee(@RequestParam Long id) throws EmployeeNotFoundException {
        if(employeeService.checkIfEmployeeIsNull(id)) {
            throw new EmployeeNotFoundException("There are no employees in restaurant with that id: " + id);
        }
        employeeService.deleteById(id);

        return new ResponseEntity<>("Deleted employee has id: " + id, HttpStatus.OK);
    }

    @GetMapping("/job")
    public ResponseEntity<List<EmployeeResponse>> getEmployeeByJobName(@RequestParam String job) throws JobNotFoundException {
        if(employeeService.checkIfJobIsNull(job)) {
            throw new JobNotFoundException("There are no job like that in restaurant with that name: " + job);
        }
        List<Employee> employeesFound = employeeService.findByJob(job);
        List<EmployeeResponse> employeesResponse = createResponseList(employeesFound);

        return new ResponseEntity<>(employeesResponse, HttpStatus.OK);
    }

    private List<EmployeeResponse> createResponseList(List<Employee> employeesEntity){
        List<EmployeeResponse> employeesResponse = employeesEntity
                .stream()
                .map(Employee::convertEntityToResponse)
                .collect(Collectors.toList());

        employeesResponse.forEach(this::addLinkToResponse);

        return employeesResponse;
    }

    private void addLinkToResponse(EmployeeResponse dto){
        dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(dto.getId()).withSelfRel());
    }
}