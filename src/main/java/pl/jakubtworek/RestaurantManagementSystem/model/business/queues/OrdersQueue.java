package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

import java.util.*;

@Component
@Scope("singleton")
public class OrdersQueue implements Subject {
    private final Queue<OrderDTO> orders = new PriorityQueue<>(new OrderComparator());
    private final ArrayList<Observer> observerList;

    public OrdersQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(OrderDTO order){
        orders.add(order);
        notifyObservers();
    }

    public OrderDTO get(){
        return orders.poll();
    }

    public int size(){
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

    static class OrderComparator implements Comparator<OrderDTO>{
        @Override
        public int compare(OrderDTO o1, OrderDTO o2) {
            return Integer.compare(isOrderOnsite(o2), isOrderOnsite(o1));
        }

        private int isOrderOnsite(OrderDTO o1){
            if (Objects.equals(o1.getTypeOfOrder().getType(), "On-site")) return 1;
            if (Objects.equals(o1.getTypeOfOrder().getType(), "Delivery")) return -1;
            return 0;
        }
    }
}
