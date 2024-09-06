package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.listener.AnswerListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WordTestController {
    @FXML
    public TextField first;
    @FXML
    public TextField second;
    @FXML
    public TextArea firstExample;
    @FXML
    public TextArea secondExample;
    @FXML
    public Button showFirstExample;
    @FXML
    public Button showSecondExample;
    @FXML
    public Button showAnswer;

    private Word word;
    private AnswerListener answerListener;

    @FXML
    public void initialize() {

    }

    public void setData(Word word) {
        this.word = word;

        first.setText(word.getFirst());
        firstExample.setText(word.getFirstExample());
        secondExample.setText(word.getSecondExample());

        first.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;");
        second.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;");

        showFirstExample.setOnAction(actionEvent -> {
            if (firstExample.isVisible()) {
                showFirstExample.setText("Show First Example");
                firstExample.setVisible(false);
            } else {
                showFirstExample.setText("Hide First Example");
                firstExample.setVisible(true);
            }
        });
        showSecondExample.setOnAction(actionEvent -> {
            if (secondExample.isVisible()) {
                showSecondExample.setText("Show Second Example");
                secondExample.setVisible(false);
            } else {
                showSecondExample.setText("Hide Second Example");
                secondExample.setVisible(true);
            }
         });
        showAnswer.setOnAction(actionEvent -> {
            if (second.isEditable()) {
                second.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;-fx-border-color: red;");
                second.setText(word.getSecond());
                second.setEditable(false);
            }
        });
        second.setOnAction(actionEvent -> {
            if (second.getText().toLowerCase().replace(" ", "").equals(
                    word.getSecond().toLowerCase().replace(" ", ""))) {
                second.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;-fx-border-color: green;");
                answerListener.onAnswer(true);
                second.setEditable(false);
            } else {
                second.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;-fx-border-color: red");
                answerListener.onAnswer(false);
            }
        });
    }

    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }
}
