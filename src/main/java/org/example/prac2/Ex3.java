package org.example.prac2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Ex3 {
    private final String directory;

    public Ex3(String directory) {
        this.directory = directory;
    }

    public short calculateCheckSum(String filename) throws IOException {
        short checkSum = 0;
        try ( FileInputStream sourceStream = new FileInputStream(this.directory + filename);
        FileChannel sourceChannel = sourceStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            while (sourceChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    checkSum += buffer.get();
                    checkSum ^= (short) (checkSum >> 8);
                }
                buffer.clear();
            }
        }
        return checkSum;
    }

    public void execute() throws IOException {
        String filename = "ffff.txt";
        System.out.println("The checksum of " + filename + ": " + calculateCheckSum(filename));
    }
}
