package controllers.user;

import entity.User;
import DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.util.List;

import javafx.stage.Stage;
import utils.Session;

public class UserController {

    public final UserDAO userDAO;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Button backButton;

    public void initialize() {
        User user = Session.getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getNom());
            emailLabel.setText(user.getEmail());
            roleLabel.setText(user.getRole());
        }

        backButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org.example/home.fxml"));
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Accueil");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserController() {
        this.userDAO = new UserDAO(); // Initialisation
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.getAll();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            return null;
        }
    }
}
