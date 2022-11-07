package pl.jakubtworek.RestaurantManagementSystem;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("")
@EnableTransactionManagement
public class H2JpaConfig {
}
