package pl.jakubtworek.employee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.SpringDomainEventPublisher;

@Configuration
class EmployeeConfiguration {
    @Bean
    EmployeeFacade employeeFacade(
            EmployeeRepository employeeRepository,
            EmployeeQueryRepository employeeQueryRepository,
            SpringDomainEventPublisher publisher
    ) {
        return new EmployeeFacade(
                employeeRepository,
                employeeQueryRepository,
                publisher
        );
    }
}
