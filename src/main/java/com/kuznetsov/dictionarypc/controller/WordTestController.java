package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.listener.AnswerListener;
import com.kuznetsov.dictionarypc.listener.WordbookCloseListener;
import com.kuznetsov.dictionarypc.utils.TestConfigure;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class WordTestController implements WordbookCloseListener {
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
    @FXML
    public StackPane rootStackPane;
    @FXML
    public GridPane gridPane;

    private Word word;
    private AnswerListener answerListener;

    @FXML
    public void initialize() {

    }

    public void setData(Word word, TestConfigure.TestType testType) {
        this.word = word;

        firstExample.setText(word.getRussianExample());
        secondExample.setText(word.getForeignExample());

        configureTest(testType);
    }

    private void configureTest(TestConfigure.TestType testType) {
        if (testType == TestConfigure.TestType.WriteFirst) {
            setAnswerStyle(first, word.getRussianWord());
            setQuestionStyle(second, word.getForeignWord());
        } else if (testType == TestConfigure.TestType.WriteSecond) {
            setAnswerStyle(second, word.getForeignWord());
            setQuestionStyle(first, word.getRussianWord());
        }
        setFocusMode(testType);
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
    }

    private void setAnswerStyle(TextField textField, String answerWord) {
        textField.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;");
        textField.setText("");
        textField.setOnAction(actionEvent -> {
            if (textField.getText().toLowerCase().replace(" ", "").equals(
                    answerWord.toLowerCase().replace(" ", ""))) {
                textField.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;-fx-border-color: green;");
                answerListener.onAnswer(true);
                textField.setEditable(false);
                word.setWordType(TestConfigure.WordType.Right);
            } else {
                textField.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;-fx-border-color: red");
                answerListener.onAnswer(false);
                word.setWordType(TestConfigure.WordType.Wrong);
            }
        });
        showAnswer.setOnAction(actionEvent -> {
            if (textField.isEditable()) {
                textField.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;-fx-border-color: red;");
                textField.setText(answerWord);
                textField.setEditable(false);
                word.setWordType(TestConfigure.WordType.Wrong);
            }
        });
    }

    private void setQuestionStyle(TextField textField, String questionWord) {
        textField.setStyle("-fx-font: 12pt serif;-fx-font-weight: 600;");
        textField.setText(questionWord);
        textField.setEditable(false);
    }

    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }

    @Override
    public void onCloseWordbook() {
        Repository.updateWordType(word.getId(), word.getWordType());
    }

    private void setFocusMode(TestConfigure.TestType testType) {
        rootStackPane.setFocusTraversable(false);
        gridPane.setFocusTraversable(false);
        showAnswer.setFocusTraversable(false);
        showFirstExample.setFocusTraversable(false);
        showSecondExample.setFocusTraversable(false);
        if (testType == TestConfigure.TestType.WriteFirst) {
            first.setFocusTraversable(true);
            second.setFocusTraversable(false);
        } else {
            //System.out.println("Second is editable");
            first.setFocusTraversable(false);
            second.setFocusTraversable(true);
        }
    }
}
