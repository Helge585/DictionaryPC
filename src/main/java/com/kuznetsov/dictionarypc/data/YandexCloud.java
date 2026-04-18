package com.kuznetsov.dictionarypc.data;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
        sandboxGetFile();

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
                HttpEntity entity = response.getEntity();

                System.out.println(new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8));
            }

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
