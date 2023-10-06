package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.employee.EmployeeDto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class WaiterQueue implements Subject {
    private final Queue<EmployeeDto> waiters = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    WaiterQueue() {
        this.observerList = new ArrayList<>();
    }

    void add(EmployeeDto waiter) {
        waiters.add(waiter);
        notifyObservers();
    }

    public EmployeeDto get() {
        return waiters.poll();
    }

    public int size() {
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
