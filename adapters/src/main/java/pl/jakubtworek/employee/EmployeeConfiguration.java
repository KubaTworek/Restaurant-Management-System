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
            WaiterDelivery waiterDelivery,
            CarDelivery carDelivery,
            SpringDomainEventPublisher publisher
    ) {
        return new EmployeeFacade(
                employeeRepository,
                employeeQueryRepository,
                waiterDelivery,
                carDelivery,
                publisher);
    }

    @Bean
    CarDelivery carDelivery(
            SpringDomainEventPublisher publisher
    ) {
        return new CarDelivery(
                publisher
        );
    }

    @Bean
    WaiterDelivery waiterDelivery(
            SpringDomainEventPublisher publisher
    ) {
        return new WaiterDelivery(
                publisher
        );
    }
}
