package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TypeOfOrderServiceIT {
    @Autowired
    private TypeOfOrderService typeOfOrderService;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOnSiteTypeOfOrder_whenPassOnSiteString(){
        // when
        Optional<TypeOfOrderDTO> typeOfOrder = typeOfOrderService.findByType("On-site");

        // then
        assertEquals("On-site", typeOfOrder.get().getType());
        assertEquals(1, typeOfOrder.get().getOrders().size());
        assertEquals(12.99, typeOfOrder.get().getOrders().get(0).getPrice());
        assertEquals("2022-08-22", typeOfOrder.get().getOrders().get(0).getDate());
        assertEquals("12:00", typeOfOrder.get().getOrders().get(0).getHourOrder());
        assertEquals("12:15", typeOfOrder.get().getOrders().get(0).getHourAway());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnDeliveryTypeOfOrder_whenPassDeliveryString(){
        // when
        Optional<TypeOfOrderDTO> typeOfOrder = typeOfOrderService.findByType("Delivery");

        // then
        assertEquals("Delivery", typeOfOrder.get().getType());
        assertEquals(1, typeOfOrder.get().getOrders().size());
        assertEquals(30.99, typeOfOrder.get().getOrders().get(0).getPrice());
        assertEquals("2022-08-22", typeOfOrder.get().getOrders().get(0).getDate());
        assertEquals("12:05", typeOfOrder.get().getOrders().get(0).getHourOrder());
        assertNull(typeOfOrder.get().getOrders().get(0).getHourAway());
    }
}
