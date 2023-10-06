package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.order.query.SimpleOrderQueryDto;
import pl.jakubtworek.restaurant.order.query.TypeOfOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

@Component
public class OrdersQueue implements Subject {
    private final Queue<SimpleOrderQueryDto> orders = new PriorityQueue<>(new OrderComparator());
    private final ArrayList<Observer> observerList;

    OrdersQueue() {
        this.observerList = new ArrayList<>();
    }

    void add(SimpleOrderQueryDto order) {
        orders.add(order);
        notifyObservers();
    }

    public SimpleOrderQueryDto get() {
        return orders.poll();
    }

    public int size() {
        return orders.size();
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

    static class OrderComparator implements Comparator<SimpleOrderQueryDto> {
        @Override
        public int compare(SimpleOrderQueryDto o1, SimpleOrderQueryDto o2) {
            return Integer.compare(isOrderOnsite(o2), isOrderOnsite(o1));
        }

        private int isOrderOnsite(SimpleOrderQueryDto o1) {
            if (Objects.equals(o1.getTypeOfOrder(), TypeOfOrder.ON_SITE) || Objects.equals(o1.getTypeOfOrder(), TypeOfOrder.TAKE_AWAY))
                return 1;
            if (Objects.equals(o1.getTypeOfOrder(), TypeOfOrder.DELIVERY))
                return -1;
            return 0;
        }
    }
}
