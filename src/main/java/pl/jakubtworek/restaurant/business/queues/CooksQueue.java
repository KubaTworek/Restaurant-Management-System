package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.employee.EmployeeDto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class CooksQueue implements Subject {
    private final Queue<EmployeeDto> cooks = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    CooksQueue() {
        this.observerList = new ArrayList<>();
    }

    void add(EmployeeDto cook) {
        cooks.add(cook);
        notifyObservers();
    }

    public EmployeeDto get() {
        return cooks.poll();
    }

    public int size() {
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
