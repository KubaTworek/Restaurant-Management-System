package pl.jakubtworek.queue;

import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

class OrdersQueue implements Subject {
    private final Queue<SimpleOrder> orders = new PriorityQueue<>(new OrderComparator());
    private final ArrayList<Observer> observerList;

    OrdersQueue() {
        this.observerList = new ArrayList<>();
    }

    void add(SimpleOrder order) {
        orders.add(order);
        notifyObservers();
    }

    SimpleOrder get() {
        return orders.poll();
    }

    int size() {
        return orders.size();
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

    private static class OrderComparator implements Comparator<SimpleOrder> {
        @Override
        public int compare(SimpleOrder o1, SimpleOrder o2) {
            return Integer.compare(isOrderOnsite(o1), isOrderOnsite(o2));
        }

        private int isOrderOnsite(SimpleOrder o1) {
            if (Objects.equals(o1.getTypeOfOrder(), TypeOfOrder.ON_SITE) || Objects.equals(o1.getTypeOfOrder(), TypeOfOrder.TAKE_AWAY))
                return -1;
            if (Objects.equals(o1.getTypeOfOrder(), TypeOfOrder.DELIVERY))
                return 1;
            return 0;
        }
    }
}
