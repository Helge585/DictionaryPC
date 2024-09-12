package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.WordbookGroup;
import com.kuznetsov.dictionarypc.listener.ItemDeleteListener;
import com.kuznetsov.dictionarypc.listener.WordbookGroupCreatingListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainMenuController implements WordbookGroupCreatingListener, ItemDeleteListener {
    @FXML
    public Button saveButton;
    @FXML
    public TextField wordbookGroupName;

    @FXML
    private Label welcomeText;

    @FXML
    TabPane wordbookGroups;

    @FXML
    public void initialize() throws SQLException {
        List<WordbookGroup> wordbookGroupNames = Repository.selectWordbookGroups();
        for (WordbookGroup wordbookGroup : wordbookGroupNames) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                    .class.getResource("/com/kuznetsov/dictionarypc/views/wordbook-group.fxml"));
            try {
                Tab tab = (Tab)fxmlLoader.load();
                WordbookGroupController controller =
                        (WordbookGroupController)fxmlLoader.getController();
                controller.setWordbookGroup(wordbookGroup);
                controller.setItemDeleteListener(this);
                Repository.setOnWordbookCreatingListener(controller);
                wordbookGroups.getTabs().add(0,tab);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        if (wordbookGroups.getTabs().size() > 0) {
            wordbookGroups.getSelectionModel().select(0);
        }
        wordbookGroups.getStylesheets()
                .add(getClass().getResource("/com/kuznetsov/dictionarypc/mainTabPaneStyle.css").toExternalForm());
        saveButton.setOnAction(actionEvent -> {
            Repository.createWordbookGroup(new WordbookGroup(-1, wordbookGroupName.getText()));
        });
        Repository.setOnWordbookGroupCreateListener(this);
    }

    @Override
    public void onWordbookGroupCreate(WordbookGroup wordbookGroup) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                .class.getResource("/com/kuznetsov/dictionarypc/views/wordbook-group.fxml"));
        try {
            Tab tab = (Tab)fxmlLoader.load();
            WordbookGroupController controller =
                    (WordbookGroupController)fxmlLoader.getController();
            controller.setWordbookGroup(wordbookGroup);
            controller.setItemDeleteListener(this);
            wordbookGroups.getTabs().add(0,tab);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void onItemDelete(Object obj) {
        wordbookGroups.getTabs().remove(obj);
    }
}