package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.listener.ItemDeleteListener;
import com.kuznetsov.dictionarypc.listener.WordCreatingListener;
import com.kuznetsov.dictionarypc.listener.WordbookCloseListener;
import com.kuznetsov.dictionarypc.utils.TestConfigure;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class WordbookOpenController implements WordCreatingListener, WordbookCloseListener, ItemDeleteListener {
    @FXML
    public StackPane rootStackPane;
    @FXML
    public TextArea firstExample;
    @FXML
    public TextArea secondExample;
    @FXML
    private TextField first;
    @FXML
    private TextField second;
    @FXML
    public Button saveWordButton;
    @FXML
    public VBox wordsList;
    private Wordbook wordbook;
    private final ArrayList<WordbookCloseListener> wordbookCloseListeners = new ArrayList<>();

    @FXML
    public void initialize() {
        firstExample.setOnMouseEntered(mouseEvent -> {
            firstExample.setCursor(Cursor.MOVE);
        });

        firstExample.setOnMouseDragged(mouseEvent -> {
            double newWidth = mouseEvent.getX();
            double newHeight = mouseEvent.getY();
            System.out.println(newWidth + " " + newHeight);
            firstExample.setPrefSize(newWidth, newHeight);
        });

        saveWordButton.setOnAction(actionEvent -> {
            Word word = new Word(
                    -1,
                    wordbook.getId(),
                    first.getText(),
                    second.getText(),
                    firstExample.getText(),
                    secondExample.getText(),
                    TestConfigure.WordType.New
            );
            Repository.createWord(word);
            first.setText("");
            second.setText("");
            firstExample.setText("");
            secondExample.setText("");
        });
    }

    public void setData(Wordbook wordbook, TestConfigure.WordType wordType) {
        this.wordbook = wordbook;
        Repository.setOnWordCreatingListener(this);
        List<Word> words = null;
        if (wordType == TestConfigure.WordType.Wrong || wordType == TestConfigure.WordType.New) {
            words = Repository.selectWords(wordbook.getId(), wordType);
        } else {
            words = Repository.selectWords(wordbook.getId(), null);
        }
        for (Word word : words) {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    MainApplication.class.getResource("/com/kuznetsov/dictionarypc/views/word-open.fxml"));
            try {
                StackPane stackPane = fxmlLoader.load();
                WordOpenController controller = (WordOpenController)fxmlLoader.getController();
                controller.setWord(word, this);
                wordbookCloseListeners.add(controller);
                wordsList.getChildren().add(stackPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getWordbookId() {
        return wordbook.getId();
    }

    @Override
    public void onWordCreate(Word word) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("/com/kuznetsov/dictionarypc/views/word-open.fxml"));
        try {
            StackPane stackPane = fxmlLoader.load();
            WordOpenController controller = (WordOpenController)fxmlLoader.getController();
            controller.setWord(word, this);
            wordsList.getChildren().add(0, stackPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCloseWordbook() {
        for (WordbookCloseListener wordbookCloseListener : wordbookCloseListeners) {
            wordbookCloseListener.onCloseWordbook();
        }
    }

    @Override
    public void onItemDelete(Object obj) {
        wordsList.getChildren().remove(obj);
    }
}
