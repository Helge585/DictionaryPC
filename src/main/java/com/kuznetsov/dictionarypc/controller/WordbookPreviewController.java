package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.listener.WordbookCloseListener;
import com.kuznetsov.dictionarypc.utils.TestConfigure;
import com.kuznetsov.dictionarypc.utils.WindowsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class WordbookPreviewController implements WordbookCloseListener {
    @FXML
    public Button testButton;
    @FXML
    public ChoiceBox<String> testType;
    @FXML
    public ChoiceBox<String> wordType;
    @FXML
    public Label wordsCount;
    @FXML
    public Label testDate;
    @FXML
    public Label wordTypesCount;
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

    public void setData(Wordbook wordbook) {
        setWordbook(wordbook);
        openButton.setOnAction(actionEvent -> {
            WindowsManager.showWordbookOpenWindow(this.wordbook,
                    TestConfigure.WordType.getWordType(wordType.getValue()), this);
        });
        testButton.setOnAction(actionEvent -> {
            WindowsManager.showWordbookTestWindow(this.wordbook,
                    TestConfigure.TestType.getTestType(testType.getValue()),
                    TestConfigure.WordType.getWordType(wordType.getValue()), this);
        });
    }

    @Override
    public void onCloseWordbook() {
        try {
            Wordbook newWordbook = Repository.getWordbookById(wordbook.getId());
            setWordbook(newWordbook);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setWordbook(Wordbook wordbook) {
        this.wordbook = wordbook;
        nameLabel.setText(wordbook.getName());
        testDate.setText("Last test: " + wordbook.getLastDate() + ", Last result: " + wordbook.getResult() + "%");
        try {
            //int wc = Repository.getWordsCountByWordbookId(wordbook.getId());
            int[] typesCount = Repository.getWordTypesCountByWordbookId(wordbook.getId());
            wordsCount.setText("Words: " + (typesCount[0] + typesCount[1] + typesCount[2]));
            wordTypesCount.setText("Wrong: " + typesCount[2] + ", New: " + typesCount[0] + ".");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
