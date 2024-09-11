package org.example.prac1;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Thread.sleep;

public class P1Ex3 {
    private enum FileType {
            XML,
            JSON,
            XLS
    }

    private record MyFile(FileType type, int size) {}

    private record FileGenerator(ArrayBlockingQueue<MyFile> queue) implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                try {
                    sleep(random.nextInt(100, 1001));
                    int size = random.nextInt(10, 101);
                    FileType type = FileType.values()[random.nextInt(FileType.values().length)];
                    MyFile file = new MyFile(type, size);
                    queue.put(file);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private record FileProcessor(ArrayBlockingQueue<MyFile> queue, FileType allowedType) implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    MyFile file = queue.peek();
                    if (file != null && file.type() == allowedType) {
                        queue.take();
                        sleep(file.size() * 7L);
                        System.out.println("Size: " + file.size() + " | Type: " + file.type());
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void execute() {
        ArrayBlockingQueue<MyFile> queue = new ArrayBlockingQueue<>(5);

        Thread generator  = new Thread(new FileGenerator(queue));
        Thread processorJSON = new Thread(new FileProcessor(queue, FileType.JSON));
        Thread processorXML = new Thread(new FileProcessor(queue, FileType.XML));
        Thread processorXLS = new Thread(new FileProcessor(queue, FileType.XLS));

        generator.start();
        processorJSON.start();
        processorXML.start();
        processorXLS.start();
    }
}
