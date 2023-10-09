package pl.jakubtworek.queue;

import org.springframework.stereotype.Component;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
class WaiterQueue implements Subject, EmployeeQueue {
    private final Queue<SimpleEmployee> waiters = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    WaiterQueue() {
        this.observerList = new ArrayList<>();
    }

    SimpleEmployee get() {
        return waiters.poll();
    }

    int size() {
        return waiters.size();
    }

    @Override
    public void add(SimpleEmployee waiter) {
        waiters.add(waiter);
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer o) {
        observerList.add(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observerList) {
            o.update();
        }
    }
}
