package org.example.prac3;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import java.util.Random;

import static java.lang.Thread.sleep;

public class P3Ex1 {
    static class TemperatureSensor extends Observable<Integer> {
        private final PublishSubject<Integer> subject = PublishSubject.create();

        @Override
        protected void subscribeActual(Observer<? super Integer> observer) {
            subject.subscribe(observer);
        }

        private void start() {
            Random random = new Random();
            new Thread(() -> {
                while (true) {
                    int temperature = random.nextInt(15, 31);
                    subject.onNext(temperature);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }

    static class CO2Sensor extends Observable<Integer> {
        private final PublishSubject<Integer> subject = PublishSubject.create();

        @Override
        protected void subscribeActual(Observer<? super Integer> observer) {
            subject.subscribe(observer);
        }

        private void start() {
            Random random = new Random();
            new Thread(() -> {
                while (true) {
                    int temperature = random.nextInt(30, 101);
                    subject.onNext(temperature);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }

    class Alarm implements Observer<Integer> {

        private final Integer CO2Limit = 70;
        private final Integer temperatureLimit = 25;

        private Integer temperature;
        private Integer co2;

        @Override
        public void onSubscribe(@NonNull Disposable disposable) {}
        @Override
        public void onError(@NonNull Throwable throwable) {}
        @Override
        public void onComplete() {}

        @Override
        public void onNext(@NonNull Integer integer) {
            if (integer <= 30) {
                temperature = integer;
            } else {
                co2 = integer;
            }
            if (temperature != null && co2 != null) {
                String message = "";
                if (temperature > temperatureLimit && co2 > CO2Limit) {
                    message = " | ALARM!!!";
                }
                else if (temperature > temperatureLimit) {
                    message = " | Temperature warning";
                }
                else if (co2 > CO2Limit) {
                    message = " | CO2 warning";
                }
                System.out.println(String.format("CO2:%3d | temperature:%3d%s", co2, temperature, message));
            }
        }
    }

    public void execute() {
        TemperatureSensor temperatureSensor = new TemperatureSensor();
        CO2Sensor co2Sensor = new CO2Sensor();
        Alarm alarm = new Alarm();

        temperatureSensor.subscribe(alarm);
        co2Sensor.subscribe(alarm);

        temperatureSensor.start();
        co2Sensor.start();
    }
}
