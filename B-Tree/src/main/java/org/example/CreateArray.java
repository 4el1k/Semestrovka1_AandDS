package org.example;

import java.awt.desktop.PrintFilesEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class CreateArray {
    public static void createArrayInFile(int size, String filename ){
        try {
            PrintWriter out = new PrintWriter(filename);
            for (int i = 0; i < size; i++) {
                out.print((int) (Math.random() * (Integer.MAX_VALUE)) + "\n");
            }
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
