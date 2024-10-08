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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WordbookOpenController implements WordCreatingListener, WordbookCloseListener, ItemDeleteListener {
    @FXML
    public StackPane rootStackPane;
    @FXML
    public Button btChangeMode;
    @FXML
    public Button btOpacityPlus;
    @FXML
    public Button btOpacityMinus;
    public ScrollPane scrollPaneWords;
    @FXML
    private TextField tfRussianWord;
    @FXML
    private TextField tfForeignWord;
    @FXML
    public TextArea taRussianExample;
    @FXML
    public TextArea taForeignExample;
    @FXML
    public Button saveWordButton;
    @FXML
    public VBox wordsList;
    private Wordbook wordbook;
    private final ArrayList<WordbookCloseListener> wordbookCloseListeners = new ArrayList<>();
    private final double minOpacity = 0.1;
    private final double opacityChangeStep = 0.05;
    private final int WORDS_INVISIBLE_MODE = 0;
    private final int WORDS_VISIBLE_MODE = 1;
    private int mode = WORDS_VISIBLE_MODE;

    @FXML
    public void initialize() {
        taRussianExample.setOnMouseEntered(mouseEvent -> {
            taRussianExample.setCursor(Cursor.MOVE);
        });

        taRussianExample.setOnMouseDragged(mouseEvent -> {
            double newWidth = mouseEvent.getX();
            double newHeight = mouseEvent.getY();
            System.out.println(newWidth + " " + newHeight);
            taRussianExample.setPrefSize(newWidth, newHeight);
        });

        saveWordButton.setOnAction(actionEvent -> {
            Word word = new Word(
                    -1,
                    wordbook.getId(),
                    tfRussianWord.getText(),
                    tfForeignWord.getText(),
                    taRussianExample.getText(),
                    taForeignExample.getText(),
                    TestConfigure.WordType.New
            );
            Repository.createWord(word);
            tfRussianWord.setText("");
            tfForeignWord.setText("");
            taRussianExample.setText("");
            taForeignExample.setText("");
        });
    }

    public void setData(Wordbook wordbook, TestConfigure.WordType wordType, Stage mainWindow) {
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
        btOpacityMinus.setOnAction(event -> {
            double newOpacity = mainWindow.getOpacity() - opacityChangeStep;
            if (newOpacity > minOpacity) {
                mainWindow.setOpacity(newOpacity);
            }
        });
        btOpacityPlus.setOnAction(event -> {
            mainWindow.setOpacity(mainWindow.getOpacity() + opacityChangeStep);
        });
        btChangeMode.setOnAction(event -> {
            if (mode == WORDS_VISIBLE_MODE) {
                scrollPaneWords.setVisible(false);
                scrollPaneWords.setManaged(false);
                mainWindow.setAlwaysOnTop(true);
                mode = WORDS_INVISIBLE_MODE;
            } else if (mode == WORDS_INVISIBLE_MODE) {
                scrollPaneWords.setVisible(true);
                scrollPaneWords.setManaged(true);
                mainWindow.setAlwaysOnTop(false);
                mode = WORDS_VISIBLE_MODE;
            }
        });
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
