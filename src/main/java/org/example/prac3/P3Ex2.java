package org.example.prac3;

import io.reactivex.Observable;

import java.util.Random;

public class P3Ex2 {
    private Random random = new Random();

    private void ex1() {
        System.out.println("============= Ex2.1.1 =============");

        Observable<Integer> source = Observable.range(0, 1000).map(i -> random.nextInt(1000));
        Observable<Integer> result = source.map(n -> n * n);

        result.blockingSubscribe(res -> System.out.printf(res + ", "));
        System.out.println();
    }

    private void ex2() {
        System.out.println("============= Ex2.1.2 =============");

        Observable<Integer> source = Observable.range(0, 1000).map(i -> random.nextInt(1000));
        Observable<Integer> result = source.filter(n -> n > 500);

        result.blockingSubscribe(res -> System.out.printf(res + ", "));
        System.out.println();
    }

    private void ex3() {
        System.out.println("============= Ex2.1.3 =============");

        Observable<Integer> source = Observable.range(0, random.nextInt(1000)).map(i -> random.nextInt(1000));
        Observable<Long> result = source.count().toObservable();

        result.blockingSubscribe(res -> System.out.printf("Count: " + res));
        System.out.println();
    }

    private void ex4() {
        System.out.println("============= Ex2.2.1 =============");

        Observable<Integer> sourceNum = Observable.range(0, 1000).map(i -> random.nextInt(26));
        Observable<Character> sourceLet = Observable.range(0, 1000).map(i -> (char) random.nextInt(65, 91));
        Observable<String> result = Observable.zip(sourceLet, sourceNum, (let, num) -> let + String.valueOf(num));

        result.blockingSubscribe(res -> System.out.printf(res + ","));
        System.out.println();
    }

    private void ex5() {
        System.out.println("============= Ex2.2.2 =============");

        Observable<Integer> source1 = Observable.range(0, 1000).map(i -> random.nextInt(10));
        Observable<Integer> source2 = Observable.range(0, 1000).map(i -> random.nextInt(10, 20));
        Observable<Integer> result = Observable.concat(source1, source2);

        result.blockingSubscribe(res -> System.out.printf(res + ","));
        System.out.println();
    }

    private void ex6() {
        System.out.println("============= Ex2.2.3 =============");

        Observable<Integer> source1 = Observable.range(0, 1000).map(i -> random.nextInt(10));
        Observable<Integer> source2 = Observable.range(0, 1000).map(i -> random.nextInt(10, 20));
        Observable<Integer> result = Observable.zip(source1, source2, Observable::just).flatMap(observable -> observable);

        result.blockingSubscribe(res -> System.out.printf(res + ","));
        System.out.println();
    }

    private void ex7() {
        System.out.println("============= Ex2.3.1 =============");

        Observable<Integer> source = Observable.range(0, 10).map(i -> random.nextInt(10));
        Observable<Integer> result = source.skip(3);

        result.blockingSubscribe(res -> System.out.printf(res + ","));
        System.out.println();
    }

    private void ex8() {
        System.out.println("============= Ex2.3.2 =============");

        Observable<Integer> source = Observable.range(0, 10).map(i -> random.nextInt(10));
        Observable<Integer> result = source.take(5);

        result.blockingSubscribe(res -> System.out.printf(res + ","));
        System.out.println();
    }

    private void ex9() {
        System.out.println("============= Ex2.3.3 =============");

        Observable<Integer> source = Observable.range(0, random.nextInt(1, 10)).map(i -> random.nextInt(10));
        Observable<Integer> result = source.lastElement().toObservable();

        result.blockingSubscribe(res -> System.out.printf(res + ","));
        System.out.println();
    }


    public void execute() {
        ex1();
        ex2();
        ex3();
        ex4();
        ex5();
        ex6();
        ex7();
        ex8();
        ex9();
    }
}
