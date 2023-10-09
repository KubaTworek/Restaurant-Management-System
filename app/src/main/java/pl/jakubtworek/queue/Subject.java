package pl.jakubtworek.queue;

interface Subject {
    void registerObserver(Observer o);

    void notifyObservers();
}
