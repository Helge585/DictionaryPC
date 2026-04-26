package com.kuznetsov.dictionarypc.data;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

public class YandexCloud {

    private static final String OAuthToken;
    private static final String testPath = "/Dictionary/test.txt";

    static {
        Properties properties = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("yandexCloud.properties"))) {
            properties.load(in);
        } catch (Exception e) {
            System.out.println(e);
        }
        OAuthToken = properties.getProperty("OAuthToken");
    }

    public static void main(String[] args) {
        sandbox();
    }

    private static void sandbox() {
//        sandboxGetFile();
        sandboxSaveFile();
    }

    private static void sandboxSaveFile() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(
                    "https://cloud-api.yandex.net/v1/disk/resources/upload?path=" + testPath + "&overwrite=true"
            );
            getRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
            getRequest.setHeader("Authorization", "OAuth " + OAuthToken);

            try (CloseableHttpResponse response = client.execute(getRequest)) {
                System.out.println("Result: " + response.getStatusLine());

                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    String jsonResponse = new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8);

                    String uploadUrl = extractHrefFromJson(jsonResponse);
                    System.out.println("Upload URL: " + uploadUrl);

                    String fileContent = "It's working!!! Это работает!!!";

                    byte[] contentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

                    HttpPut putRequest = new HttpPut(uploadUrl);
                    putRequest.setHeader("Content-Type", "text/plain; charset=UTF-8");
//                    putRequest.setHeader("Content-Length", String.valueOf(contentBytes.length));
                    putRequest.setEntity(new ByteArrayEntity(contentBytes, ContentType.TEXT_PLAIN));

                    try (CloseableHttpResponse putResponse = client.execute(putRequest)) {
                        System.out.println("!!!!");
                        int statusCode = putResponse.getStatusLine().getStatusCode();
                        if (statusCode == 201 || statusCode == 200) {
                            System.out.println(statusCode + ": File was saved");
                        } else {
                            System.err.println(statusCode + ": Error. File wasn't saved");
                        }
                    }
                } else {
                    System.out.println("Failed to get save link: " + response.getStatusLine());
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void sandboxGetFile() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(
                    "https://cloud-api.yandex.net/v1/disk/resources/download?path=" + testPath
            );
            getRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
            getRequest.setHeader("Authorization", "OAuth " + OAuthToken);

            try (CloseableHttpResponse response = client.execute(getRequest)) {
                System.out.println("Result: " + response.getStatusLine());
//                HttpEntity entity = response.getEntity();

//                System.out.println(new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8));

                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    String jsonResponse = new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8);

                    String downloadUrl = extractHrefFromJson(jsonResponse);
                    System.out.println(downloadUrl);

                    HttpGet downloadRequest = new HttpGet(downloadUrl);

                    try (CloseableHttpResponse downloadResponse = client.execute(downloadRequest)){
                        if (downloadResponse.getStatusLine().getStatusCode() == 200) {
                            HttpEntity downloadEntity = downloadResponse.getEntity();
                            String fileContent = new String(
                                    downloadEntity.getContent().readAllBytes(),
                                    StandardCharsets.UTF_8
                            );
                            System.out.println("fileContent:\n" + fileContent);
                        } else {
                            System.err.println("Failed to download file: " + downloadResponse.getStatusLine());
                        }
                    }
                } else {
                    System.out.println("Failed to get download link: " + response.getStatusLine());
                }
            }

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static String extractHrefFromJson(String json) {
        //TODO: использовать Jackson или Gson
        //В документации API описаны класы Link и Link-upload

        String pattern = "\"href\":\"";

        int start = json.indexOf(pattern);
        if (start == -1) {
            throw new RuntimeException("No 'href' field");
        }
        start += pattern.length();

        int end = json.indexOf("\"", start);
        if (end == -1) {
            throw new RuntimeException("Incorrect 'href' field");
        }

        return json.substring(start, end);
    }
}
