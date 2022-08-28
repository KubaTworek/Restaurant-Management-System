package pl.jakubtworek.RestaurantManagementSystem.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
@Scope("singleton")
public class DeliveryQueue implements Subject{
    private final Queue<Employee> deliveries = new LinkedList<>();
    private ArrayList<Observer> observerList;

    public DeliveryQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(Employee delivery){
        deliveries.add(delivery);
        notifyObservers();
    }

    public Employee get(){
        return deliveries.poll();
    }

    public int size(){
        return deliveries.size();
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
