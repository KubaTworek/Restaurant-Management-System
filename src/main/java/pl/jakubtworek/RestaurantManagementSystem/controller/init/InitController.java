package pl.jakubtworek.RestaurantManagementSystem.controller.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

@RestController
@RequestMapping("/")
public class InitController {

    private final EmployeeService employeeService;

    @Autowired
    public InitController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public void init() {
        employeeService.addCooksToKitchen();
        employeeService.addWaitersToKitchen();
        employeeService.addDeliveriesToKitchen();
    }
}
