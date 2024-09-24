package com.kuznetsov.dictionarypc.entity;

import java.util.ArrayList;

public class Test {
    private int id;
    private String name;
    private ArrayList<Wordbook> wordbooks;

    public Test() {
        id = 1;
        name = "name";
        wordbooks = new ArrayList<>();
        wordbooks.add(new Wordbook(1, "a", 2, 1, "aaa"));
        wordbooks.add(new Wordbook(11, "aa", 22, 11, "aaabbb"));
    }
}
