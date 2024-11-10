package dev.meirong.demos.webflux.reactor.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

class DataStream {
    // This method is used to simulate the data emitting process
    public Observable<Integer> emitData() {
        return Observable.create(emitter -> {
            for (int i = 0; i < 10; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Thread was interrupted, stopping data emission.");
                    emitter.onComplete();
                    break;
                }

                String signal = "Red Signal";
                if (i % 2 == 0) {
                    signal = "Green Signal";
                }
                System.out.println("Emitting data: " + signal);
                emitter.onNext(i);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    emitter.onComplete();
                    break;
                }
            }
        });
    }
}

class PrintObserver implements Observer<Integer> {
    @Override
    public void onSubscribe(Disposable d) {
        // No-op
    }

    @Override
    public void onNext(Integer value) {
        System.out.println("Received value: " + value);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Data emission completed.");
    }
}

public class RxJavaDemo {
    public static void main(String[] args) {
        DataStream dataStream = new DataStream();
        PrintObserver observer = new PrintObserver();

        Observable<Integer> observable = dataStream.emitData();
        observable.subscribe(observer);

        Thread dataEmittingThread = new Thread(() -> observable.blockingSubscribe());
        dataEmittingThread.start();

        try {
            Thread.sleep(3000);
            dataEmittingThread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}