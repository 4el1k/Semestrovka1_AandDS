package org.example;

import java.lang.instrument.UnmodifiableClassException;
import java.util.Comparator;
import java.util.List;

public class BTree<T> {

    private Node<T> root;
    private int degree;
    private Comparator<T> comparator;

    BTree(Comparator<T> comparator){
        degree=4;
        root=null;
        this.comparator=comparator;
    }

    public void insert(T key){
        if(root==null){
            root = new Node<>(true);
            ClassClient.iteration++;
        }
        if (isNodeFull(root)){
            Node<T> newRoot = new Node<>(false);
            newRoot.children.add(root);
            root = newRoot;
            splitChildren(root,0);
            ClassClient.iteration++;
        }
        insertInNonFullNode(root,key);
    }

    private boolean isNodeFull(Node<T> node){
        return node.keys.size() == 2*degree -1;
    }

    private void insertInNonFullNode(Node<T> node, T key){
        ClassClient.iteration++;
        int currentPosition = node.keys.size()-1;
        if (node.isLeaf()){
            node.keys.setSize(node.keys.size()+1);
            while (currentPosition>=0&&compare(key,node.keys.get(currentPosition))){
                node.keys.set(currentPosition+1,node.keys.get(currentPosition));
                --currentPosition;
                ClassClient.iteration++;
            }
            node.keys.set(currentPosition+1,key);
        }
        else {
            while (currentPosition>=0 && compare(key, node.keys.get(currentPosition))){
                currentPosition--;
                ClassClient.iteration++;
            }
            if (isNodeFull(node.children.get(currentPosition+1))){
                splitChildren(node,currentPosition+1);
                if (compare(node.keys.get(currentPosition+1),key)){
                    ++currentPosition;
                }
            }
            insertInNonFullNode(node.children.get(currentPosition+1),key);
        }
    }

    private void splitChildren(Node<T> node, int childPosition){
        ClassClient.iteration++;
        Node<T> oldChild = node.children.get(childPosition);
        Node<T> newChild = new Node<>(oldChild.isLeaf());
        for (int i = 0; i < degree-1; ++i) {
            newChild.keys.add(oldChild.keys.get(i+degree));
            ClassClient.iteration++;
        }
        if (!oldChild.isLeaf()){
            for (int i = 0; i < degree; ++i) {
                newChild.children.add(oldChild.children.get(i+degree));
                ClassClient.iteration++;
            }
        }
        T oldKey = oldChild.keys.get(degree-1);
        oldChild.keys.setSize(degree-1);
        oldChild.children.setSize(degree);

        node.children.setSize(node.children.size()+1);
        for (int i = node.children.size()-1; i > childPosition+1; --i) {
            node.children.set(i,node.children.get(i-1));
            ClassClient.iteration++;
        }
        node.children.set(childPosition+1,newChild);
        node.keys.setSize(node.keys.size()+1);
        for (int i = node.keys.size()-1; i >childPosition ; --i) {
            node.keys.set(i,node.keys.get(i-1));
            ClassClient.iteration++;
        }
        node.keys.set(childPosition,oldKey);
    }
    

    public boolean isExist(T elementToResearch){
        return getKeyPosition(root,elementToResearch);
    }

    private boolean getKeyPosition(Node<T> node, T key){
        int i = 0;
        while (i<node.keys.size() && (!compare(key,node.keys.get(i)))){ // можно было бы и бин боиск сделать
            i++;
            ClassClient.iteration++;
        }
        if (i<node.keys.size()&& comparator.compare(key,node.keys.get(i))==0){
            return true;
        }
        if (node.isLeaf){
            return false;
        }
        return getKeyPosition(node.children.get(i),key);

    }
    
    public void delete(T key){
        delete(root,key);
    }
    
