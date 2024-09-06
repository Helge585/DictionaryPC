package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.utils.WindowsManager;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class WordbookPreviewController {
    @FXML
    public Button testButton;
    @FXML
    public ChoiceBox<String> testType;
    @FXML
    public ChoiceBox<String> wordType;
    @FXML
    private Label nameLabel;
    @FXML
    private Button openButton;
    private Wordbook wordbook;

    public void initialize() {
        ObservableList<String> testTypes =
                FXCollections.<String>observableArrayList("Write first", "Write second");
        testType.setItems(testTypes);
        testType.setValue("Write second");

        ObservableList<String> wordTypes =
                FXCollections.<String>observableArrayList("All words", "Wrong", "New");
        wordType.setItems(wordTypes);
        wordType.setValue("All words");
    }

    public void setWordbook(Wordbook wordbook) {
        this.wordbook = wordbook;
        nameLabel.setText(wordbook.getName());
        openButton.setOnAction(actionEvent -> {
            WindowsManager.showWordbookOpenWindow(this.wordbook);
        });
        testButton.setOnAction(actionEvent -> {
            WindowsManager.showWordbookTestWindow(this.wordbook);
        });
    }
}
