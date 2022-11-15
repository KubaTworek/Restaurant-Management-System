package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import java.util.*;

@Component
@Scope("singleton")
public class OrdersQueue implements Subject {
    private final Queue<OrderDTO> orders = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    public OrdersQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(OrderDTO order){
        orders.add(order);
        notifyObservers();
    }

    public OrderDTO get(){
        return orders.poll();
    }

    public int size(){
        return orders.size();
    }

    @Override
    public void registerObserver(Observer o) {
        observerList.add(o);
    }

    @Override
    public void unregisterObserver(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observerList) {
            o.update();
        }
    }
}
