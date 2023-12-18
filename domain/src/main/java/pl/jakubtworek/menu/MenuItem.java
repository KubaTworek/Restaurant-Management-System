package pl.jakubtworek.menu;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.common.vo.Status;

import java.math.BigDecimal;
import java.util.Objects;

class MenuItem {
    private static final String MENU_ITEM_NOT_FOUND_ERROR = "Menu item with that id doesn't exist";
    private static final String MENU_NOT_FOUND_ERROR = "Menu with that id doesn't exist";
    private static final String MENU_ITEM_WRONG_STATUS = "Menu item is in wrong status!";

    private Long id;
    private String name;
    private Money price;
    private Menu menu;
    private Status status;
    private DomainEventPublisher publisher;
    private MenuItemRepository repository;

    MenuItem() {
    }

    private MenuItem(final Long id,
                     final String name,
                     final Money price,
                     final Menu menu,
                     final Status status
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.status = status;
    }

    static MenuItem restore(MenuItemSnapshot snapshot, int depth) {
        final var restoredMenu = (depth > 0 && snapshot.getMenu() != null)
                ? Menu.restore(snapshot.getMenu(), depth - 1)
                : null;

        return new MenuItem(
                snapshot.getId(),
                snapshot.getName(),
                new Money(snapshot.getPrice()),
                restoredMenu,
                snapshot.getStatus()
        );
    }

    MenuItemSnapshot getSnapshot(int depth) {
        final var menuSnapshot = (depth > 0 && menu != null)
                ? menu.getSnapshot(depth - 1)
                : null;

        return new MenuItemSnapshot(
                id,
                name,
                price.value(),
                menuSnapshot,
                status
        );
    }

    void setDependencies(DomainEventPublisher publisher, MenuItemRepository repository) {
        this.publisher = publisher;
        this.repository = repository;
    }

    MenuItem update(Long id, String name, BigDecimal price, Long menuId) {
        final var menuItem = this.getById(id);
        validateStatus(menuItem.status, Status.INACTIVE);

        menuItem.menu = this.getMenuById(menuId);
        menuItem.setInfo(name, price);

        return this.repository.save(menuItem);
    }

    MenuItem updateAndCreateMenu(Long menuItemId, String name, BigDecimal price, String menuName) {
        final var menuItem = this.getById(menuItemId);
        validateStatus(menuItem.status, Status.INACTIVE);

        menuItem.menu = this.createMenu(menuName);
        menuItem.setInfo(name, price);

        return this.repository.save(menuItem);
    }

    MenuItem create(String name, BigDecimal price, Long menuId) {
        this.menu = this.getMenuById(menuId);
        this.setInfo(name, price);

        return this.repository.save(this);
    }

    MenuItem createWithMenu(String name, BigDecimal price, String menuName) {
        this.menu = this.createMenu(menuName);
        this.setInfo(name, price);

        return this.repository.save(this);
    }

    void delete(Long id) {
        this.repository.deleteById(id);
    }

    void deactivate(Long menuItemId) {
        final var menuItem = this.getById(menuItemId);
        validateStatus(menuItem.status, Status.ACTIVE);
        menuItem.status = Status.INACTIVE;
        this.repository.save(menuItem);
    }

    private MenuItem getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalStateException(MENU_ITEM_NOT_FOUND_ERROR));
    }

    private Menu getMenuById(Long id) {
        return repository.findMenuById(id)
                .orElseThrow(() -> new IllegalStateException(MENU_NOT_FOUND_ERROR));
    }

    private void validateStatus(Status status, Status expectedStatus) {
        if (status != expectedStatus) {
            throw new IllegalStateException(MENU_ITEM_WRONG_STATUS);
        }
    }

    private Menu createMenu(String menuName) {
        final var menu = new Menu();
        menu.updateName(menuName);
        return this.repository.save(menu);
    }

    private void setInfo(String name, BigDecimal price) {
        this.name = name;
        this.price = new Money(price);
        this.status = Status.ACTIVE;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuItem menuItem = (MenuItem) o;
        return Objects.equals(id, menuItem.id) && Objects.equals(name, menuItem.name) && Objects.equals(price, menuItem.price) && Objects.equals(menu, menuItem.menu) && status == menuItem.status;
    }
}
