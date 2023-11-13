package pl.jakubtworek.menu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MenuItemConfiguration {

    @Bean
    MenuItemFacade menuItemFacade(
            MenuItemFactory menuItemFactory,
            MenuItemRepository menuItemRepository,
            MenuItemQueryRepository menuItemQueryRepository,
            MenuQueryRepository menuQueryRepository
    ) {
        return new MenuItemFacade(
                menuItemFactory,
                menuItemRepository,
                menuItemQueryRepository,
                menuQueryRepository
        );
    }

    @Bean
    MenuItemFactory menuItemFactory(
            MenuItemRepository menuItemRepository
    ) {
        return new MenuItemFactory(
                menuItemRepository
        );
    }
}
