package com.kuznetsov.dictionarypc.unused;

import com.google.gson.*;
import com.kuznetsov.dictionarypc.entity.Word;

import java.lang.reflect.Type;

public class WordSerializer implements JsonSerializer<Word> {
    @Override
    public JsonElement serialize(Word src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("first", src.getRussianWord());
        jsonObject.addProperty("second", src.getForeignWord());
        jsonObject.addProperty("firstExample", src.getRussianExample());
        jsonObject.addProperty("secondExample", src.getForeignExample());
        return jsonObject;
    }
}
