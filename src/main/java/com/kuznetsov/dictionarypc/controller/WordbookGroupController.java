package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.entity.WGroup;
import com.kuznetsov.dictionarypc.listener.ItemDeleteListener;
import com.kuznetsov.dictionarypc.listener.WordbookCreatingListener;
import com.kuznetsov.dictionarypc.utils.DialogsManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.List;

public class WordbookGroupController implements WordbookCreatingListener, ItemDeleteListener {
    @FXML
    public FlowPane flowPane;
    @FXML
    public Button addWordbookButton;
    @FXML
    public TextField wordbookName;
    @FXML
    public Button deleteWordbookGroupButton;
    @FXML
    private Tab tab;

    private WGroup wGroup;
    private ItemDeleteListener itemDeleteListener;

    public void initialize() {
        addWordbookButton.setOnAction(actionEvent -> {
            addWordbook();
        });
        deleteWordbookGroupButton.setOnAction(actionEvent -> {
            if (DialogsManager.showOkCancelDialog("", "", "Подтвердите удаление")) {
                Repository.deleteWordbookGroup(wGroup.getId());
                this.itemDeleteListener.onItemDelete(tab);
            }
        });
    }

    public void setWordbookGroup(WGroup wGroup) {
        this.wGroup = wGroup;
        tab.setText(wGroup.getName());
        try {
            List<Wordbook> wordbooks = Repository.selectWordbooks(wGroup.getId());
            for (Wordbook wordbook : wordbooks) {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                        .class.getResource("/com/kuznetsov/dictionarypc/views/wordbook-preview.fxml"));
                flowPane.getChildren().add(0, fxmlLoader.load());
                WordbookPreviewController controller
                        = (WordbookPreviewController)fxmlLoader.getController();
                //System.out.println(controller + " is null:" + (controller == null));
                controller.setData(wordbook, this);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addWordbook() {
        Wordbook wordbook = new Wordbook(-1, wordbookName.getText(), wGroup.getId(), 0, "");
        Repository.createWordbook(wordbook);
    }

    @Override
    public int getWordbookGroupId() {
        return wGroup.getId();
    }

    @Override
    public void onWordbookCreate(Wordbook wordbook) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                .class.getResource("/com/kuznetsov/dictionarypc/views/wordbook-preview.fxml"));
        try {
            flowPane.getChildren().add(0, fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        WordbookPreviewController controller
                = (WordbookPreviewController)fxmlLoader.getController();
        controller.setData(wordbook, this);
    }

    public void setItemDeleteListener(ItemDeleteListener itemDeleteListener) {
        this.itemDeleteListener = itemDeleteListener;
    }

    @Override
    public void onItemDelete(Object obj) {
        flowPane.getChildren().remove(obj);
    }
}
