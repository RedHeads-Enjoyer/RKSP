package org.example.prac2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ex1 {
    private final String directory;

    public Ex1(String directory) {
        this.directory = directory;
    }

    public void createFile(String filename) throws IOException {
        try {
            Path newFilePath = Paths.get(this.directory + filename);
            Files.createFile(newFilePath);
            System.out.println("File created successfully: " + filename);
        } catch (IOException e) {
            System.err.println("Error creating file: " + filename);
        }
    }

    public void writeFile(String filename, String text) throws IOException {
        Path path = Paths.get(this.directory + filename);
        if (!Files.exists(path)) {
            System.out.println("File does not exist: " + filename);
        } else {
            Files.write(path, text.getBytes());
            System.out.println("File written successfully: " + filename);
        }
    }

    public void readFile(String filename) throws IOException {
        Path path = Paths.get(this.directory + filename);
        if (!Files.exists(path)) {
            System.out.println("File does not exist: " + filename);
        } else {
            String text = new String(Files.readAllBytes(path));
            System.out.println("File read successfully: " + filename);
            System.out.println(text);
        }
    }

    public void execute() throws IOException {
        createFile("ffff.txt");
        writeFile("ffff.txt", "asdasdasd asdasd\n asdasdsd");
        readFile("ffff.txt");
    }
}