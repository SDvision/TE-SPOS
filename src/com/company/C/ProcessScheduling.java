package com.company.C;

import java.util.*;

public class ProcessScheduling {


    private ArrayList<Block> process_table;

    public ProcessScheduling(){
        process_table = new ArrayList<>();
        take_input();
        perform();
//        process_table.sort(Comparator.comparingInt(o -> o.arrival_time));

    }

    private void perform(){

        System.out.println("P\tAT\tBT\tCT\tWT\tTAT");

        int time = 0;
        while (process_table.size() != 0) {
            int min = process_table.get(0).arrival_time;
            int min_idx = 0;
            for (int i = 0; i < process_table.size(); i++) {
                if (process_table.get(i).arrival_time < min) {
                    min = process_table.get(i).arrival_time;
                    min_idx = i;
                }
            }
            Block bt = process_table.get(min_idx);


            if (time < bt.arrival_time) time = bt.arrival_time;

            time = time + bt.bus_time;
            bt.completion_time = time;

            bt.tat = bt.completion_time - bt.arrival_time;
            bt.wait_time = bt.tat - bt.bus_time;

            System.out.println(bt.toString());


            process_table.remove(min_idx);
        }
    }



    private void take_input(){
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 3; i++) {
            Block block = new Block();
            System.out.println("Enter process Name :");
            block.name = scanner.nextLine();
            System.out.println("Enter Arrival Time :");
            block.arrival_time = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Bus(CPU) Time :");
            block.bus_time = Integer.parseInt(scanner.nextLine());

            process_table.add(block);
            System.out.println("Block added\n");

        }
    }

    class Block{
        String name;
        int arrival_time,bus_time,completion_time,wait_time,tat;

        public String toString(){
            return name+"\t"+arrival_time+"\t"+bus_time+"\t"+completion_time+"\t"+wait_time+"\t"+tat;
        }
    }

}
