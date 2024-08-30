package com.kuznetsov.dictionarypc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainMenuController {
    @FXML
    public Button saveButton;
    @FXML
    public TextField groupName;

    @FXML
    private Label welcomeText;

    @FXML
    TabPane dictionaryGroups;

    @FXML
    public void initialize() {
        List<String> groupNames = Data.readDictionaryGroups();
        for (String groupName : groupNames) {
            FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication
                    .class.getResource("dictionary-group.fxml"));
            try {
                Tab tab = (Tab)fxmlLoader.load();
                DictionaryGroupController controller =
                        (DictionaryGroupController)fxmlLoader.getController();
                controller.setText(groupName, groupName.toUpperCase() );
                dictionaryGroups.getTabs().add(0,tab);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        dictionaryGroups.getStylesheets()
                .add(getClass().getResource("mainTabPaneStyle.css").toExternalForm());
        saveButton.setOnAction(actionEvent -> {
            Data.saveDictionaryGroup(groupName.getText());
        });
    }
}