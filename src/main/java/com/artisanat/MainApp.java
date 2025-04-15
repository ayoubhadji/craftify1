package com.artisanat;

import com.artisanat.database.MyDataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialiser la base de données
        MyDataBase.initializeDatabase();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        primaryStage.setTitle("Système de Gestion d'Art et Artisanat");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Fermer la connexion à la base de données à la fermeture de l'application
        MyDataBase.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 