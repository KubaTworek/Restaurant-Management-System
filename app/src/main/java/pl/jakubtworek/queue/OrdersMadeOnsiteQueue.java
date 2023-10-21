package pl.jakubtworek.queue;

import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class OrdersMadeOnsiteQueue implements Subject {
    private final Queue<SimpleOrder> ordersMadeOnsite = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    OrdersMadeOnsiteQueue() {
        this.observerList = new ArrayList<>();
    }

    void add(SimpleOrder order) {
        ordersMadeOnsite.add(order);
        notifyObservers();
    }

    public SimpleOrder get() {
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
    public void notifyObservers() {
        for (var o : observerList) {
            o.update();
        }
    }
}
