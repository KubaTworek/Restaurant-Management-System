package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

import java.util.*;

@Component
@Scope("singleton")
public class OrdersMadeOnsiteQueue implements Subject {
    private final Queue<OrderDTO> ordersMadeOnsite = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    public OrdersMadeOnsiteQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(OrderDTO order) {
        ordersMadeOnsite.add(order);
        notifyObservers();
    }

    public OrderDTO get() {
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
