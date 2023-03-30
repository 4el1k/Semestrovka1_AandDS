package org.example;


import com.sun.org.apache.xerces.internal.impl.dv.xs.AnyURIDV;

import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static void printArray(long arr[][]) {
        for (long[] j : arr) System.out.print(j[0] + " "+j[1]+" "+j[2]+"\n");
        System.out.println();
    }

    public static void main(String[] args) {
        List<Integer> result;
        List<Integer> array;
        long[][] dataTime=new long[99][3];
        for (int i = 1; i < 100; i++) {
            try (Scanner sc = new Scanner(new FileReader(i + ".txt"))) {
                int length = i*100;
                dataTime[i-1][0]=length;
                array = new ArrayList<>();
                for (int j=0;j<length;j++){
                    array.add(sc.nextInt());
                }
                TreeSort.iteration=0;
                long time = System.nanoTime();
                //TreeSort.sort(array);
                result = TreeSort.sort(array);
                time=System.nanoTime() - time;
                dataTime[i-1][0] = length;
                dataTime[i-1][1] = time;
                dataTime[i-1][2] = TreeSort.iteration;

            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }

        }
        printArray(dataTime);
    }


    public static void maint(String[] args) {
        List<Integer> list = new ArrayList<>();
        long[] dataTime=new long[3];
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        long time = System.nanoTime();
        list = TreeSort.sort(list);
        time=System.nanoTime() - time;
        dataTime[0] = list.size();
        dataTime[1] = time;

        list.stream().forEach(System.out::println);
    }
}