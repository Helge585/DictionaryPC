package com.kuznetsov.dictionarypc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    Map<String, String> tabsContent = Map.of("First", "First data",
            "Second", "Second data", "Test", "GGGGGG" );

    @FXML
    private Label welcomeText;

    @FXML
    TabPane dictionaryGroups;

    @FXML
    public void initialize() {
        for (Map.Entry<String, String> entry : tabsContent.entrySet()) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication
                    .class.getResource("dictionary-group.fxml"));
            try {
                Tab tab = (Tab)fxmlLoader.load();
                DictionaryGroupController controller =
                        (DictionaryGroupController)fxmlLoader.getController();
                controller.setText(entry.getKey(), entry.getValue());
                dictionaryGroups.getTabs().add(tab);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        dictionaryGroups.getStylesheets()
                .add(getClass().getResource("mainTabPaneStyle.css").toExternalForm());

    }
}