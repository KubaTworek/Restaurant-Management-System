package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.order.query.SimpleOrderQueryDto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class OrdersMadeDeliveryQueue implements Subject {
    private final Queue<SimpleOrderQueryDto> ordersMadeDelivery = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    OrdersMadeDeliveryQueue() {
        this.observerList = new ArrayList<>();
    }

    void add(SimpleOrderQueryDto order) {
        ordersMadeDelivery.add(order);
        notifyObservers();
    }

    public SimpleOrderQueryDto get() {
        return ordersMadeDelivery.poll();
    }

    public int size() {
        return ordersMadeDelivery.size();
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
