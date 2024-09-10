package org.example;

import org.example.prac2.Ex1;
import org.example.prac2.Ex2;
import org.example.prac2.Ex3;
import org.example.prac2.Ex4;

import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String directory = "src/main/java/org/example/prac2/";

//        new Ex1(directory).execute();
//        new Ex2(directory).execute();
//        new Ex3(directory).execute();
        new Ex4(directory).execute();
    }
}