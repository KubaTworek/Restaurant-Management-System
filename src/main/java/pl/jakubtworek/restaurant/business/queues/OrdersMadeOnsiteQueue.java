package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.order.OrderDto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class OrdersMadeOnsiteQueue implements Subject {
    private final Queue<OrderDto> ordersMadeOnsite = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    OrdersMadeOnsiteQueue() {
        this.observerList = new ArrayList<>();
    }

    void add(OrderDto order) {
        ordersMadeOnsite.add(order);
        notifyObservers();
    }

    public OrderDto get() {
        return ordersMadeOnsite.poll();
    }

    public int size() {
        return ordersMadeOnsite.size();
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