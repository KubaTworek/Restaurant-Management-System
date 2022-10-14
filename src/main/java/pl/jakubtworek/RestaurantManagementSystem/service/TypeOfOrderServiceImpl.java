package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

@Service
public class TypeOfOrderServiceImpl implements TypeOfOrderService{

    @Autowired
    private TypeOfOrderRepository typeOfOrderRepository;

    @Override
    public TypeOfOrder findByType(String typeName) {
        return typeOfOrderRepository.findByType(typeName);
    }
}
