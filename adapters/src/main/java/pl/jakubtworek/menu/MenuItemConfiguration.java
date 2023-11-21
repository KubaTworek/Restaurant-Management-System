package pl.jakubtworek.menu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pl.jakubtworek.common.SpringDomainEventPublisher;

@Configuration
class MenuItemConfiguration {

    @Bean
    @Scope("prototype")
    MenuItem menuItem(
            SpringDomainEventPublisher publisher,
            MenuItemRepository repository
    ) {
        MenuItem menuItem = new MenuItem();
        menuItem.setDependencies(publisher, repository);
        return menuItem;
    }

    @Bean
    MenuItemFacade menuItemFacade(
            MenuItemQueryRepository menuItemQueryRepository,
            MenuQueryRepository menuQueryRepository,
            MenuItem menuItem
    ) {
        return new MenuItemFacade(
                menuItemQueryRepository,
                menuQueryRepository,
                menuItem
        );
    }
}
