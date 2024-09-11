package org.example;

import org.example.prac1.P1Ex1;
import org.example.prac1.P1Ex2;
import org.example.prac1.P1Ex3;
import org.example.prac2.P2Ex1;
import org.example.prac2.P2Ex2;
import org.example.prac2.P2Ex3;
import org.example.prac2.P2Ex4;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, NoSuchMethodException {
        String practice = "1";

        switch (practice) {
            case "1":
//                System.out.println("=============== Ex1 ===============");
//                new P1Ex1().execute();
//                System.out.println("=============== Ex2 ===============");
//                new P1Ex2().execute();
                System.out.println("=============== Ex3 ===============");
                new P1Ex3().execute();
                break;
            case "2":
                String directory = "src/main/java/org/example/prac2/";
                System.out.println("=============== Ex1 ===============");
                new P2Ex1(directory).execute();
                System.out.println("=============== Ex2 ===============");
                new P2Ex2(directory).execute();
                System.out.println("=============== Ex3 ===============");
                new P2Ex3(directory).execute();
                System.out.println("=============== Ex4 ===============");
                new P2Ex4(directory).execute();
                break;
            default:
                System.out.println("Invalid practice");
                break;
        }


    }
}