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
        replace(false);

    }

    private void replace(boolean isOptimal){

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
            if(isOptimal){
                int replace_close_use = 0;
                int replace_idx = 0;
                for (Block aFrame : frame) {
                    int close_use = stream.size();
                    for (int k = i; k < stream.size(); k++)
                        if (aFrame.value.equals(stream.get(k)) && k < close_use) close_use = k;
                    if (close_use > replace_close_use) replace_close_use = close_use;
                }
                frame[replace_idx] = new Block(stream.get(i),i);

            }else {
                // is full and not found (i.e we need to replace)
                int min_lu_idx = frame[0].last_use_idx;
                int min_idx = 0;
                for (int j = 0; j < frame.length; j++) {
                    if (min_lu_idx > frame[j].last_use_idx) {
                        min_lu_idx = frame[j].last_use_idx;
                        min_idx = j;
                    }
                }

                frame[min_idx] = new Block(stream.get(i), i);
                System.out.print("PH :");
                print_frame();

            }
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
