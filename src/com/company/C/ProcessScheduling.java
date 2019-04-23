package com.page;

import java.util.*;

public class Main{


    private ArrayList<Block> process_table;

    public Main(){
        process_table = new ArrayList<>();
        take_input();
    }

    private void rouond_robin(){
        System.out.println("P\tAT\tBT\tCT\tWT\tTAT");

        int time = 0;
        process_table.sort(Comparator.comparingInt(o -> o.arrival_time));

        while(process_table.size()!=0) {

            Block block = process_table.get(0);
            if (time < block.arrival_time) time = block.arrival_time;

            if(block.bus_time>3){
                time+=3;
                block.bus_time-=3;
                process_table.remove(block);
                process_table.add(block);
            }else{
                time = time + block.bus_time;

                block.completion_time = time;

                block.tat = block.completion_time - block.arrival_time;
                block.wait_time = block.tat - block.bus_time;

                System.out.println(block.toString());
                process_table.remove(block);
            }

        }
    }

    private void sjf(){
        System.out.println("P\tAT\tBT\tCT\tWT\tTAT");

        int time = 0;
        process_table.sort(Comparator.comparingInt(o -> o.arrival_time));

        while(process_table.size() != 0){

//            select fastest from arrived
            Block block= null;
            for (Block b : process_table) {
                if (b.arrival_time <= time) {
                    if(block==null || block.bus_time > b.bus_time){
                        block = b;
                    }
                }
            }

            if (block == null) {
                if (time < process_table.get(0).arrival_time) time = process_table.get(0).arrival_time;
                continue;
            }
            time = time + block.bus_time;
            block.completion_time = time;

            block.tat = block.completion_time - block.arrival_time;
            block.wait_time = block.tat - block.bus_time;
            System.out.println(block.toString());
            process_table.remove(block);


        }

    }

    private void fcfs(){

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
            Block block = process_table.get(min_idx);


            if (time < block.arrival_time) time = block.arrival_time;

            time = time + block.bus_time;
            block.completion_time = time;

            block.tat = block.completion_time - block.arrival_time;
            block.wait_time = block.tat - block.bus_time;

            System.out.println(block.toString());


            process_table.remove(min_idx);
        }
    }



    private void take_input(){
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 4; i++) {
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

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        Main prb = new Main();
        String check;
        do {
            System.out.println("1.FCFS \n 2.SJF \n 3.Round Robin \n");
            int c = sc.nextInt();
            switch(c)
            {
                case 1:prb.fcfs();
                break;
                case 2: prb.sjf();
                break;
                case 3: prb.rouond_robin();
                break;
//                case 4: prb.priority();
//                break;
            }
            System.out.println("Do you want to Cont ... y/s");
            check = sc.next();
        }while(check=="y" || check=="Y");
    }

}
