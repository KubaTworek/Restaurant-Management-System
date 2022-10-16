package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
@Scope("singleton")
public class CooksQueue implements Subject{
    private final Queue<Employee> cooks = new LinkedList<>();
    private ArrayList<Observer> observerList;

    public CooksQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(Employee cook){
        cooks.add(cook);
        notifyObservers();
    }

    public Employee get(){
        return cooks.poll();
    }

    public int size(){
        return cooks.size();
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