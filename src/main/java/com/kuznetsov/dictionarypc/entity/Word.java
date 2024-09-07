package com.kuznetsov.dictionarypc.entity;

import com.kuznetsov.dictionarypc.utils.TestConfigure;

public class Word {
    private int id;
    private int dictId;
    private String first;
    private String second;
    private String firstExample;
    private String secondExample;
    private TestConfigure.WordType wordType;

    public Word(int id, int dictId, String first, String second,
                String firstExample, String secondExample, TestConfigure.WordType wordType) {
        this.id = id;
        this.dictId = dictId;
        this.first = first;
        this.second = second;
        this.firstExample = firstExample;
        this.secondExample = secondExample;
        this.wordType = wordType;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getFirstExample() {
        return firstExample;
    }

    public void setFirstExample(String firstExample) {
        this.firstExample = firstExample;
    }

    public String getSecondExample() {
        return secondExample;
    }

    public void setSecondExample(String secondExample) {
        this.secondExample = secondExample;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDictId() {
        return dictId;
    }

    public void setDictId(int dictId) {
        this.dictId = dictId;
    }

    public TestConfigure.WordType getWordType() {
        return wordType;
    }

    public void setWordType(TestConfigure.WordType wordType) {
        this.wordType = wordType;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", dictId=" + dictId +
                ", first='" + first + '\'' +
                ", second='" + second + '\'' +
                ", firstExample='" + firstExample + '\'' +
                ", secondExample='" + secondExample + '\'' +
                ", wordType=" + wordType +
                '}';
    }
}
