package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;

public class Node<T> {
    static public int degree;
    public boolean isLeaf;
    public Vector<Node<T>> children; //элементов в этом листе на 1 больше, чем элементов в keys
    public Vector<T> keys;

    public boolean isLeaf() {
        return isLeaf;
    }


    Node(boolean isLeaf){
        this.isLeaf = isLeaf;
        children=new Vector<>();
        keys=new Vector<>();

    }

}
