package org.example;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassClient {
    public static int iteration;
    public static BTree<Integer> tree;
    public static void createTree(List<Integer> array){
        long[][] dataTime = new long[10000][3];
        long time;
        tree = new BTree<>(new IntegerComparator());
        for (int i = 0; i < 10000; i++) {
            iteration=0;
            time = System.nanoTime();
            tree.insert(array.size());
            time=System.nanoTime() - time;
            dataTime[i][0] = i;
            dataTime[i][1] = iteration;
            dataTime[i][2]= time;
        }
        //printArray(dataTime);
        System.out.println("iteration");

        dataTime = new long[100][3];
        for (int i = 0; i < 100; i++) {
            iteration=0;
            time = System.nanoTime();
            tree.isExist(array.get((int)(Math.random()*(10000-1))));
            time=System.nanoTime() - time;
            dataTime[i][0] = i;
            dataTime[i][1] = iteration;
            dataTime[i][2]= time;
        }
        System.out.println("search");
        //printArray(dataTime);
    }
    static void printArray(long arr[][]) {
        for (long[] j : arr) System.out.print(j[0] + " "+j[1]+" "+j[2]+"\n");
        System.out.println();
    }
}
