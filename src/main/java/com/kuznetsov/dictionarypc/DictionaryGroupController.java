package com.kuznetsov.dictionarypc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DictionaryGroupController implements DictCreatingListener {
    @FXML
    public FlowPane flowPane;
//    @FXML
//    public FlowPane settings;
    @FXML
    public Button addDictButton;
    @FXML
    public TextField dictName;
    @FXML
    public Button deleteGroupButton;
    @FXML
    private Tab tab;

    private DictGroup dictionaryGroup;

    public void initialize() {
        addDictButton.setOnAction(actionEvent -> {
            saveDictionary();
        });
    }

    public void setDictionaryGroup(DictGroup dictionaryGroup) {
        this.dictionaryGroup = dictionaryGroup;
        tab.setText(dictionaryGroup.getName());
        try {
            List<Dict> dicts = Repository.getDictsByGroupId(dictionaryGroup.getId());
            for (Dict dict : dicts) {
                FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication
                        .class.getResource("dictionary-preview.fxml"));
                flowPane.getChildren().add(0, fxmlLoader.load());
                DictionaryPreviewController controller
                        = (DictionaryPreviewController)fxmlLoader.getController();
                System.out.println(controller + " is null:" + (controller == null));
                controller.setDict(dict);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveDictionary() {
        Dict dict = new Dict(-1, dictName.getText(), dictionaryGroup.getId());
        try {
            Repository.addDictionary(dict);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getDictGroupId() {
        return dictionaryGroup.getId();
    }

    @Override
    public void onDictCreate(Dict dict) {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication
                .class.getResource("dictionary-preview.fxml"));
        try {
            flowPane.getChildren().add(0, fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DictionaryPreviewController controller
                = (DictionaryPreviewController)fxmlLoader.getController();
        controller.setDict(dict);
    }
}
