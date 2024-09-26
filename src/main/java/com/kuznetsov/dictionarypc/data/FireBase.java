package com.kuznetsov.dictionarypc.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kuznetsov.dictionarypc.entity.Test;
import com.kuznetsov.dictionarypc.entity.WGroup;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class FireBase {
    private static final String url;

    static {
        Properties properties = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("firebase.properties"))) {
            properties.load(in);
        } catch (Exception e) {
            System.out.println(e);
        }
        url = properties.getProperty("url");
    }

    public static void main(String[] args) throws SQLException, IOException {
        Repository.initialize();
        sandbox();
        Repository.close();
    }

    private static void sandbox() {
        //addWordbook();
    }

    public static void saveWordbook(int wordbookId) {
        Wordbook wordbook = Repository.selectWordbook(wordbookId);
        WGroup wGroup = Repository.selectWordbookGroup(wordbook.getwGroupId());

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPatch patch = new HttpPatch(url + "WordbookGroups/" + wGroup.getId() + ".json");
            patch.setHeader("Content-Type", "application/json; charset=UTF-8");

            Gson gson = new Gson();
            String json = gson.toJson(wGroup);
            StringEntity entity = new StringEntity(json,  "UTF-8");
            patch.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(patch)) {
                System.out.println("Data added: " + response.getStatusLine());
            }

            patch = new HttpPatch(url + "Wordbooks/" + wGroup.getId() + "/" + wordbook.getId() + ".json");
            patch.setHeader("Content-Type", "application/json; charset=UTF-8");
            json = gson.toJson(wordbook);
            entity = new StringEntity(json, "UTF-8");
            patch.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(patch)) {
                System.out.println("Data added: " + response.getStatusLine());
            }

            patch = new HttpPatch(url + "Words/" + wordbook.getId() + ".json");
            patch.setHeader("Content-Type", "application/json; charset=UTF-8");
            StringBuilder jsonSb = new StringBuilder("{");
            List<Word> words = Repository.selectWords(wordbook.getId(), null);
            for (Word word : words) {
                jsonSb.append("\"" + word.getId() + "\":");
                jsonSb.append(gson.toJson(word));
                jsonSb.append(",");
            }
            jsonSb.deleteCharAt(jsonSb.length() - 1);
            jsonSb.append("}");

            entity = new StringEntity(jsonSb.toString(), "UTF-8");
            patch.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(patch)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
