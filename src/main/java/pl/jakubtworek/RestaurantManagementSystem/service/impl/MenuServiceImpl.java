package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
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
    public Menu save(MenuDTO menuDTO) {
        menuDTO.setId(0L);
        Menu menu = menuDTO.convertDTOToEntity();
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
}