package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.WGroup;
import com.kuznetsov.dictionarypc.listener.WordbookCreatingListener;
import com.kuznetsov.dictionarypc.listener.WordbookGroupCreatingListener;
import com.kuznetsov.dictionarypc.utils.DialogsManager;
import com.kuznetsov.dictionarypc.utils.WindowsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainMenuController_New implements WordbookGroupCreatingListener {
    @FXML
    public ListView<WGroup> groupsList;
    @FXML
    public MenuItem addGroupButton;
    @FXML
    public MenuItem deleteGroupButton;
    @FXML
    public void initialize() {
        initializeWidgets();
        Repository.setOnWordbookGroupCreateListener(this);
    }

    @Override
    public void onWordbookGroupCreate(WGroup wGroup) {
        updateWordbookGroups();
    }
    private void initializeWidgets() {
        updateWordbookGroups();

        addGroupButton.setOnAction(event -> {
            WindowsManager.showWordbookCreateWindow();
        });

        deleteGroupButton.setOnAction(event -> {
            deleteSelectedWordbookGroup();
        });
    }
    private void deleteSelectedWordbookGroup() {
        boolean isDeletingConfirmed = DialogsManager.showOkCancelDialog(
            "", "", "Удалить выбранные группы?"
        );

        if (!isDeletingConfirmed) {
            return;
        }

        ObservableList<WGroup> selectedItems = groupsList.getSelectionModel().getSelectedItems();
        for (WGroup selectedItem: selectedItems) {
            Repository.deleteWordbookGroup(selectedItem.getId());
        }
        updateWordbookGroups();
    }
    private void updateWordbookGroups() {

        List<WGroup> groups = Repository.selectWordbookGroups();

        if (groups == null) {
            return;
        }

        ObservableList<WGroup> observableGroups = FXCollections.observableArrayList();
        observableGroups.addAll(groups);

        groupsList.setItems(observableGroups);
    }
}
