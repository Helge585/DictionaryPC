package com.kuznetsov.dictionarypc.listener;

import com.kuznetsov.dictionarypc.entity.Wordbook;

public interface WordbookCreatingListener {

    int getWordbookGroupId();
    void onWordbookCreate(Wordbook wordbook);
}
