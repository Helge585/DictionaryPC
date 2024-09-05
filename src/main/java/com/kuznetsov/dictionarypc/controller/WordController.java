package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.entity.Word;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WordController {
    @FXML
    private TextField first;
    @FXML
    private TextField second;
    @FXML
    private TextArea firstExample;
    @FXML
    private TextArea secondExample;

    private Word word;

    @FXML
    public void initialize() {

    }

    public void setWord(Word word) {
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
    }
}
