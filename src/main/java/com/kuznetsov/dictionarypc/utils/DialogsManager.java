package com.kuznetsov.dictionarypc.utils;

import com.kuznetsov.dictionarypc.data.Repository;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.sql.SQLException;
import java.util.Optional;

public class DialogsManager {
    private DialogsManager() {}

    public static boolean showOkCancelDialog(String title,
                                                          String headerText,
                                                          String contentText) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("OK", ButtonBar.ButtonData.OK_DONE),
                new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
        );
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.orElseThrow().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return true;
            }
        }
        return false;
    }
}
