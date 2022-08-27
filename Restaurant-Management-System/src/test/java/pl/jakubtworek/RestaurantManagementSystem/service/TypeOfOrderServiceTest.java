package pl.jakubtworek.RestaurantManagementSystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class TypeOfOrderServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private TypeOfOrderService typeOfOrderService;

    @BeforeEach
    public void setupDatabase() {
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("INSERT INTO Type_Of_Order(id, type)" + "VALUES (1,'On-site')");
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    public void findByNameMenuTest(){
        TypeOfOrder typeOfOrder = typeOfOrderService.findByType("On-site");

        assertEquals("On-site",typeOfOrder.getType());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Type_Of_Order");
    }
}
