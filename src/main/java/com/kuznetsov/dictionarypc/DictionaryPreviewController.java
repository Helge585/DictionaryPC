package com.kuznetsov.dictionarypc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DictionaryPreviewController {
    @FXML
    private Label nameLabel;
    @FXML
    private Button openButton;
    private Dict dict;

    public void initialize() {

    }

    public void setDict(Dict dict) {
        this.dict = dict;
        nameLabel.setText(dict.getName());
    }
}
