package pl.jakubtworek.employee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pl.jakubtworek.common.SpringDomainEventPublisher;

@Configuration
class EmployeeConfiguration {
    @Bean
    @Scope("prototype")
    Employee employee(
            SpringDomainEventPublisher publisher,
            EmployeeRepository repository
    ) {
        Employee employee = new Employee();
        employee.setDependencies(publisher, repository);
        return employee;
    }

    @Bean
    EmployeeFacade employeeFacade(
            EmployeeQueryRepository employeeQueryRepository,
            Employee employee
    ) {
        return new EmployeeFacade(
                employeeQueryRepository,
                employee
        );
    }
}
