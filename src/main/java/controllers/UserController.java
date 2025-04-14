package controllers;

import entity.User;
import DAO.UserDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class UserController {

    public final UserDAO userDAO;

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
