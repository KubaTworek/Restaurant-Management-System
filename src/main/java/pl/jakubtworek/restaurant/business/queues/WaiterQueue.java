package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.employee.query.SimpleEmployeeQueryDto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class WaiterQueue implements Subject {
    private final Queue<SimpleEmployeeQueryDto> waiters = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    WaiterQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(SimpleEmployeeQueryDto waiter) {
        waiters.add(waiter);
        notifyObservers();
    }

    public SimpleEmployeeQueryDto get() {
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
