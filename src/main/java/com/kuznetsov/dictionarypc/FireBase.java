package com.kuznetsov.dictionarypc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.entity.WordbookGroup;
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
//        Repository.initialize();
//        Properties properties = new Properties();
//        try(InputStream in = Files.newInputStream(Paths.get("firebase.properties"))) {
//            properties.load(in);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        url = properties.getProperty("url");
//        try {
//            addWordbookGroup("First");
//            addWordbook("First", "FirstWordbook");
//            addWordbook("First", "ThirdWordbook");
//            addWordbooksFromDB();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        Repository.close();
    }

    public static void saveWordbook(Wordbook wordbook) throws SQLException {
        WordbookGroup wordbookGroup = Repository.getWordbookGroupById(wordbook.getGroupId());
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String encodedPath = URLEncoder.encode(wordbookGroup.getName(), "UTF-8");
            String encodedPath2 = URLEncoder.encode(wordbook.getName(), "UTF-8");
            HttpPatch post = new HttpPatch(url + "root/" + encodedPath
                    + "/" + encodedPath2 + ".json");
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            List<Word> words = Repository.getWordsByWordbookId(wordbook.getId());
            StringBuffer json = new StringBuffer("{ ");
            for (Word word : words) {
                json.append(" \"").append(word.getFirst()).append("\" : { ");
                json.append("\"First\":").append("\"").append(word.getFirst()).append("\",");
                json.append("\"Second\":").append("\"").append(word.getSecond()).append("\",");
                json.append("\"FirstExample\":").append("\"").append(word.getFirstExample()).append("\",");
                json.append("\"SecondExample\":").append("\"").append(word.getSecondExample()).append("\"");
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

    private static void addWordbooksFromDB() throws SQLException, IOException {
        List<WordbookGroup> wordbookGroups = Repository.getAllWordbookGroups();
        for (WordbookGroup wordbookGroup : wordbookGroups) {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPatch post = new HttpPatch(url + "root.json");
                post.setHeader("Content-Type", "application/json; charset=UTF-8");

                StringEntity entity = new StringEntity(
                        "{\"" + wordbookGroup.getName() + "\": " +
                                "{} }", "UTF-8");
                post.setEntity(entity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    System.out.println("Data added: " + response.getStatusLine());
                }
            }
        }
    }

    private static void addWordbookGroup(String wordbook) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPatch post = new HttpPatch(url);
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity entity = new StringEntity(
                    "{\"" + wordbook + "\": { \"null\" : \"null\"} }", "UTF-8");
            post.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        }
    }
    private static void addWordbook(String wordbookGroupName, String wordbookName) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPatch post = new HttpPatch(url + "WordbookGroup/" + wordbookGroupName + ".json");
            post.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity entity = new StringEntity(
                    "{\"" + wordbookName + "\": { } }", "UTF-8");
            post.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Data added: " + response.getStatusLine());
            }
        }
    }

}
