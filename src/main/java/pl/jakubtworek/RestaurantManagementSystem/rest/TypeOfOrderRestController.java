package pl.jakubtworek.RestaurantManagementSystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

import java.util.List;

@RestController
@RequestMapping("/")
public class TypeOfOrderRestController {

    private final TypeOfOrderService typeOfOrderService;

    public TypeOfOrderRestController(TypeOfOrderService typeOfOrderService) {
        this.typeOfOrderService = typeOfOrderService;
    }

    @GetMapping("/typeOfOrder/type/{type}")
    public TypeOfOrder getTypeOfOrderByName(@PathVariable String type) throws Exception {
        if(typeOfOrderService.findByType(type) == null) throw new Exception("TypeOfOrder id not found - " + type);

        return typeOfOrderService.findByType(type);
    }
}
