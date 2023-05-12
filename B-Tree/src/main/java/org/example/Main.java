package org.example;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CreateArray.createArrayInFile(10000,"dataset2");
        List<Integer> array;
        long[][] dataTime=new long[99][3];

        try (Scanner sc= new Scanner(new FileReader("dataset2"))) {
            array = new ArrayList<>();
            for (int j = 0; j < 10000; j++) {
                array.add(sc.nextInt());
            }

            ClassClient.iteration=0;
            ClassClient.createTree(array);
            System.out.println();


        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }



    }
}