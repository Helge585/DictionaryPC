package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.listener.ItemDeleteListener;
import com.kuznetsov.dictionarypc.listener.WordbookCloseListener;
import com.kuznetsov.dictionarypc.utils.DialogsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;

public class WordOpenController implements WordbookCloseListener {
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;
    @FXML
    public StackPane rootStackPane;
    @FXML
    private TextField first;
    @FXML
    private TextField second;
    @FXML
    private TextArea firstExample;
    @FXML
    private TextArea secondExample;
    private boolean isEditingMode = false;
    private boolean isWordEdited = false;

    private ItemDeleteListener itemDeleteListener;
    private Word word;

    @FXML
    public void initialize() {
        editButton.setOnAction(actionEvent -> {
            if (isEditingMode) {
                editButton.setText("Edit");
                isEditingMode = !isEditingMode;
                setEditableMode(false);
                word.setFirst(first.getText());
                word.setSecond(second.getText());
                word.setFirstExample(firstExample.getText());
                word.setSecondExample(secondExample.getText());
                isWordEdited = true;
            } else {
                editButton.setText("Save");
                isEditingMode = !isEditingMode;
                setEditableMode(true);
            }
        });
    }

    public void setWord(Word word, ItemDeleteListener itemDeleteListener) {
        this.itemDeleteListener = itemDeleteListener;
        this.word = word;
        first.setText(word.getFirst());
        second.setText(word.getSecond());
        firstExample.setText(word.getFirstExample());
        secondExample.setText(word.getSecondExample());

        firstExample.setOnMouseDragged(mouseEvent -> {
            double newWidth = mouseEvent.getX();
            double newHeight = mouseEvent.getY();
            System.out.println(newWidth + " " + newHeight);
            firstExample.setPrefSize(newWidth, newHeight);
        });

        deleteButton.setOnAction(actionEvent -> {
            if (DialogsManager.showOkCancelDialog("", "", "Подтвердите удаление")) {
                Repository.deleteWord(word.getId());
                this.itemDeleteListener.onItemDelete(rootStackPane);
            }
        });
    }

    private void setEditableMode(boolean editableMode) {
        first.setEditable(editableMode);
        second.setEditable(editableMode);
        firstExample.setEditable(editableMode);
        secondExample.setEditable(editableMode);
    }

    @Override
    public void onCloseWordbook() {
        if (isWordEdited) {
            Repository.updateWordValues(word);
        }
    }
}
