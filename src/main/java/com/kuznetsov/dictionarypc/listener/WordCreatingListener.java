package com.kuznetsov.dictionarypc.listener;

import com.kuznetsov.dictionarypc.entity.Word;

public interface WordCreatingListener {
    int getWordbookId();
    void onWordCreate(Word word);
}
