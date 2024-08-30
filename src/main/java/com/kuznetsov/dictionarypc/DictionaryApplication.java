package com.kuznetsov.dictionarypc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class DictionaryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Repository.initialize();
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 440);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Repository.close();
    }

    public static void main(String[] args) {
        launch();
    }
}