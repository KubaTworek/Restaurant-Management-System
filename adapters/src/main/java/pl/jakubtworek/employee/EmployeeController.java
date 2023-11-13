package pl.jakubtworek.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeFacade employeeFacade;

    EmployeeController(final EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    @PostMapping
    ResponseEntity<EmployeeDto> create(@RequestBody EmployeeRequest employeeRequest) {
        logger.info("Received a request to create a new employee with job: {}", employeeRequest.getJob());
        EmployeeDto result = employeeFacade.save(employeeRequest);
        logger.info("Employee {} created successfully.", result.getFirstName() + result.getLastName());
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<EmployeeDto> delete(@PathVariable Long id) {
        logger.info("Received a request to delete an employee with ID: {}", id);
        employeeFacade.deleteById(id);
        logger.info("Employee with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    List<EmployeeDto> get() {
        logger.info("Received a request to get the list of all employees.");
        return employeeFacade.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
        logger.info("Received a request to get employee details for ID: {}", id);
        return employeeFacade.findById(id)
                .map(employee -> {
                    logger.info("Found employee with ID {}: {}", id, employee.getFirstName() + employee.getLastName());
                    return ResponseEntity.ok(employee);
                })
                .orElseGet(() -> {
                    logger.warn("Employee with ID {} not found.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/job")
    List<EmployeeDto> getByJob(@RequestParam String job) {
        logger.info("Received a request to get employees by job: {}", job);
        return employeeFacade.findByJob(job);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        logger.error("An error occurred: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
