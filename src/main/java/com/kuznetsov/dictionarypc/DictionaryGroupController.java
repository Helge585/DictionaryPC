package com.kuznetsov.dictionarypc;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class DictionaryGroupController {
    @FXML
    public FlowPane flowPane;
//    @FXML
//    public FlowPane settings;
    @FXML
    public Button addDictButton;
    @FXML
    public TextField dictName;
    @FXML
    public Button deleteGroupButton;
    @FXML
    private Tab tab;

    public void setText(String tabText, String labeltext) {
        tab.setText(tabText);
    }

    public void initialize() {

    }
}
