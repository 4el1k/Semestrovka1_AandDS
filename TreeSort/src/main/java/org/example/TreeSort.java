package org.example;

import com.sun.org.apache.xerces.internal.impl.dv.xs.AnyURIDV;

import java.util.List;

public class TreeSort {
    public static int iteration;
    public static List<Integer> sort(List<Integer> inputArray){
        Tree root = new Tree(inputArray.get(0));
        for (int i = 1; i < inputArray.size(); i++) {
            iteration++;
            root.insert(new Tree(inputArray.get(i)));
        }
        return root.makeResultArray();
    }
}
