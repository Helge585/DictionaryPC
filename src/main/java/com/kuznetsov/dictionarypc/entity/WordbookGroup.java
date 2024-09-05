package com.kuznetsov.dictionarypc.entity;

public class WordbookGroup {
    private int id;
    private String name;
    public WordbookGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }
}
