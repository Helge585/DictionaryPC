package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.EntitiesManager;
import com.kuznetsov.dictionarypc.entity.WGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class WordbookGroupCreateController_New {
    @FXML
    Button btnCreate;
    @FXML
    TextField tfGroupName;
    @FXML
    public void initialize() {
        initializeWidgets();
    }
    private void initializeWidgets() {
        btnCreate.setOnAction(event -> {
            if (isDataCorrectForCreatingWordbookGroup()) {
                Repository.createWordbookGroup(getWordbookGroupForSaving());
            }
        });
    }
    private boolean isDataCorrectForCreatingWordbookGroup() {
        return !tfGroupName.getText().isEmpty();
    }
    private WGroup getWordbookGroupForSaving() {
        int id = EntitiesManager.newEntityId();
        String name = tfGroupName.getText().strip();

        return new WGroup(id, name);
    }
}
