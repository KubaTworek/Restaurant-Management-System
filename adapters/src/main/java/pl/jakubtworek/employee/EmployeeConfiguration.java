package pl.jakubtworek.employee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.business.queues.EmployeeQueueFacade;
import pl.jakubtworek.order.OrderFacade;

@Configuration
class EmployeeConfiguration {
    @Bean
    EmployeeFacade employeeFacade(
            OrderFacade orderFacade,
            EmployeeRepository employeeRepository,
            EmployeeQueryRepository employeeQueryRepository,
            EmployeeQueueFacade employeeQueueFacade
    ) {
        return new EmployeeFacade(
                orderFacade,
                employeeRepository,
                employeeQueryRepository,
                employeeQueueFacade
        );
    }
}
