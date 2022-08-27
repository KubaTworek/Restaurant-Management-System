package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.TypeOfOrderDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

import java.util.List;

@Service
public class TypeOfOrderServiceImpl implements TypeOfOrderService{

    @Autowired
    private TypeOfOrderDAO typeOfOrderDAO;

    @Override
    public TypeOfOrder findByType(String typeName) {
        return typeOfOrderDAO.findByType(typeName);
    }
}
