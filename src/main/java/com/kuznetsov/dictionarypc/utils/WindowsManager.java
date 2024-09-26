package com.kuznetsov.dictionarypc.utils;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.controller.WordbookOpenController;
import com.kuznetsov.dictionarypc.controller.WordbookTestController;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.listener.WordbookCloseListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowsManager {
    private WindowsManager() {}

    public static void showWordbookTestWindow(Wordbook wordbook, TestConfigure.TestType testType,
                                              TestConfigure.WordType wordType, WordbookCloseListener listener) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                .class.getResource(ResourcesManager.getWordbookTestFxmlPath()));
        try {
            StackPane stackPane= (StackPane)fxmlLoader.load();
            WordbookTestController controller =
                    (WordbookTestController)fxmlLoader.getController();
            controller.setData(wordbook, testType, wordType);
            Scene scene = new Scene(stackPane, 1200, 800);

            Stage window = new Stage();
            window.setScene(scene);
            window.setOnCloseRequest(windowEvent -> {
                controller.onCloseWordbook();
                listener.onCloseWordbook();
            });
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showWordbookOpenWindow(Wordbook wordbook, TestConfigure.WordType wordType,
                                              WordbookCloseListener wordbookCloseListener) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                .class.getResource(ResourcesManager.getWordbookOpenFxmlPath()));
        try {
            StackPane flowPane = (StackPane)fxmlLoader.load();
            Scene scene = new Scene(flowPane, 900, 800);

            Stage window = new Stage();
            window.setScene(scene);

            WordbookOpenController controller =
                    (WordbookOpenController)fxmlLoader.getController();
            controller.setData(wordbook, wordType, window);

            window.setOnCloseRequest(windowEvent -> {
                wordbookCloseListener.onCloseWordbook();
                controller.onCloseWordbook();
            });
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
