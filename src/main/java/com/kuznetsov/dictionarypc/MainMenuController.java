package com.kuznetsov.dictionarypc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainMenuController implements DictGroupCreatingListener{
    @FXML
    public Button saveButton;
    @FXML
    public TextField dictGroupName;

    @FXML
    private Label welcomeText;

    @FXML
    TabPane dictionaryGroups;

    @FXML
    public void initialize() throws SQLException {
        List<DictGroup> dictGroupNames = Repository.getAllGroups();
        for (DictGroup dictGroup : dictGroupNames) {
            FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication
                    .class.getResource("dictionary-group.fxml"));
            try {
                Tab tab = (Tab)fxmlLoader.load();
                DictionaryGroupController controller =
                        (DictionaryGroupController)fxmlLoader.getController();
                controller.setDictionaryGroup(dictGroup);
                Repository.setOnDictCreatingListener(controller);
                dictionaryGroups.getTabs().add(0,tab);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        dictionaryGroups.getStylesheets()
                .add(getClass().getResource("mainTabPaneStyle.css").toExternalForm());
        saveButton.setOnAction(actionEvent -> {
            try {
                Repository.addDictGroup(new DictGroup(-1, dictGroupName.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Repository.setOnDictGroupCreateListener(this);
    }

    @Override
    public void onDictGroupCreate(DictGroup dictGroup) {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication
                .class.getResource("dictionary-group.fxml"));
        try {
            Tab tab = (Tab)fxmlLoader.load();
            DictionaryGroupController controller =
                    (DictionaryGroupController)fxmlLoader.getController();
            controller.setDictionaryGroup(dictGroup);
            dictionaryGroups.getTabs().add(0,tab);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}