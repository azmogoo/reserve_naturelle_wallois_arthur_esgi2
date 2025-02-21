package com.example.evaluation_wallois_arthur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/evaluation_wallois_arthur/main-view.fxml"));
        StackPane root = fxmlLoader.load();
        
        // Chargement de l'image de fond
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/background.jpg"));
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, true)
        );
        
        // Application de l'image de fond
        root.setBackground(new Background(background));
        
        Scene scene = new Scene(root, 1024, 768);
        
        // Ajout du CSS
        scene.getStylesheets().add(getClass().getResource("/com/example/evaluation_wallois_arthur/styles/main.css").toExternalForm());
        
        stage.setTitle("Réserve Naturelle");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 