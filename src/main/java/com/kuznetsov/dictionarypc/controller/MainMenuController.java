package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.WGroup;
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
    TabPane wGroups;

    @FXML
    public void initialize() throws SQLException {
        List<WGroup> wGroupNames = Repository.selectWordbookGroups();
        for (WGroup wGroup : wGroupNames) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                    .class.getResource("/com/kuznetsov/dictionarypc/views/wordbook-group.fxml"));
            try {
                Tab tab = (Tab)fxmlLoader.load();
                WordbookGroupController controller =
                        (WordbookGroupController)fxmlLoader.getController();
                controller.setWordbookGroup(wGroup);
                controller.setItemDeleteListener(this);
                Repository.setOnWordbookCreatingListener(controller);
                wGroups.getTabs().add(0,tab);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        if (wGroups.getTabs().size() > 0) {
            wGroups.getSelectionModel().select(0);
        }
        wGroups.getStylesheets()
                .add(getClass().getResource("/com/kuznetsov/dictionarypc/mainTabPaneStyle.css").toExternalForm());
        saveButton.setOnAction(actionEvent -> {
            Repository.createWordbookGroup(new WGroup(-1, wordbookGroupName.getText()));
        });
        Repository.setOnWordbookGroupCreateListener(this);
    }

    @Override
    public void onWordbookGroupCreate(WGroup wGroup) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                .class.getResource("/com/kuznetsov/dictionarypc/views/wordbook-group.fxml"));
        try {
            Tab tab = (Tab)fxmlLoader.load();
            WordbookGroupController controller =
                    (WordbookGroupController)fxmlLoader.getController();
            controller.setWordbookGroup(wGroup);
            controller.setItemDeleteListener(this);
            wGroups.getTabs().add(0,tab);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void onItemDelete(Object obj) {
        wGroups.getTabs().remove(obj);
    }
}