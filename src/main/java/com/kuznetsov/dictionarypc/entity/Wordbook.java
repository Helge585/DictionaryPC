package com.kuznetsov.dictionarypc.entity;

public class Wordbook {
    private int id;
    private String name;
    private int wGroupId;

    private int result;
    private String lastDate;

    public Wordbook(int id, String name, int wGroupId, int result, String lastDate) {
        this.id = id;
        this.name = name;
        this.wGroupId = wGroupId;
        this.result = result;
        this.lastDate = lastDate;
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
    public void setName(String name) {
        this.name = name;
    }

    public int getwGroupId() {
        return wGroupId;
    }

    public void setwGroupId(int wGroupId) {
        this.wGroupId = wGroupId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupId=" + wGroupId +
                '}';
    }
}
