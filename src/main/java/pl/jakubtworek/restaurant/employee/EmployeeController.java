package pl.jakubtworek.restaurant.employee;

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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
class EmployeeController {
    private final EmployeeService employeeService;

    EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    ResponseEntity<EmployeeDto> create(@RequestBody EmployeeRequest employeeRequest) {
        EmployeeDto result = employeeService.save(employeeRequest);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<EmployeeDto> delete(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    List<EmployeeDto> get() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/job")
    List<EmployeeDto> getByJob(@RequestParam String job) {
        return employeeService.findByJob(job);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}