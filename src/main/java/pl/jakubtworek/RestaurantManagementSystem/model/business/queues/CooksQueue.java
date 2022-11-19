package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
@Scope("singleton")
public class CooksQueue implements Subject {
    private final Queue<EmployeeDTO> cooks = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    public CooksQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(EmployeeDTO cook){
        cooks.add(cook);
        notifyObservers();
    }

    public EmployeeDTO get(){
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
