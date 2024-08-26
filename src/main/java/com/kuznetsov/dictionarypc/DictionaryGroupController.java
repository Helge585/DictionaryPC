package com.kuznetsov.dictionarypc;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class DictionaryGroupController {
    @FXML
    public FlowPane flowPane;
    @FXML
    private Tab tab;
    @FXML
    private Label label;

    public void setText(String tabText, String labeltext) {
        tab.setText(tabText);
        label.setText(labeltext);
    }

    public void initialize() {
        flowPane.setBackground(Background.fill(Color.rgb(176, 196, 222)));
        tab.setOnCloseRequest(Event::consume);
    }
}
