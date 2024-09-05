package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.utils.WindowsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class WordbookPreviewController {
    @FXML
    public Button testButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Button openButton;
    private Wordbook wordbook;

    public void initialize() {

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
