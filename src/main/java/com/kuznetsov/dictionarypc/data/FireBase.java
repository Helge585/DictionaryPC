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

    public static void sandbox() {
        addWordbook2();
    }

    public static void addWordbook2() {
        Wordbook wordbook = Repository.selectWordbook(3);
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

    public static void addNewFields() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPatch post = new HttpPatch(url + "Words/WordbookName/words/0/First.json");
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity entity = new StringEntity("{\"FirstExample\": \"dsdd\"}",  "UTF-8");
            post.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveGroups() {
        List<WGroup> wGroups = Repository.selectWordbookGroups();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(WGroup.class, new WordbookGroupSerializer())
                .create();
        String json = gson.toJson(wGroups);
        System.out.println(json);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPatch post = new HttpPatch(url + "root2.json");
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity entity = new StringEntity("{\"groups\":" + json + "}",  "UTF-8");
            post.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addWordbook() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut post = new HttpPut(url + "Wordbooks/newWordbook.json");
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity entity = new StringEntity("{\"w\": \"s\"}", "UTF-8");
            post.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveWordbook2() throws SQLException, UnsupportedEncodingException {
//        WordbookGroup wordbookGroup = new WordbookGroup(1, "group name");
//        Gson gson = new Gson();
//        List<Word> words = Repository.selectWords(3, null);
//        System.out.println(gson.toJson(wordbookGroup));
//        System.out.println(gson.toJson(words));
        WGroup wGroup = Repository.selectWordbookGroup(7);
        String encodedPath = URLEncoder.encode(wGroup.getName(), "UTF-8");
        List<Wordbook> wordbook = Repository.selectWordbooks(7);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Wordbook.class, new WordbookSerializer())
                .create();
        String json = "{\"" + encodedPath + "\":" + gson.toJson(wordbook) + "}";
        System.out.println(json);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPatch post = new HttpPatch(url + "Wordbooks.json");
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity entity = new StringEntity(json, "UTF-8");
            post.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveData() {
        Gson gson = new Gson();
        String json = gson.toJson(new Test());
        //System.out.println(json);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPatch post = new HttpPatch(url + "root2.json");
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity entity = new StringEntity(json,  "UTF-8");
            post.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveWordbook(Wordbook wordbook) throws SQLException {
        WGroup wGroup = Repository.selectWordbookGroup(wordbook.getwGroupId());
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String encodedPath = URLEncoder.encode(wGroup.getName(), "UTF-8");
            String encodedPath2 = URLEncoder.encode(wordbook.getName(), "UTF-8");
            HttpPatch post = new HttpPatch(url + "root/" + encodedPath
                    + "/" + encodedPath2 + ".json");
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            List<Word> words = Repository.selectWords(wordbook.getId(), null);
            StringBuffer json = new StringBuffer("{ ");
            for (Word word : words) {
                json.append(" \"").append(word.getRussianWord()).append("\" : { ");
                json.append("\"First\":").append("\"").append(word.getRussianWord()).append("\",");
                json.append("\"Second\":").append("\"").append(word.getForeignWord()).append("\",");
                json.append("\"FirstExample\":").append("\"").append(word.getRussianExample()).append("\",");
                json.append("\"SecondExample\":").append("\"").append(word.getForeignExample()).append("\"");
                json.append(" },");

            }
            json.delete(json.length() - 1, json.length());
            json.append(" }");
            StringEntity entity = new StringEntity(json.toString(),  "UTF-8");
            post.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
