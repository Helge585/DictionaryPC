package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.entity.WordbookGroup;
import com.kuznetsov.dictionarypc.listener.ItemDeleteListener;
import com.kuznetsov.dictionarypc.listener.WordbookCreatingListener;
import com.kuznetsov.dictionarypc.utils.DialogsManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

    private WordbookGroup wordbookGroup;
    private ItemDeleteListener itemDeleteListener;

    public void initialize() {
        addWordbookButton.setOnAction(actionEvent -> {
            addWordbook();
        });
        deleteWordbookGroupButton.setOnAction(actionEvent -> {
            if (DialogsManager.showOkCancelDialog("", "", "Подтвердите удаление")) {
                try {
                    Repository.deleteWordbookGroup(wordbookGroup.getId());
                    this.itemDeleteListener.onItemDelete(tab);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setWordbookGroup(WordbookGroup wordbookGroup) {
        this.wordbookGroup = wordbookGroup;
        tab.setText(wordbookGroup.getName());
        try {
            List<Wordbook> wordbooks = Repository.getWordbooksByGroupId(wordbookGroup.getId());
            for (Wordbook wordbook : wordbooks) {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication
                        .class.getResource("/com/kuznetsov/dictionarypc/views/wordbook-preview.fxml"));
                flowPane.getChildren().add(0, fxmlLoader.load());
                WordbookPreviewController controller
                        = (WordbookPreviewController)fxmlLoader.getController();
                //System.out.println(controller + " is null:" + (controller == null));
                controller.setData(wordbook, this);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addWordbook() {
        Wordbook wordbook = new Wordbook(-1, wordbookName.getText(), wordbookGroup.getId(), 0, "");
        try {
            Repository.addWordbook(wordbook);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getWordbookGroupId() {
        return wordbookGroup.getId();
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
