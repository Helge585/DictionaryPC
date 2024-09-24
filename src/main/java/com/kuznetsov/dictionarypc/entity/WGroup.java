package com.kuznetsov.dictionarypc.entity;

public class WGroup {
    private int id;
    private String name;
    public WGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }
}
