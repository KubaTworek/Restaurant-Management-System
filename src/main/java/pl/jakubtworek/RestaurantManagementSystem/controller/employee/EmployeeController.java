package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.EmployeeNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        List<Employee> employees = employeeService.findAll();

        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employees.forEach(e -> employeeDTOS.add(e.convertEntityToDTO()));

        employeeDTOS.forEach(dto -> dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(dto.getId()).withSelfRel()));

        return new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
    }

    @GetMapping("/id/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeService.findById(employeeId);

        if(employee.isPresent()) {
            Employee employeeFound = employee.get();
            EmployeeDTO dto = employeeFound.convertEntityToDTO();

            dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(dto.getId()).withSelfRel());
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }
        throw new EmployeeNotFoundException("There are employees in restaurant with that id: " + employeeId);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO dto, BindingResult result) throws JobNotFoundException {

        dto.setId(0L);
        employeeService.save(dto.convertDTOToEntity());
        dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(dto.getId()).withSelfRel());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) throws Exception {
        if(employeeService.findById(employeeId).isPresent()){
            employeeService.deleteById(employeeId);

            return new ResponseEntity<>("Deleted employee has id: " + employeeId, HttpStatus.OK);
        }
        throw new EmployeeNotFoundException("Employee id not found - " + employeeId);
    }

    @GetMapping("/job/{jobName}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeByJobName(@PathVariable String jobName) throws JobNotFoundException {

        if (employeeService.findJobByName(jobName).isPresent()) {
            List<Employee> employees = employeeService.findByJob(jobName);

            List<EmployeeDTO> employeeDTOS = new ArrayList<>();
            employees.forEach(e -> employeeDTOS.add(e.convertEntityToDTO()));

            employeeDTOS.forEach(dto -> dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(dto.getId()).withSelfRel()));

            return new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
        }

        throw new JobNotFoundException("There are no job like that in restaurant with that name: " + jobName);
    }
}