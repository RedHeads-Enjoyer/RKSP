package org.example.prac3;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Thread.sleep;

public class P4Ex4 {
    private Random random = new Random();

    private enum FileType {
        XML,
        JSON,
        XLS
    }

    private record MyFile(FileType type, int size) {}

    class FileGenerator {
        public Observable<MyFile> generateFile() {
            return Observable
                    .fromCallable(() -> {
                        try {
                            sleep(random.nextInt(100, 1001));
                            int size = random.nextInt(10, 101);
                            FileType type = FileType.values()[random.nextInt(FileType.values().length)];
                            return new MyFile(type, size);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .repeat()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());
        }
    }

    class FileQueue {
        private final Observable<MyFile> fileObservable;

        public FileQueue(int capacity, FileGenerator fileGenerator) {
            this.fileObservable = fileGenerator.generateFile()
                    .replay(capacity)
                    .autoConnect();
        }
        public Observable<MyFile> getFileObservable() {
            return fileObservable;
        }
    }


    static class FileProcessor {
        private final FileType supportedFileType;
        public FileProcessor(FileType supportedFileType) {
            this.supportedFileType = supportedFileType;
        }

        public void processFiles(Observable<MyFile> fileObservable) {
            fileObservable
                    .filter(file -> file.type.equals(supportedFileType))
                    .doOnNext(file -> {
                        try {
                            Thread.sleep(file.size() * 7L);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Size: " + file.size() + " | Type: " + file.type());
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe();
        }

    }

    public void execute() {
        int queueCapacity = 5;
        FileGenerator fileGenerator = new FileGenerator(); // Create a single FileGenerator instance
        FileQueue fileQueue = new FileQueue(queueCapacity, fileGenerator);
        FileType[] supportedFileTypes = {FileType.JSON, FileType.XML, FileType.XLS};
        for (FileType fileType : supportedFileTypes) {
            new FileProcessor(fileType)
                    .processFiles(fileQueue.getFileObservable());
        }
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
