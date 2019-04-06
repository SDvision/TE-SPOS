package com.company.D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PageReplacement {

    private ArrayList<String> stream;
    private Block[] frame;
    private final static int FRAME_SIZE = 3;

    class Block{
        String value;
        int last_use_idx;
        Block(String value, int last_use_idx) {
            this.value = value;
            this.last_use_idx = last_use_idx;
        }
    }

    public PageReplacement(){

        stream = new ArrayList<>();
        frame = new Block[FRAME_SIZE];
        temp_input();
        lru();

    }

    private void lru(){

        stream_for:
        for (int i = 0; i < stream.size(); i++) {
            for (int j = 0; j < frame.length; j++) {

                if (frame[j]==null){
                    frame[j] = new Block(stream.get(i),i);

                    System.out.print("PF :");
                    print_frame();

                    continue stream_for;
                }
                if(frame[j].value.equals(stream.get(i))){

                    System.out.print("PH :");
                    print_frame();

                    continue stream_for;
                }

            }
            // is full and not found (i.e we need to replace)
            int min_lu_idx = frame[0].last_use_idx;
            int min_idx = 0;
            for (int j = 0; j < frame.length; j++) {
                if(min_lu_idx > frame[j].last_use_idx){
                    min_lu_idx = frame[j].last_use_idx;
                    min_idx = j;
                }
            }

            frame[min_idx] = new Block(stream.get(i),i);
            System.out.print("PH :");
            print_frame();


        }


    }

    private void print_frame(){
        System.out.print("[ ");
        for (int i = 0; i < frame.length; i++) {
            if (frame[i] == null){
                System.out.print("null ");
            }else{
                System.out.print(frame[i].value+" ");
            }
        }
        System.out.println("]");
    }

    private void temp_input(){
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 5; i++) {
            stream.add(scanner.nextLine());
        }
        System.out.println(stream);
    }






}
