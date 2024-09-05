package com.kuznetsov.dictionarypc.utils;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.controller.WordbookOpenController;
import com.kuznetsov.dictionarypc.controller.WordbookTestController;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowsManager {
    private WindowsManager() {}

    public static void showWordbookTestWindow(Wordbook wordbook) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                .class.getResource(ResourcesManager.getWordbookTestFxmlPath()));
        try {
            StackPane stackPane= (StackPane)fxmlLoader.load();
            WordbookTestController controller =
                    (WordbookTestController)fxmlLoader.getController();
            controller.setData(wordbook);
            Scene scene = new Scene(stackPane, 1000, 800);

            Stage window = new Stage();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showWordbookOpenWindow(Wordbook wordbook) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                .class.getResource(ResourcesManager.getWordbookOpenFxmlPath()));
        try {
            StackPane flowPane = (StackPane)fxmlLoader.load();
            //flowPane.setStyle("-fx-padding: 20;");
            WordbookOpenController controller =
                    (WordbookOpenController)fxmlLoader.getController();
            controller.setWordbook(wordbook);
            //flowPane.setPadding(new Insets(10, 10, 10,10));
            Scene scene = new Scene(flowPane, 900, 800);

            Stage window = new Stage();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
