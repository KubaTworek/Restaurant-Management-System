package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

public interface Subject {
    void registerObserver(Observer o);

    void unregisterObserver(Observer o);

    void notifyObservers();
}
