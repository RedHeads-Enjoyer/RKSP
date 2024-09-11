package org.example.prac2;

import org.apache.commons.io.FileUtils;
import org.example.Tester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class P2Ex2 {
    private final String directory;

    public P2Ex2(String directory) {
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

    public void execute() throws IOException, NoSuchMethodException {
        long startTime, endTime, totalTime, memoryUsage;
        Runtime runtime;

        String[] filenames = {"100MB_stream_copy.bin", "100MB_channel_copy.bin", "100MB_apacheIO_copy.bin", "100MB_files_copy.bin"};
        for (String filename : filenames) {
            File file = new File(directory + filename);
            if (file.exists()) {
                file.delete();
            }
        }

        Tester tester = new Tester();

        tester.testFunction("copyFileStreams", () -> {
            copyFileStreams("100MB.bin", filenames[0]);
        });

        tester.testFunction("copyFileStreams", () -> {
            copyFileChannels("100MB.bin", filenames[1]);
        });

        tester.testFunction("copyFileStreams", () -> {
            try {
                copyFileApacheIO("100MB.bin", filenames[2]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        tester.testFunction("Files class", () -> {
            try {
                copyFileFiles("100MB.bin", filenames[3]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
