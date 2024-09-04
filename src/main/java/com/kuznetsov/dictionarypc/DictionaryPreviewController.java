package com.kuznetsov.dictionarypc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DictionaryPreviewController {
    @FXML
    private Label nameLabel;
    @FXML
    private Button openButton;
    private Dict dict;

    public void initialize() {

    }

    public void setDict(Dict dict) {
        this.dict = dict;
        nameLabel.setText(dict.getName());
        openButton.setOnAction(actionEvent -> {
            openDictionary();
        });
    }

    private void openDictionary() {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication
                .class.getResource("dictionary-open.fxml"));
        try {
            StackPane flowPane = (StackPane)fxmlLoader.load();
            //flowPane.setStyle("-fx-padding: 20;");
            DictionaryOpenController controller =
                    (DictionaryOpenController)fxmlLoader.getController();
            controller.setDict(dict);
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
