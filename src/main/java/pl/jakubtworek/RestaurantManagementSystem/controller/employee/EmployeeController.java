package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.EmployeeNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;

import pl.jakubtworek.RestaurantManagementSystem.model.response.Response;
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

    @GetMapping("/")
    public ResponseEntity<Response<List<EmployeeDTO>>> getEmployees() throws EmployeeNotFoundException {
        Response<List<EmployeeDTO>> response = new Response<>();
        List<Employee> employees = employeeService.findAll();

        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("There are no employees in restaurant");
        }

        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employees.forEach(e -> employeeDTOS.add(e.convertEntityToDTO()));

        employeeDTOS.forEach(dto -> dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash("employee").slash(dto.getId()).withSelfRel()));

        response.setData(employeeDTOS);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Response<EmployeeDTO>> getEmployeeById(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        Response<EmployeeDTO> response = new Response<>();
        Optional<Employee> employee = employeeService.findById(employeeId);

        if(employee.isPresent()) {
            Employee employeeFound = employee.get();
            EmployeeDTO dto = employeeFound.convertEntityToDTO();

            dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash("employee").slash(dto.getId()).withSelfRel());
            response.setData(dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new EmployeeNotFoundException("There are employees in restaurant with that id: " + employeeId);
    }

    @PostMapping("/{jobName}")
    public ResponseEntity<Response<EmployeeDTO>> saveEmployee(@PathVariable String jobName,@RequestBody EmployeeDTO dto, BindingResult result) throws JobNotFoundException {

        Response<EmployeeDTO> response = new Response<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        if(employeeService.findJobByName(jobName).isPresent()){
            dto.setId(Long.parseLong("0"));
            dto.setJob(employeeService.findJobByName(jobName).get().convertEntityToDTO());
            Employee employee = employeeService.save(dto.convertDTOToEntity());
            EmployeeDTO employeeDTO = employee.convertEntityToDTO();
            dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash("employee").slash(dto.getId()).withSelfRel());
            response.setData(employeeDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        throw new JobNotFoundException("There are no job like that in restaurant with that name: " + jobName);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Response<String>> deleteEmployee(@PathVariable Long employeeId) throws Exception {
        if(employeeService.findById(employeeId).isPresent()){
            employeeService.deleteById(employeeId);

            Response<String> response = new Response<>();

            response.setData("Deleted category is - " + employeeId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new EmployeeNotFoundException("Employee id not found - " + employeeId);
    }

    @GetMapping("/{jobName}")
    public ResponseEntity<Response<List<EmployeeDTO>>> getEmployeeByJobName(@PathVariable String jobName) throws JobNotFoundException {

        if (employeeService.findJobByName(jobName).isPresent()) {
            Response<List<EmployeeDTO>> response = new Response<>();
            List<Employee> employees = employeeService.findByJob(jobName);

            List<EmployeeDTO> employeeDTOS = new ArrayList<>();
            employees.forEach(e -> employeeDTOS.add(e.convertEntityToDTO()));

            employeeDTOS.forEach(dto -> dto.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash("employee").slash(dto.getId()).withSelfRel()));

            response.setData(employeeDTOS);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new JobNotFoundException("There are no job like that in restaurant with that name: " + jobName);
    }

    @GetMapping("/startRestaurant")
    public void addCooksToKitchen(){
        employeeService.addCooksToKitchen();
        employeeService.addWaitersToKitchen();
        employeeService.addDeliveriesToKitchen();
    }
}