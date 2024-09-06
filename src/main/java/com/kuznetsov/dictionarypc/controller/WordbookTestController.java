package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.listener.AnswerListener;
import com.kuznetsov.dictionarypc.utils.ResourcesManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordbookTestController implements AnswerListener {
    @FXML
    public VBox testsList;
    @FXML
    public ProgressBar progressBar;
    //@FXML
    //public Button start;
    @FXML
    public Label info;
//    @FXML
//    public ProgressIndicator progressIndicator;
    private Wordbook wordbook;
    private int wordCount = 0;
    private int currentWord = 0;

    @FXML
    public void initialize() {

//        start.setOnAction(actionEvent -> {
//            progressBar.setProgress(progressBar.getProgress() + 0.05);
//            progressIndicator.setProgress(progressIndicator.getProgress() + 0.05);
//        });
    }

    public void setData(Wordbook wordbook) {
        this.wordbook = wordbook;
        try {
            List<Word> words = Repository.getWordsByWordbookId(wordbook.getId());

            wordCount = words.size();
            for (Word word : words) {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        MainApplication.class.getResource(ResourcesManager.getWordTestFxmlPath()));
                fxmlLoader.load();
                WordTestController controller = (WordTestController)fxmlLoader.getController();
                controller.setData(word);
                controller.setAnswerListener(this);
                testsList.getChildren().add(fxmlLoader.getRoot());
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        info.setText("0 from " + wordCount);
    }

    @Override
    public void onAnswer(boolean isCorrect) {
        if (isCorrect) {
            ++currentWord;
            info.setText(currentWord + " from " + wordCount);
            progressBar.setProgress(progressBar.getProgress() + (1.0 / wordCount));
        }
    }
}
