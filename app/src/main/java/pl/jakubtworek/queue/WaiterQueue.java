package pl.jakubtworek.queue;

import pl.jakubtworek.employee.dto.SimpleEmployee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class WaiterQueue implements Subject, EmployeeQueue {
    private final Queue<SimpleEmployee> waiters = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    WaiterQueue() {
        this.observerList = new ArrayList<>();
    }

    @Override
    public SimpleEmployee get() {
        return waiters.poll();
    }

    @Override
    public int size() {
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
        for (var o : observerList) {
            o.update();
        }
    }
}
