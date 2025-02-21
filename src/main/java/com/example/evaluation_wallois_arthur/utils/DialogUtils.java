package com.example.evaluation_wallois_arthur.utils;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import java.util.Optional;

public class DialogUtils {
    public static Dialog<?> createCustomDialog(String title, String headerText) {
        Dialog<?> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        
        // Style du dialogue
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().add("custom-dialog");
        dialogPane.getStylesheets().add(DialogUtils.class.getResource("/com/example/evaluation_wallois_arthur/styles/dialog.css").toExternalForm());
        
        // Boutons
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        return dialog;
    }
} 