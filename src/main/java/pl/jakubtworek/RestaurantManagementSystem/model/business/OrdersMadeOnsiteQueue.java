package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
@Scope("singleton")
public class OrdersMadeOnsiteQueue implements Subject{
    private final Queue<Order> ordersMadeOnsite = new LinkedList<>();
    private ArrayList<Observer> observerList;

    public OrdersMadeOnsiteQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(Order order){
        ordersMadeOnsite.add(order);
        notifyObservers();
    }

    public Order get(){
        return ordersMadeOnsite.poll();
    }

    public int size(){
        return ordersMadeOnsite.size();
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
