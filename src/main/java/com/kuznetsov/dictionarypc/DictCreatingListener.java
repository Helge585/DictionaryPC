package com.kuznetsov.dictionarypc;

public interface DictCreatingListener {

    int getDictGroupId();
    void onDictCreate(Dict dict);
}
