package pl.jakubtworek.RestaurantManagementSystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    @Autowired
    private JdbcTemplate jdbc;


    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees(){
        return employeeService.findAll();
    }

    @GetMapping("/employee/{employeeId}")
    public Employee getEmployeeById(@PathVariable int employeeId) throws Exception {
        if(employeeService.findById(employeeId) == null) throw new Exception("Employee id not found - " + employeeId);

        return employeeService.findById(employeeId);
    }

    @PostMapping("/employee")
    public Employee saveEmployee(@RequestBody Employee theEmployee){
        theEmployee.setId(0);
        employeeService.save(theEmployee);

        return theEmployee;
    }

    @DeleteMapping("/employee/{employeeId}")
    public String deleteCategory(@PathVariable int employeeId) throws Exception {
        if(employeeService.findById(employeeId) == null) throw new Exception("Employee id not found - " + employeeId);
        employeeService.deleteById(employeeId);

        return "Deleted category is - " + employeeId;
    }

    @GetMapping("/employees/{jobName}")
    public List<Employee> getEmployeeById(@PathVariable String jobName) throws Exception {
        if(employeeService.findByJob(jobName) == null) throw new Exception("Employee id not found - " + jobName);

        return employeeService.findByJob(jobName);
    }

}
