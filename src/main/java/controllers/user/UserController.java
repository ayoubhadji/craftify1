package controllers.user;

import entity.User;
import DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.io.IOException;
import java.util.List;

import javafx.stage.Stage;
import utils.Session;

public class UserController {

    public final UserDAO userDAO;

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label codeLabel;
    @FXML private Label roleLabel;
    @FXML private Label sexeLabel;
    @FXML private Label dateNaissanceLabel;
    @FXML private Label dateJoinLabel;
    @FXML private Label telLabel;
    @FXML private Label addressLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button updateButton;


    public void initialize() {
        User user = Session.getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getNom());
            emailLabel.setText(user.getEmail());
            codeLabel.setText(user.getCode());
            roleLabel.setText(user.getRole());
            sexeLabel.setText(user.getSexe());
            dateNaissanceLabel.setText(user.getDateNaissance().toString());
            dateJoinLabel.setText(user.getDateJoin().toString());
            telLabel.setText(user.getTel());
            addressLabel.setText(user.getAddress());
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

        // Update button action - Goes to the update profile screen
        updateButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/user/user_update.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) updateButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Mettre à jour le profil");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Log the error
                System.err.println("Error loading user update screen: " + e.getMessage());
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
