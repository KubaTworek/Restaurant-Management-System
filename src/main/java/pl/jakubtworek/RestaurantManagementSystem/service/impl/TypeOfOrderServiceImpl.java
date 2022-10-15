package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

import java.util.Optional;

@Service
public class TypeOfOrderServiceImpl implements TypeOfOrderService {

    private final TypeOfOrderRepository typeOfOrderRepository;

    @Autowired
    public TypeOfOrderServiceImpl(TypeOfOrderRepository typeOfOrderRepository) {
        this.typeOfOrderRepository = typeOfOrderRepository;
    }

    @Override
    public Optional<TypeOfOrder> findByType(String typeName) {
        return typeOfOrderRepository.findByType(typeName);
    }
}
