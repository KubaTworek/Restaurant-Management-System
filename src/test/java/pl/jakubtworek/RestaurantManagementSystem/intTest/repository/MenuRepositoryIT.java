package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.MenuAssertions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenu;

@SpringBootTest
@Transactional
class MenuRepositoryIT {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void shouldReturnCreatedMenu(){
        // given
        Menu menu = createMenu();

        // when
        Menu menuCreated = menuRepository.save(menu);

        // then
        MenuAssertions.checkAssertionsForMenu(menuCreated);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        menuRepository.deleteById(1L);
        List<Menu> menuList = menuRepository.findAll();

        // then
        assertEquals(1, menuList.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllMenu() {
        // when
        List<Menu> menuListReturned = menuRepository.findAll();

        // then
        MenuAssertions.checkAssertionsForMenus(menuListReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenu_whenPassId() {
        // when
        Menu menuReturned = menuRepository.findById(1L).orElse(null);

        // then
        MenuAssertions.checkAssertionsForMenu(menuReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenu_whenPassName() {
        // when
        Menu menuReturned = menuRepository.findByName("Drinks").orElse(null);

        // then
        MenuAssertions.checkAssertionsForMenu(menuReturned);
    }
}
