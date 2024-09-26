package com.kuznetsov.dictionarypc.unused;

import com.google.gson.*;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.entity.Wordbook;

import java.lang.reflect.Type;

public class WordbookSerializer implements JsonSerializer<Wordbook> {
    @Override
    public JsonElement serialize(Wordbook src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.getName());
        return jsonObject;
    }
}
