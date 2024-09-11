package org.example;

public class Tester {
    public void testFunction(String label, Runnable function) {
        long startTime = System.nanoTime();
        function.run();
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        Runtime runtime = Runtime.getRuntime();
        long memoryUsage = runtime.totalMemory() - runtime.freeMemory();

        System.out.println(label);
        System.out.println("Time: " + totalTime + " ns");
        System.out.println("Memory: " + memoryUsage + "\n");
    }
}
