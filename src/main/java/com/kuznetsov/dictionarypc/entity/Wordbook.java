package com.kuznetsov.dictionarypc.entity;

public class Wordbook {
    private int id;
    private String name;
    private int groupId;

    public Wordbook(int id, String name, int groupId) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getGroupId() {
        return groupId;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}
