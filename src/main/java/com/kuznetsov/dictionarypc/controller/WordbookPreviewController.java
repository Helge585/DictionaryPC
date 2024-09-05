package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.controller.WordbookOpenController;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WordbookPreviewController {
    @FXML
    private Label nameLabel;
    @FXML
    private Button openButton;
    private Wordbook wordbook;

    public void initialize() {

    }

    public void setWordbook(Wordbook wordbook) {
        this.wordbook = wordbook;
        nameLabel.setText(wordbook.getName());
        openButton.setOnAction(actionEvent -> {
            openWordbook();
        });
    }

    private void openWordbook() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                .class.getResource("/com/kuznetsov/dictionarypc/views/wordbook-open.fxml"));
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
