package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
@Scope("singleton")
public class WaiterQueue implements Subject {
    private final Queue<EmployeeDTO> waiters = new LinkedList<>();
    private ArrayList<Observer> observerList;

    public WaiterQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(EmployeeDTO waiter){
        waiters.add(waiter);
        notifyObservers();
    }

    public EmployeeDTO get(){
        return waiters.poll();
    }

    public int size(){
        return waiters.size();
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
