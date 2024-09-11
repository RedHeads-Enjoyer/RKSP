package org.example.prac1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class P1Ex2 {
    public void execute() throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        while (true) {
            System.out.println("Input number: ");
            int n = scanner.nextInt();
            if (n == 0) {
                executorService.shutdown();
                break;
            };

            Future<Integer> future = executorService.submit(() -> {
                Thread.sleep(1000L * ThreadLocalRandom.current().nextInt(1, 6));
                return n * n;
            });

            System.out.println(n + "^2 = " + future.get());
        }
    }
}
