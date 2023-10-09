package pl.jakubtworek.menu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MenuConfiguration {
    @Bean
    MenuFacade menuFacade(
            MenuRepository menuRepository,
            MenuQueryRepository menuQueryRepository
    ) {
        return new MenuFacade(
                menuRepository,
                menuQueryRepository
        );
    }
}
