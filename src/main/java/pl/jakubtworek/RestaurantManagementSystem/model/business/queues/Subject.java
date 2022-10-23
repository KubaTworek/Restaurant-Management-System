package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.Observer;

public interface Subject {
    public void registerObserver(Observer o);
    public void unregisterObserver(Observer o);
    public void notifyObservers();
}
