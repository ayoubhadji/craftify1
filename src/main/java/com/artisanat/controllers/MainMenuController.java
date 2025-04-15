package com.artisanat.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private void ouvrirGestionProduits() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/GestionProduits.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestion des Produits");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ouvrirGestionCommandes() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/GestionCommandes.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestion des Commandes");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quitterApplication() {
        System.exit(0);
    }
} 