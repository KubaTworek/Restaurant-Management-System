package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu findById(int theId) {
        Optional<Menu> result = menuRepository.findById(theId);

        Menu theMenu = null;

        if (result.isPresent()) {
            theMenu = result.get();
        }

        return theMenu;
    }

    @Override
    public void save(Menu theMenu) {
        menuRepository.save(theMenu);
    }

    @Override
    public void deleteById(int theId) {
        menuRepository.deleteById(theId);
    }

    @Override
    public Menu findByName(String menuName) {
        return menuRepository.findByName(menuName);
    }
}
