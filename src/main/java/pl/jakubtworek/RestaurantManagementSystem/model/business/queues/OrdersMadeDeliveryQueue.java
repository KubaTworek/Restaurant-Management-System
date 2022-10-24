package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
@Scope("singleton")
public class OrdersMadeDeliveryQueue implements Subject {
    private final Queue<Order> ordersMadeDelivery = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    public OrdersMadeDeliveryQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(Order order){
        ordersMadeDelivery.add(order);
        notifyObservers();
    }

    public Order get(){
        return ordersMadeDelivery.poll();
    }

    public int size(){
        return ordersMadeDelivery.size();
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
