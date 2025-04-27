package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ChatBotController {

    @FXML private VBox root;
    @FXML private VBox messagesBox;
    @FXML private HBox buttonBar;
    @FXML private ScrollPane scrollPane;
    @FXML private Button modeButton;
    

    private boolean darkMode = true;
    private Scene scene;

    public void initialize() {
        modeButton.setOnAction(e -> toggleMode());
        setLightMode();  // Set the default mode to light
        botMessage("Bonjour ! Comment puis-je vous aider aujourd'hui ?");
        showOptions(List.of("Aide", "Connexion", "Mot de passe oublié", "Problème"));
    }


    public void setScene(Scene scene) {
        this.scene = scene;
        setDarkMode(); // démarre en mode sombre
    }

    private void showOptions(List<String> options) {
        buttonBar.getChildren().clear();

        for (String option : options) {
            Button btn = new Button(option);
            btn.setOnAction(e -> userChooses(option));
            styleButton(btn);
            buttonBar.getChildren().add(btn);
        }
    }

    private void userChooses(String choice) {
        userMessage(choice);
        handleChoice(choice.toLowerCase());
    }

    private void handleChoice(String input) {
        if (input.contains("aide")) {
            botMessage("Je peux vous aider avec la connexion, la réinitialisation du mot de passe, les bugs et plus encore.");
            showOptions(List.of("Connexion", "Réinitialisation du mot de passe", "Bugs"));
        } else if (input.contains("connexion")) {
            botMessage("Pour la connexion, vérifiez votre email et mot de passe. Utilisez 'mot de passe oublié' si nécessaire.");
            showOptions(List.of("Retour"));
        } else if (input.contains("mot de passe")) {
            botMessage("Cliquez sur 'Mot de passe oublié' et suivez les instructions par email.");
            showOptions(List.of("Retour"));
        } else if (input.contains("problème") || input.contains("bugs")) {
            botMessage("Merci de préciser votre problème, nous ferons de notre mieux pour vous aider !");
            showOptions(List.of("Connexion", "Réinitialisation du mot de passe", "Autre"));
        } else if (input.contains("retour")) {
            botMessage("Que souhaitez-vous faire ?");
            showOptions(List.of("Aide", "Connexion", "Mot de passe oublié", "Problème"));
        } else {
            botMessage("Désolé, je n'ai pas compris. Pouvez-vous reformuler ?");
            showOptions(List.of("Aide", "Connexion", "Mot de passe oublié", "Problème"));
        }
    }

    private void botMessage(String message) {
        HBox box = createMessageBox(message, true);
        messagesBox.getChildren().add(box);
        scrollPane.setVvalue(1.0); // scroll en bas automatique
    }

    private void userMessage(String message) {
        HBox box = createMessageBox(message, false);
        messagesBox.getChildren().add(box);
        scrollPane.setVvalue(1.0);
    }

    private HBox createMessageBox(String message, boolean isBot) {
        Label msgLabel = new Label(message);
        msgLabel.setFont(new Font(14));
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(250);
        msgLabel.setPadding(new Insets(8));
        msgLabel.setStyle("-fx-background-radius: 10; -fx-background-color: " + (isBot ? "#444" : "#0066cc") + "; -fx-text-fill: white;");

        Circle avatar = new Circle(15, isBot ? Color.GRAY : Color.LIGHTBLUE);

        HBox box = new HBox(10, isBot ? avatar : msgLabel, isBot ? msgLabel : avatar);
        box.setAlignment(isBot ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
        return box;
    }

    private void toggleMode() {
        darkMode = !darkMode;
        if (darkMode) {
            setDarkMode();
        } else {
            setLightMode();
        }
    }

    private void setDarkMode() {
        root.setStyle("-fx-background-color: #000000;");
        messagesBox.setStyle("-fx-background-color: #000000;");
    }

    private void setLightMode() {
        // Set background colors for root and messages box
        root.setStyle("-fx-background-color: #ffffff;");
        messagesBox.setStyle("-fx-background-color: #ffffff;");

        // Update buttonBar background to a light color and update button text colors
        buttonBar.setStyle("-fx-background-color: #f0f0f0;"); // Light gray background for button bar

        buttonBar.getChildren().forEach(button -> {
            button.setStyle(
                    "-fx-background-color: #0066cc; " + // Blue background for buttons
                            "-fx-border-color: gray; " +
                            "-fx-border-radius: 5; " +
                            "-fx-text-fill: white;" // White text color
            );
        });

        // Set the mode button to have a suitable style too for light mode
        modeButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: gray; " +
                        "-fx-border-radius: 5; " +
                        "-fx-text-fill: #0066cc;"
        );
    }


    private void styleButton(Button button) {
        button.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: gray; " +
                        "-fx-border-radius: 5; " +
                        "-fx-text-fill: #144faa;"
        );
        button.setFont(new Font(13));
    }

    public void goHome(ActionEvent event) {
        loadScene(event, "/org.example/home.fxml", "Accueil");
    }

    private void loadScene(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
