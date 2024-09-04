package com.kuznetsov.dictionarypc;

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
import java.util.List;


public class DictionaryOpenController implements WordCreatingListener {
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
    private Dict dict;

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
                    dict.getId(),
                    first.getText(),
                    second.getText(),
                    firstExample.getText(),
                    secondExample.getText()
            );
            try {
                Repository.addWord(word);
                first.setText("");
                second.setText("");
                firstExample.setText("");
                secondExample.setText("");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setDict(Dict dict) {
        this.dict = dict;
        Repository.setOnWordCreatingListener(this);
        List<Word> words = null;
        try {
            words = Repository.getWordsByDictId(dict.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Word word : words) {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    DictionaryApplication.class.getResource("word.fxml"));
            try {
                StackPane stackPane = fxmlLoader.load();
                WordController controller = (WordController)fxmlLoader.getController();
                controller.setWord(word);
                wordsList.getChildren().add(stackPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getDictId() {
        return dict.getId();
    }

    @Override
    public void onWordCreate(Word word) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                DictionaryApplication.class.getResource("word.fxml"));
        try {
            StackPane stackPane = fxmlLoader.load();
            WordController controller = (WordController)fxmlLoader.getController();
            controller.setWord(word);
            wordsList.getChildren().add(0, stackPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
