package com.example.demo;

import com.example.demo.model.DB;
import com.example.demo.views.MenuChanger;


public class ConsoleApplication {
    public static void main(String[] args) {
        DB.loadData();
        MenuChanger.main(args);

//        Scanner scanner = Scan.getInstance().getScanner();
//        System.out.println("1 for load \n2 for new");
//        String command;
//        while (true){
//            command = scanner.nextLine().trim();
//            if (command.equals("1")||command.equals("2"))break;
//        }
//        if (command.equals("1")) DB.loadData();
//        new ConsoleView().run();
    }
}
