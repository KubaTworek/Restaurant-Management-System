package pl.jakubtworek.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class CooksQueue implements Subject {
    private final Queue<SimpleEmployee> cooks = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    CooksQueue() {
        this.observerList = new ArrayList<>();
    }

    public void add(SimpleEmployee cook) {
        cooks.add(cook);
        notifyObservers();
    }

    public SimpleEmployee get() {
        return cooks.poll();
    }

    public int size() {
        return cooks.size();
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
