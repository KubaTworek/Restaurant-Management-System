package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.employee.query.SimpleEmployeeQueryDto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class DeliveryQueue implements Subject {
    private final Queue<SimpleEmployeeQueryDto> deliveries = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    DeliveryQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(SimpleEmployeeQueryDto delivery) {
        deliveries.add(delivery);
        notifyObservers();
    }

    public SimpleEmployeeQueryDto get() {
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
