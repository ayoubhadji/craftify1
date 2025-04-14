package controllers;

import entity.User;
import DAO.UserDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

import java.util.List;
import utils.Session;

public class UserController {

    public final UserDAO userDAO;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label roleLabel;

    public void initialize() {
        User user = Session.getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getNom());
            emailLabel.setText(user.getEmail());
            roleLabel.setText(user.getRole());
        }
    }

    public UserController() {
        this.userDAO = new UserDAO(); // Initialisation
    }

    public void ajouterUser(ActionEvent actionEvent) {
        // Ancienne version console/test
        // Tu peux la garder si tu veux encore lancer via console
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
