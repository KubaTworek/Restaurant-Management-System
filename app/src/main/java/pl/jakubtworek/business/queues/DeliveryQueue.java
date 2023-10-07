package pl.jakubtworek.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class DeliveryQueue implements Subject {
    private final Queue<SimpleEmployee> deliveries = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    DeliveryQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(SimpleEmployee delivery) {
        deliveries.add(delivery);
        notifyObservers();
    }

    public SimpleEmployee get() {
        return deliveries.poll();
    }

    public int size() {
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
