package pl.jakubtworek.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    ResponseEntity<EmployeeDto> create(@RequestHeader("Authorization") String jwt, @RequestBody EmployeeRequest employeeRequest) {
        logger.info("Received a request to create a new employee with job: {}", employeeRequest.job());
        final var result = employeeFacade.save(employeeRequest, jwt);
        logger.info("Employee {} created successfully.", result.getFirstName() + " " + result.getLastName());
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> deactivate(@RequestHeader("Authorization") String jwt, @PathVariable Long id) {
        logger.info("Received a request to deactivate an employee with ID: {}", id);
        employeeFacade.deactivateById(id, jwt);
        logger.info("Employee with ID {} deactivate successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    List<EmployeeDto> get(@RequestHeader("Authorization") String jwt,
                          @RequestParam(required = false) String job,
                          @RequestParam(required = false) String status
    ) {
        logger.info("Received a request to get the list of all employees.");
        return employeeFacade.findByParams(job, status, jwt);
    }

    @GetMapping("/{id}")
    ResponseEntity<EmployeeDto> getById(@RequestHeader("Authorization") String jwt,
                                        @PathVariable Long id) {
        logger.info("Received a request to get employee details for ID: {}", id);
        return employeeFacade.findById(id, jwt)
                .map(employee -> {
                    logger.info("Found employee with ID {}: {}", id, employee.getFirstName() + " " + employee.getLastName());
                    return ResponseEntity.ok(employee);
                })
                .orElseGet(() -> {
                    logger.warn("Employee with ID {} not found.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        logger.error("An error occurred: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
