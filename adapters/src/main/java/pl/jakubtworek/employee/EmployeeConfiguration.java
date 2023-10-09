package pl.jakubtworek.employee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.queue.EmployeeQueueFacade;

@Configuration
class EmployeeConfiguration {
    @Bean
    EmployeeFacade employeeFacade(
            EmployeeRepository employeeRepository,
            EmployeeQueryRepository employeeQueryRepository,
            EmployeeQueueFacade employeeQueueFacade
    ) {
        return new EmployeeFacade(
                employeeRepository,
                employeeQueryRepository,
                employeeQueueFacade
        );
    }
}
