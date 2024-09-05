package com.kuznetsov.dictionarypc;

import com.kuznetsov.dictionarypc.data.Repository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Repository.initialize();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(
                "/com/kuznetsov/dictionarypc/views/main-menu.fxml"));
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