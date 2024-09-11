package org.example.prac1;

import org.example.Tester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class P1Ex1 {
    private final int n;

    public P1Ex1() {
        this.n = 10000;
    }

    public Integer sequenceMin(int[] list) throws InterruptedException {
        int min = Integer.MAX_VALUE;
        for (int n : list) {
            if (n < min) {
                sleep(1);
                min = n;
            }
        }
        return min;
    }

    public Integer threadMin(int[] list) throws InterruptedException, ExecutionException {
        int numThreads = Math.min(Runtime.getRuntime().availableProcessors(), list.length);
        int batchSize = list.length / numThreads;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int finalI = i;

            tasks.add(() -> {
                int m = Integer.MAX_VALUE;
                int start = finalI * batchSize;
                int end = Math.min((finalI + 1) * batchSize, list.length);
                for (int j = start; j < end; j++) {
                    if (list[j] < m) {
                        sleep(1);
                        m = list[j];
                    }
                }
                return m;
            });
        }

        List<Future<Integer>> futures = executorService.invokeAll(tasks);
        int min = Integer.MAX_VALUE;

        for (Future<Integer> future : futures) {
            int m = future.get();
            if (m < min) {
                sleep(1);
                min = m;
            }
        }

        executorService.shutdown();
        return min;
    }

    public Integer forkJoinMin(int[] list) {
        class ForkMin extends RecursiveTask<Integer> {
            private final int[] list;
            private final int start;
            private final int end;

            ForkMin(int[] list, int start, int end) {
                this.list = list;
                this.start = start;
                this.end = end;
            }

            @Override
            protected Integer compute() {
                if (end - start <= 1000) {
                    int m = Integer.MAX_VALUE;
                    for (int i = start; i < end; i++) {
                        if (list[i] < m) {
                            m = list[i];
                        }
                    }
                    return m;
                }
                int middle = (start + end) / 2;
                ForkMin left = new ForkMin(list, start, middle);
                ForkMin right = new ForkMin(list, middle, end);
                left.fork();
                right.fork();
                int lRes = left.join();
                int rRes = right.join();
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return Math.min(lRes, rRes);
            }
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkMin forkMin = new ForkMin(list, 0, list.length);
        return forkJoinPool.invoke(forkMin);
    }

    public void execute() {
        int[] randomArray = new int[this.n];
        Random random = new Random(this.n);

        for (int i = 0; i < this.n; i++) {
            randomArray[i] = random.nextInt();
        }

        Tester tester = new Tester();
        tester.testFunction("sequenceMin", () -> {
            try {
                sequenceMin(randomArray);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        tester.testFunction("threadMin", () -> {
            try {
                threadMin(randomArray);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        tester.testFunction("forkJoinMin", () -> {
            forkJoinMin(randomArray);
        });
    }
}
