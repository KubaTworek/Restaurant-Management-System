package pl.jakubtworek.RestaurantManagementSystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

@RestController
@RequestMapping("/")
public class TypeOfOrderController {

    private final TypeOfOrderService typeOfOrderService;

    public TypeOfOrderController(TypeOfOrderService typeOfOrderService) {
        this.typeOfOrderService = typeOfOrderService;
    }

    @GetMapping("/typeOfOrder/type/{type}")
    public TypeOfOrder getTypeOfOrderByName(@PathVariable String type) throws Exception {
        if(typeOfOrderService.findByType(type) == null) throw new Exception("TypeOfOrder id not found - " + type);

        return typeOfOrderService.findByType(type);
    }
}
