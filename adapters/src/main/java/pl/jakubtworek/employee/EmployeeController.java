package pl.jakubtworek.employee;

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
    private final EmployeeFacade employeeFacade;

    EmployeeController(final EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    @PostMapping
    ResponseEntity<EmployeeDto> create(@RequestBody EmployeeRequest employeeRequest) {
        EmployeeDto result = employeeFacade.save(employeeRequest);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<EmployeeDto> delete(@PathVariable Long id) {
        employeeFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    List<EmployeeDto> get() {
        return employeeFacade.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
        return employeeFacade.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/job")
    List<EmployeeDto> getByJob(@RequestParam String job) {
        return employeeFacade.findByJob(job);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}