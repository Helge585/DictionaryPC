package com.kuznetsov.dictionarypc;

public interface WordCreatingListener {
    int getDictId();
    void onWordCreate(Word word);
}
