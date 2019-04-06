package com.company.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Banking {

    private static final int SIZER = 3;
    private int[] available = new int[SIZER];
    private ArrayList<Block> table;

    public Banking(){
        table = new ArrayList<>();
        take_input();

        System.out.println("P\tallocated\tmaximum\t\tneed");
        for (Block b:table)
            System.out.println(b.toString());

        calculate();

    }

    private void calculate(){

        ArrayList<String> order = new ArrayList<>();


        outer:
        while(table.size() != 0) {
            for (int i = 0; i < table.size(); i++) {
                Block block = table.get(i);
                if (block.compare()) {
                    block.execute();
                    System.out.println("Executing "+block.name+" Available Resources "+Arrays.toString(available));
                    order.add(block.name);
                    table.remove(i);
                    continue outer;
                }
            }
            System.out.println("System is in complete Deadlock");
            break;
        }

        System.out.println(order);
    }


    private void take_input(){


        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            Block block = new Block();
            System.out.println("Enter name :");
            block.name = scanner.nextLine();
            System.out.println("Enter "+SIZER+" Allocated Resources");
            for (int j = 0; j < SIZER; j++) {
                block.allocated[j] = scanner.nextInt();

            }
            scanner.nextLine();

            System.out.println("Enter "+SIZER+" maximum Resources");
            for (int j = 0; j < SIZER; j++) {
                block.maximum[j] = scanner.nextInt();
            }
            scanner.nextLine();

            for (int j = 0; j < SIZER; j++) {
                block.need[j] = block.maximum[j] - block.allocated[j];
            }
            table.add(block);

        }

        System.out.println("Enter "+SIZER+" available Resources");
        for (int j = 0; j < SIZER; j++) {
            available[j] = scanner.nextInt();
        }


    }


    class Block{
        String name;
        int[] allocated = new int[SIZER];
        int[] maximum = new int[SIZER];
        int[] need = new int[SIZER];

        public String toString() {
            return name+"\t"+Arrays.toString(allocated)+"\t"+Arrays.toString(maximum)+"\t"+Arrays.toString(need);
        }

        boolean compare(){
            for (int i = 0; i < SIZER; i++) {
                if (need[i] > available[i]) return false;
            }
            return true;
        }
        void execute(){
            for (int i = 0; i < SIZER; i++) {
                available[i]+=allocated[i];
            }
        }
    }
}
