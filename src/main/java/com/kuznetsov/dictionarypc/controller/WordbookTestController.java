package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.listener.AnswerListener;
import com.kuznetsov.dictionarypc.listener.WordbookCloseListener;
import com.kuznetsov.dictionarypc.utils.ResourcesManager;
import com.kuznetsov.dictionarypc.utils.TestConfigure;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WordbookTestController implements AnswerListener, WordbookCloseListener {

    @FXML
    public VBox testsList;
    @FXML
    public ProgressBar progressBar;

    @FXML
    public Label info;
    @FXML
    public StackPane rootStackPane;
    @FXML
    public ScrollPane testsScrollPane;

    private Wordbook wordbook;
    private int wordCount = 0;
    private int rightAnswerCount = 0;
    private int currentTestIndex = -1;

    private final ArrayList<WordbookCloseListener> wordbookCloseListeners = new ArrayList<>();
    @FXML
    public void initialize() {

    }

    public void setData(Wordbook wordbook, TestConfigure.TestType testType,
                        TestConfigure.WordType wordType) {
//        rootStackPane.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
//            ++currentTestIndex;
//            if (currentTestIndex >= testsList.getChildren().size()) {
//                currentTestIndex = 0;
//            }
//            testsList.getChildren().get(currentTestIndex).requestFocus();
//            if (testsScrollPane.getVvalue() != 0) {
//                testsScrollPane.setVvalue(testsScrollPane.getVvalue() + 100);
//            }
//            keyEvent.consume();
//        });

        this.wordbook = wordbook;
        try {
            List<Word> words;
            if (wordType == TestConfigure.WordType.New || wordType == TestConfigure.WordType.Wrong) {
                words = Repository.getWordsByWordbookIdAndWordType(wordbook.getId(), wordType);
            } else {
                words = Repository.getWordsByWordbookId(wordbook.getId());
            }

            wordCount = words.size();
            for (Word word : words) {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        MainApplication.class.getResource(ResourcesManager.getWordTestFxmlPath()));
                fxmlLoader.load();
                WordTestController controller = (WordTestController)fxmlLoader.getController();
                wordbookCloseListeners.add(controller);
                controller.setData(word, testType);
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
            ++rightAnswerCount;
            info.setText(rightAnswerCount + " from " + wordCount);
            progressBar.setProgress(progressBar.getProgress() + (1.0 / wordCount));
        }
    }

    @Override
    public void onCloseWordbook() {
//        System.out.println("Date = " + LocalDate.now());
//        System.out.println("Result = " + (int)(((double)rightAnswerCount / wordCount) * 100));
        wordbook.setLastDate(LocalDate.now().toString());
        wordbook.setResult((int)(((double)rightAnswerCount / wordCount) * 100));
        try {
            Repository.updateWordbook(wordbook);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (WordbookCloseListener listener : wordbookCloseListeners) {
            listener.onCloseWordbook();
        }
    }
}
