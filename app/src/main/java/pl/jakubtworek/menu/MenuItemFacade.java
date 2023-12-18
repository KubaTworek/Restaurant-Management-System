package pl.jakubtworek.menu;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.common.vo.Role;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuItemFacade {
    private final UserFacade userFacade;
    private final MenuItemQueryRepository menuItemQueryRepository;
    private final MenuQueryRepository menuQueryRepository;
    private final MenuItem menuItem;

    MenuItemFacade(final UserFacade userFacade,
                   final MenuItemQueryRepository menuItemQueryRepository,
                   final MenuQueryRepository menuQueryRepository,
                   final MenuItem menuItem
    ) {
        this.userFacade = userFacade;
        this.menuItemQueryRepository = menuItemQueryRepository;
        this.menuQueryRepository = menuQueryRepository;
        this.menuItem = menuItem;
    }

    public List<MenuItemDto> getByNames(List<String> names) {
        return menuItemQueryRepository.findAllDtoByNames(new HashSet<>(names)).stream()
                .flatMap(menuItem -> {
                    long count = names.stream().filter(name -> menuItem.getName().equals(name)).count();
                    return count > 1 ? Collections.nCopies((int) count, menuItem).stream() : Stream.of(menuItem);
                })
                .collect(Collectors.toList());
    }

    MenuItemDto save(MenuItemRequest toSave) {
        return toDto(menuItemQueryRepository.findDtoByName(toSave.name())
                .map(menuItemDto -> getUpdatedMenuItem(toSave, menuItemDto))
                .orElseGet(() -> createMenuItem(toSave)));
    }

    void deleteById(Long id, String jwt) {
        userFacade.verifyRole(jwt, Role.ADMIN);

        menuItem.delete(id);
    }

    void deactivateById(Long id, String jwt) {
        userFacade.verifyRole(jwt, Role.ADMIN);

        menuItem.deactivate(id);
    }

    List<MenuDto> findAll() {
        return new ArrayList<>(menuQueryRepository.findDtoByMenuItems_Status(Status.ACTIVE));
    }

    Optional<MenuItemDto> findById(Long theId) {
        return menuItemQueryRepository.findDtoById(theId);
    }

    private MenuItem getUpdatedMenuItem(MenuItemRequest toSave, MenuItemDto menuItemDto) {
        return menuQueryRepository.findDtoByName(toSave.menu())
                .map(menu -> menuItem.update(
                        menuItemDto.getId(),
                        toSave.name(),
                        toSave.price(),
                        menu.getId()
                ))
                .orElseGet(() -> menuItem.updateAndCreateMenu(
                        menuItemDto.getId(),
                        toSave.name(),
                        toSave.price(),
                        toSave.menu()
                ));
    }

    private MenuItem createMenuItem(MenuItemRequest toSave) {
        return menuQueryRepository.findDtoByName(toSave.menu())
                .map(menu -> menuItem.create(
                        toSave.name(),
                        toSave.price(),
                        menu.getId()
                ))
                .orElseGet(() -> menuItem.createWithMenu(
                        toSave.name(),
                        toSave.price(),
                        toSave.menu()
                ));
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        final var snap = menuItem.getSnapshot(1);
        return MenuItemDto.create(snap.getId(), snap.getName(), snap.getPrice(), snap.getStatus());
    }
}
