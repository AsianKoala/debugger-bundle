package com.company;

import java.util.*;

public abstract class Robot {
    ArrayList<ArrayList<Integer>> pathCache;
    void setPathCache(ArrayList<ArrayList<Integer>> aList) {
        pathCache.addAll(aList);
    }

    void init() {
        pathCache = new ArrayList<>();
    }

    void loop_once() {
        for(ArrayList<Integer> a : pathCache) {
            for(int b : a) {
                System.out.println(b + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Auto lol = new Auto();
        lol.init();
        lol.loop_once();
    }
}

abstract class BaseAuto extends Robot {
    abstract ArrayList<ArrayList<Integer>> pathList();
    @Override
    void init() {
        super.init();
        setPathCache(pathList());
    }
}

class Auto extends BaseAuto {
    @Override
    ArrayList<ArrayList<Integer>> pathList() {
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        ArrayList<Integer> a1 = new ArrayList<>();
        a1.add(2);
        a1.add(4);
        a1.add(6);
        a1.add(8);
        ArrayList<Integer> a2 = new ArrayList<>();
        a2.add(3);
        a2.add(5);
        a2.add(7);
        a2.add(9);
        returnList.add(a1);
        returnList.add(a2);
        return returnList;
    }
}