package pl.jakubtworek.queue;

import org.springframework.stereotype.Component;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Component
class CooksQueue implements Subject, EmployeeQueue {
    private final Queue<SimpleEmployee> cooks = new LinkedList<>();
    private final ArrayList<Observer> observerList;

    CooksQueue() {
        this.observerList = new ArrayList<>();
    }

    SimpleEmployee get() {
        return cooks.poll();
    }

    int size() {
        return cooks.size();
    }

    @Override
    public void add(SimpleEmployee cook) {
        cooks.add(cook);
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer o) {
        observerList.add(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observerList) {
            o.update();
        }
    }
}
