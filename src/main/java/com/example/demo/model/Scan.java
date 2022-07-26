package com.example.demo.model;

import java.util.Scanner;

public class Scan {
    private static Scan instance;

    private Scanner scanner = new Scanner(System.in);

    private Scan(){

    }

    public static Scan getInstance() {
        if (instance==null)instance=new Scan();
        return instance;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
