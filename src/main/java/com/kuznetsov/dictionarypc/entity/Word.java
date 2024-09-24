package com.kuznetsov.dictionarypc.entity;

import com.kuznetsov.dictionarypc.utils.TestConfigure;

public class Word {
    private int id;
    private int dictId;
    private String russianWord;
    private String foreignWord;
    private String russianExample;
    private String foreignExample;
    private TestConfigure.WordType wordType;

    public Word(int id, int dictId, String russianWord, String foreignWord,
                String russianExample, String foreignExample, TestConfigure.WordType wordType) {
        this.id = id;
        this.dictId = dictId;
        this.russianWord = russianWord;
        this.foreignWord = foreignWord;
        this.russianExample = russianExample;
        this.foreignExample = foreignExample;
        this.wordType = wordType;
    }

    public String getRussianWord() {
        return russianWord;
    }

    public void setRussianWord(String russianWord) {
        this.russianWord = russianWord;
    }

    public String getForeignWord() {
        return foreignWord;
    }

    public void setForeignWord(String foreignWord) {
        this.foreignWord = foreignWord;
    }

    public String getRussianExample() {
        return russianExample;
    }

    public void setRussianExample(String russianExample) {
        this.russianExample = russianExample;
    }

    public String getForeignExample() {
        return foreignExample;
    }

    public void setForeignExample(String foreignExample) {
        this.foreignExample = foreignExample;
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
                ", first='" + russianWord + '\'' +
                ", second='" + foreignWord + '\'' +
                ", firstExample='" + russianExample + '\'' +
                ", secondExample='" + foreignExample + '\'' +
                ", wordType=" + wordType +
                '}';
    }
}
