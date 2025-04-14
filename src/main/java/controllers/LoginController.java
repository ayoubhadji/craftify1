package controllers;

import DAO.UserDAO;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserDAO userDAO = new UserDAO();

    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = userDAO.login(email, password);

        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/home.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Accueil");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Email ou mot de passe incorrect !");
            alert.showAndWait();
        }
    }
    public void goTosignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org.example/signup.fxml")); // Ajuste si besoin
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sign up");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
