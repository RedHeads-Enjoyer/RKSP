package org.example.prac2;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class P2Ex4 {
    private final HashMap<String, String> files = new HashMap<String, String>();
    private final String directory;

    public P2Ex4(String directory) {
        this.directory = directory;
    }

    public static String stringDifference(String original, String modified) {
        String[] originalLines = original.split("\n");
        String[] modifiedLines = modified.split("\n");

        List<String> addedLines = new ArrayList<>();
        List<String> removedLines = new ArrayList<>();

        for (String line : modifiedLines) {
            if (!Arrays.asList(originalLines).contains(line)) {
                addedLines.add(line);
            }
        }

        for (String line : originalLines) {
            if (!Arrays.asList(modifiedLines).contains(line)) {
                removedLines.add(line);
            }
        }

        StringBuilder changes = new StringBuilder();

        if (!addedLines.isEmpty()) {
            changes.append("Added {\n");
            for (String line : addedLines) {
                changes.append(line).append("\n");
            }
            changes.delete(changes.length() - 1, changes.length());
            changes.append("\n}\n");
        }

        if (!removedLines.isEmpty()) {
            changes.append("Deleted {\n");
            for (String line : removedLines) {
                changes.append(line).append("\n");
            }
            changes.delete(changes.length() - 1, changes.length());
        }
        changes.append("\n}\n");

        return changes.toString();
    }

    public void execute() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        Path path = Paths.get(directory);

        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                if (!event.context().toString().endsWith(".txt")) continue;

                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("Created: " + event.context());
                    String text = new String(Files.readAllBytes(Paths.get(this.directory + event.context().toString())));
                    files.put(event.context().toString(), text);
                    continue;
                }

                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("Deleted: " + event.context());
                    System.out.println(Files.exists(Paths.get(this.directory + event.context().toString())));
                    files.remove(event.context().toString());
                    continue;
                }

                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("Modified: " + event.context());
                    String text = new String(Files.readAllBytes(Paths.get(this.directory + event.context().toString())));
                    System.out.println(stringDifference(files.get(event.context().toString()), text));
                    files.put(event.context().toString(), text);
                }
            }
            key.reset();
        }
    }
}
