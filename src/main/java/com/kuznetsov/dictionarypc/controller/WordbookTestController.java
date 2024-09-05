package com.kuznetsov.dictionarypc.controller;

import com.kuznetsov.dictionarypc.MainApplication;
import com.kuznetsov.dictionarypc.data.Repository;
import com.kuznetsov.dictionarypc.entity.Word;
import com.kuznetsov.dictionarypc.entity.Wordbook;
import com.kuznetsov.dictionarypc.utils.ResourcesManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class WordbookTestController {
    @FXML
    public VBox testsList;
    private Wordbook wordbook;

    @FXML
    public void initialize() {

    }

    public void setData(Wordbook wordbook) {
        this.wordbook = wordbook;
        try {
            List<Word> words = Repository.getWordsByWordbookId(wordbook.getId());
            for (Word word : words) {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        MainApplication.class.getResource(ResourcesManager.getWordTestFxmlPath()));
                fxmlLoader.load();
                WordTestController controller = (WordTestController)fxmlLoader.getController();
                controller.setData(word);
                testsList.getChildren().add(fxmlLoader.getRoot());
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
