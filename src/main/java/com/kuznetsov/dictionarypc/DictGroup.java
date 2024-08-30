package com.kuznetsov.dictionarypc;

public class DictGroup {
    private int id;
    private String name;
    DictGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    int getId() { return id; }

    String getName() { return name; }
}
