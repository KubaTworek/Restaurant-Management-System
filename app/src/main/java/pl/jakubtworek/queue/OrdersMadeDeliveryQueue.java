package pl.jakubtworek.queue;

import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class OrdersMadeDeliveryQueue implements Subject {
    private final Queue<SimpleOrder> ordersMadeDelivery = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    OrdersMadeDeliveryQueue() {
        this.observerList = new ArrayList<>();
    }

    void add(SimpleOrder order) {
        ordersMadeDelivery.add(order);
        notifyObservers();
    }

    public SimpleOrder get() {
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
    public void notifyObservers() {
        for (var o : observerList) {
            o.update();
        }
    }
}
