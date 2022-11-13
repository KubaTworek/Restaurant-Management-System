package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = "/schema.sql")
public class TypeOfOrderServiceIT {
    @Autowired
    private TypeOfOrderService typeOfOrderService;
    @Autowired
    private TypeOfOrderRepository typeOfOrderRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOnSiteTypeOfOrder_whenPassOnSiteString(){
        // when
        Optional<TypeOfOrder> typeOfOrder = typeOfOrderService.findByType("On-site");

        // then
        assertNotNull(typeOfOrder.get());
        assertEquals(1, typeOfOrder.get().getId());
        assertEquals("On-site", typeOfOrder.get().getType());
        assertEquals(1, typeOfOrder.get().getOrders().size());
        assertEquals(1, typeOfOrder.get().getOrders().get(0).getId());
        assertEquals(12.99, typeOfOrder.get().getOrders().get(0).getPrice());
        assertEquals("2022-08-22", typeOfOrder.get().getOrders().get(0).getDate());
        assertEquals("12:00", typeOfOrder.get().getOrders().get(0).getHourOrder());
        assertEquals("12:15", typeOfOrder.get().getOrders().get(0).getHourAway());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnDeliveryTypeOfOrder_whenPassDeliveryString(){
        // when
        Optional<TypeOfOrder> typeOfOrder = typeOfOrderService.findByType("Delivery");

        // then
        assertNotNull(typeOfOrder.get());
        assertEquals(2, typeOfOrder.get().getId());
        assertEquals("Delivery", typeOfOrder.get().getType());
        assertEquals(1, typeOfOrder.get().getOrders().size());
        assertEquals(2, typeOfOrder.get().getOrders().get(0).getId());
        assertEquals(30.99, typeOfOrder.get().getOrders().get(0).getPrice());
        assertEquals("2022-08-22", typeOfOrder.get().getOrders().get(0).getDate());
        assertEquals("12:05", typeOfOrder.get().getOrders().get(0).getHourOrder());
        assertEquals("12:15", typeOfOrder.get().getOrders().get(0).getHourAway());
    }
}
