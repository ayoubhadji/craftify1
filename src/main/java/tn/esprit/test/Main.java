package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create a TabPane to hold both interfaces
        TabPane root = new TabPane();

        // Load the events interface
        FXMLLoader evenementLoader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
        Parent evenementContent = evenementLoader.load();
        javafx.scene.control.Tab evenementTab = new javafx.scene.control.Tab("Événements", evenementContent);
        evenementTab.setClosable(false);

        // Load the participations interface
        FXMLLoader participationLoader = new FXMLLoader(getClass().getResource("/AfficherParticipation.fxml"));
        Parent participationContent = participationLoader.load();
        javafx.scene.control.Tab participationTab = new javafx.scene.control.Tab("Participations", participationContent);
        participationTab.setClosable(false);

        // Add both tabs to the TabPane
        root.getTabs().addAll(evenementTab, participationTab);

        primaryStage.setTitle("Gestion des Événements et Participations");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}