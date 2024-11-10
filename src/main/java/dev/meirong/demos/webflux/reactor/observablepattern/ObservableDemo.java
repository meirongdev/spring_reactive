package dev.meirong.demos.webflux.reactor.observablepattern;

import java.util.ArrayList;
import java.util.List;

interface Observable<T> {
    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void notifyObservers(T value);
}

interface Observer<T> {
    void update(T value);
}

interface Subscriber<T> {
    void update(T value);
}

class DataStream implements Observable<Integer> {
    private List<Observer<Integer>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<Integer> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Integer> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Integer value) {
        observers.forEach(observer -> observer.update(value));
    }

    // This method is used to simulate the data emitting process
    public void emitData() {
        for (int i = 0; i < 10; i++) {
            // 检测中断状态，如果被中断则停止数据发射 
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Thread was interrupted, stopping data emission.");
                break;
            }

            String signal = "Red Signal";
            if (i % 2 == 0) {
                signal = "Green Signal";
            }
            System.out.println("Emitting data: " + signal);
            notifyObservers(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Restore the interrupted status, then we can handle it later
                Thread.currentThread().interrupt();
            }
        }
    }
}

class PrintObserver implements Observer<Integer> {
    @Override
    public void update(Integer value) {
        System.out.println("Received value: " + value);
    }
}

public class ObservableDemo {
    public static void main(String[] args) {
        DataStream dataStream = new DataStream();
        PrintObserver observer = new PrintObserver();
        dataStream.addObserver(observer);

        Thread dataEmittingThread = new Thread(dataStream::emitData);
        dataEmittingThread.start();

        try {
            Thread.sleep(3000);
            dataEmittingThread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}