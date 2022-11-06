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
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        List<Employee> employeesFound = employeeService.findAll();
        List<EmployeeDTO> employeesDTO = createDTOList(employeesFound);

        return new ResponseEntity<>(employeesDTO, HttpStatus.OK);
    }

    @GetMapping("/id/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        if(employeeService.checkIfEmployeeIsNull(employeeId)) {
            throw new EmployeeNotFoundException("There are employees in restaurant with that id: " + employeeId);
        }

        Employee employeeFound = employeeService.findById(employeeId).get();
        EmployeeDTO dto = employeeFound.convertEntityToDTO();
        addLinkToDTO(dto);

        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO dto) {
        Employee employeeSaved = employeeService.save(dto);
        EmployeeDTO employeeDTO = employeeSaved.convertEntityToDTO();
        addLinkToDTO(employeeDTO);

        return new ResponseEntity<>(employeeDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        if(employeeService.checkIfEmployeeIsNull(employeeId)) {
            throw new EmployeeNotFoundException("There are employees in restaurant with that id: " + employeeId);
        }
        employeeService.deleteById(employeeId);

        return new ResponseEntity<>("Deleted employee has id: " + employeeId, HttpStatus.OK);
    }

    @GetMapping("/job/{jobName}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeByJobName(@PathVariable String jobName) throws JobNotFoundException {
        if(employeeService.checkIfJobIsNull(jobName)) {
            throw new JobNotFoundException("There are no job like that in restaurant with that name: " + jobName);
        }
        List<Employee> employeesFound = employeeService.findByJob(jobName);
        List<EmployeeDTO> employeesDTO = createDTOList(employeesFound);

        return new ResponseEntity<>(employeesDTO, HttpStatus.OK);
    }

    private List<EmployeeDTO> createDTOList(List<Employee> employeesEntity){
        List<EmployeeDTO> employeesDTO = employeesEntity
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());

        employeesDTO.forEach(this::addLinkToDTO);

        return employeesDTO;
    }

    private void addLinkToDTO(EmployeeDTO dto){
        dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(dto.getId()).withSelfRel());
    }
}