    private void delete(Node<T> node,T key){
        int i = 0;
        ClassClient.iteration++;
        while (i<node.keys.size() && (!compare(key,node.keys.get(i)))){ // можно было бы и бин боиск сделать
            i++;
            ClassClient.iteration++;
        }
        if (node.isLeaf && comparator.compare(key,node.keys.get(i))!=0 ){
            return;
        }
        if (i<node.keys.size()&& comparator.compare(key,node.keys.get(i))==0){ // мы нашли нужный элемент для удаления
            //случай 1
            if (node.isLeaf){
                for (int j = i+1; j <node.keys.size() ; j++) {
                    node.keys.set(i,node.keys.get(i-1)); //просто удалили элемент
                    ClassClient.iteration++;
                }
                node.keys.setSize(node.keys.size()-1);
                node.children.setSize(node.children.size()-1);

            }
            //случай 2
            else {
                Node<T> children_i = node.children.get(i);
                Node<T> children_i_plus_1 = node.children.get(i+1);

                //пункт а
                if (children_i.keys.size()>degree-1){
                    //мы не просто удаляем
                    T newKey = children_i.keys.get(children_i.keys.size()-1);
                    delete(children_i,children_i.keys.get(children_i.keys.size()-1));
                    node.keys.set(i,newKey);

                }
                //пункт b
                else if(children_i_plus_1.keys.size()>degree-1){
                    T newKey = children_i_plus_1.keys.get(0);
                    delete(children_i_plus_1,children_i_plus_1.keys.get(0)); //второй параметр можно было не переписывать и записать туда newKey
                    node.keys.set(i,newKey);
                }
                //пункт c
                else {
                    mergeChildren(children_i,node.keys.get(i), children_i_plus_1);
                    for (int j = i+1; j <node.keys.size() ; j++) {
                        node.keys.set(j-1,node.keys.get(j));
                        node.children.set(j,node.children.get(j-1));
                        ClassClient.iteration++;
                    }
                    node.keys.setSize(node.keys.size()-1);
                    node.children.setSize(node.children.size()-1);
                    delete(children_i,key);

                }
            }
            
        }
        //случай 3
        else {
            //пунтк а
            if (node.children.get(i).keys.size()>degree-1){
                delete(node.children.get(i),key);
                //заметим, что тут мы не сокращаем размер вектора
            }
            else{
                Node<T> children_i = node.children.get(i);
                Node<T> children_i_minus_1=null;
                Node<T> children_i_plus_1=null;
                if (i!=0){
                    children_i_minus_1 = node.children.get(i-1);
                }
                if (node.children.size()-1!=i){
                    children_i_plus_1 = node.children.get(i+1);
                }


                //пункт б
                if (children_i_plus_1!=null && children_i_plus_1.keys.size()>degree-1){
                    T newKey = children_i_plus_1.keys.get(0);
                    delete(children_i_plus_1,children_i_plus_1.keys.get(0)); //второй параметр можно было не переписывать и записать туда newKey


                    T oldKey = node.keys.get(i);
                    node.keys.set(i,newKey);
                    insertInNonFullNode(children_i, oldKey); // это ошибка, здесь вообще не нужно использовать добавление
                    delete(children_i,key);
                }
                else if (children_i_minus_1!=null && children_i_minus_1.keys.size()>degree-1){
                    T newKey = children_i_minus_1.keys.get(children_i_minus_1.keys.size()-1);
                    delete(children_i_minus_1,newKey);


                    T oldKey = node.keys.get(i);
                    node.keys.set(i,newKey);
                    insertInNonFullNode(children_i,oldKey); // это ошибка, здесь вообще не нужно использовать добавление
                    delete(children_i,key);
                }
                //пункт c
                else{
                    if (children_i_plus_1!=null){
                        mergeChildren(children_i,node.keys.get(i),children_i_plus_1);
                        for (int j = i+1; j <node.keys.size() ; j++) {
                            node.keys.set(j-1,node.keys.get(j));
                            node.children.set(j,node.children.get(j-1));
                            ClassClient.iteration++;
                        }
                        node.keys.setSize(node.keys.size()-1);
                        node.children.setSize(node.children.size()-1);
                        delete(children_i,key);
                    }
                    else {
                        mergeChildren(children_i_minus_1,key,children_i);
                        for (int j = i+1; j <node.keys.size() ; j++) {
                            node.keys.set(j-1,node.keys.get(j));
                            node.children.set(j,node.children.get(j-1));
                            ClassClient.iteration++;
                        }
                        node.keys.setSize(node.keys.size()-1);
                        node.children.setSize(node.children.size()-1);
                        delete(children_i_minus_1,key);
                    }
                }
            }

        }
    }

    //предпологается, что использоваться это будет, когда
    //child1.keys.size()==child1.keys.size()==degree-1
    private void mergeChildren(Node<T> child1,T element,Node<T> child2){
        child1.keys.add(element);
        child1.keys.setSize(child1.keys.size()+(degree-1));
        int i = 0;
        while (!isNodeFull(child1)){
            child1.keys.add(child2.keys.get(i));
            child1.children.add(child2.children.get(i));
            ClassClient.iteration++;
        }
        //по итогу все ключи и дети из child2 добавяться в child11
        //и в child1 теперь полный
    }

    private boolean compare(T o1, T o2){ // странный компоратор
        //o1<=o2 -> true
        int comparatorResult = comparator.compare(o1,o2);
        return comparatorResult<=0;
    }


}
