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

public class WordOpenController implements WordbookCloseListener {
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;
    @FXML
    public StackPane rootStackPane;
    @FXML
    private TextField tfRussianWord;
    @FXML
    private TextField tfForeignWord;
    @FXML
    private TextArea taRussianExample;
    @FXML
    private TextArea taForeignExample;
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
                word.setRussianWord(tfRussianWord.getText());
                word.setForeignWord(tfForeignWord.getText());
                word.setRussianExample(taRussianExample.getText());
                word.setForeignExample(taForeignExample.getText());
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
        tfRussianWord.setText(word.getRussianWord());
        tfForeignWord.setText(word.getForeignWord());
        taRussianExample.setText(word.getRussianExample());
        taForeignExample.setText(word.getForeignExample());

        taRussianExample.setOnMouseDragged(mouseEvent -> {
            double newWidth = mouseEvent.getX();
            double newHeight = mouseEvent.getY();
            System.out.println(newWidth + " " + newHeight);
            taRussianExample.setPrefSize(newWidth, newHeight);
        });

        deleteButton.setOnAction(actionEvent -> {
            if (DialogsManager.showOkCancelDialog("", "", "Подтвердите удаление")) {
                Repository.deleteWord(word.getId());
                this.itemDeleteListener.onItemDelete(rootStackPane);
            }
        });
    }

    private void setEditableMode(boolean editableMode) {
        tfRussianWord.setEditable(editableMode);
        tfForeignWord.setEditable(editableMode);
        taRussianExample.setEditable(editableMode);
        taForeignExample.setEditable(editableMode);
    }

    @Override
    public void onCloseWordbook() {
        if (isWordEdited) {
            Repository.updateWordValues(word);
        }
    }
}
