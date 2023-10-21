package pl.jakubtworek.queue;

import pl.jakubtworek.employee.dto.SimpleEmployee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class DeliveryQueue implements Subject, EmployeeQueue {
    private final Queue<SimpleEmployee> deliveries = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    DeliveryQueue() {
        this.observerList = new ArrayList<>();
    }

    @Override
    public SimpleEmployee get() {
        return deliveries.poll();
    }

    @Override
    public int size() {
        return deliveries.size();
    }

    @Override
    public void add(SimpleEmployee delivery) {
        deliveries.add(delivery);
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
