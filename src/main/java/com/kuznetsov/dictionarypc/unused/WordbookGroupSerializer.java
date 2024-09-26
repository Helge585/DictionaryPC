package com.kuznetsov.dictionarypc.unused;

import com.google.gson.*;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.WGroup;
import com.kuznetsov.dictionarypc.entity.Wordbook;

import java.lang.reflect.Type;

public class WordbookGroupSerializer implements JsonSerializer<WGroup> {
    @Override
    public JsonElement serialize(WGroup src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.getName());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Wordbook.class, new WordbookSerializer())
                .create();
        String jsonWordbooks = gson.toJson(Repository.selectWordbooks(src.getId()));
        jsonObject.addProperty("wordbooks", jsonWordbooks);
        //jsonObject.addProperty("wordbooks", "{}");
        return jsonObject;
    }
}
