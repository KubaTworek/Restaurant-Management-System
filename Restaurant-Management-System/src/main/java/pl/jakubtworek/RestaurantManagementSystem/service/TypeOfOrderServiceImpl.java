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
    public List<TypeOfOrder> findAll() {
        return null;
    }

    @Override
    public TypeOfOrder findById(int theId) {
        return null;
    }

    @Override
    public void save(TypeOfOrder theTypeOfOrder) {

    }

    @Override
    public void deleteById(int theId) {

    }

    @Override
    public TypeOfOrder findByType(String typeName) {
        return null;
    }
}
