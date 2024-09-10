package org.example.prac2;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Ex2 {
    private final String directory;

    public Ex2(String directory) {
        this.directory = directory;
    }

    public void copyFileStreams(String source, String target) {
        try (FileInputStream sourceStream = new FileInputStream(this.directory + source);
             FileOutputStream targetStream = new FileOutputStream(this.directory + target)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = sourceStream.read(buffer)) > 0) {
                targetStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void copyFileChannels(String source, String target) {
        try (FileInputStream sourceStream = new FileInputStream(this.directory + source);
             FileChannel sourceChannel = sourceStream.getChannel();
             FileOutputStream targetStream = new FileOutputStream(this.directory + target);
             FileChannel targetChannel = targetStream.getChannel()) {
            sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void copyFileApacheIO(String source, String target) throws IOException {
        FileUtils.copyFile(new File(this.directory + source), new File(this.directory + target));
    }

    public void copyFileFiles(String source, String target) throws IOException {
        Files.copy(Paths.get(this.directory + source), Paths.get(this.directory + target));
    }

    public void execute() throws IOException {
        long startTime, endTime, totalTime, memoryUsage;
        Runtime runtime;

        String[] filenames = {"100MB_stream_copy.bin", "100MB_channel_copy.bin", "100MB_apacheIO_copy.bin", "100MB_files_copy.bin"};
        for (String filename : filenames) {
            File file = new File(directory + filename);
            if (file.exists()) {
                file.delete();
            }
        }

        startTime = System.nanoTime();
        copyFileStreams("100MB.bin", filenames[0]);
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        runtime = Runtime.getRuntime();
        memoryUsage = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("FileInputStream/FileOutputStream");
        System.out.println("Time: " + totalTime + " ns");
        System.out.println("Memory: " + memoryUsage + "\n");

        startTime = System.nanoTime();
        copyFileChannels("100MB.bin", filenames[1]);
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        runtime = Runtime.getRuntime();
        memoryUsage = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("FileChannel");
        System.out.println("Time: " + totalTime + " ns");
        System.out.println("Memory: " + memoryUsage + "\n");

        startTime = System.nanoTime();
        copyFileApacheIO("100MB.bin", filenames[2]);
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        runtime = Runtime.getRuntime();
        memoryUsage = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Apache Commons IO");
        System.out.println("Time: " + totalTime + " ns");
        System.out.println("Memory: " + memoryUsage + "\n");

        startTime = System.nanoTime();
        copyFileFiles("100MB.bin", filenames[3]);
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        runtime = Runtime.getRuntime();
        memoryUsage = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Files class");
        System.out.println("Time: " + totalTime + " ns");
        System.out.println("Memory: " + memoryUsage + "\n");
    }
}
