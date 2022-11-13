package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuFactory menuFactory;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository, MenuFactory menuFactory) {
        this.menuRepository = menuRepository;
        this.menuFactory = menuFactory;
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> findById(Long theId) {
        return menuRepository.findById(theId);
    }

    @Override
    public Menu save(MenuRequest menuDTO) {
        Menu menu = menuFactory.createMenu(menuDTO);
        menuRepository.save(menu);
        return menu;
    }

    @Override
    public void deleteById(Long theId) {
        menuRepository.deleteById(theId);
    }

    @Override
    public Optional<Menu> findByName(String menuName) {
        return menuRepository.findByName(menuName);
    }

    @Override
    public boolean checkIsMenuIsNull(Long id) {
        return findById(id).isEmpty();
    }
    @Override
    public boolean checkIsMenuIsNull(String name) {
        return findByName(name).isEmpty();
    }
}