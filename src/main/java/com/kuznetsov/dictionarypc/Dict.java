package com.kuznetsov.dictionarypc;

public class Dict {
    private int id;
    private String name;
    private int groupId;

    Dict(int id, String name, int groupId) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    int getGroupId() {
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
