package pl.jakubtworek.menu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MenuItemConfiguration {
    @Bean
    MenuItemFacade menuItemFacade(
            MenuItemRepository menuItemRepository,
            MenuItemQueryRepository menuItemQueryRepository,
            MenuQueryRepository menuQueryRepository
    ) {
        return new MenuItemFacade(
                menuItemRepository,
                menuItemQueryRepository,
                menuQueryRepository
        );
    }
}
