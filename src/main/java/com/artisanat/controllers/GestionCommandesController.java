package com.artisanat.controllers;

import com.artisanat.entities.Commande;
import com.artisanat.entities.LigneCommande;
import com.artisanat.entities.Produit;
import com.artisanat.services.CommandeService;
import com.artisanat.services.ProduitService;
import com.artisanat.services.impl.CommandeServiceImpl;
import com.artisanat.services.impl.ProduitServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class GestionCommandesController {
    @FXML private ComboBox<Integer> clientCombo;
    @FXML private ComboBox<String> statutCombo;
    @FXML private ComboBox<Produit> produitCombo;
    @FXML private TextField quantiteField;
    @FXML private TableView<LigneCommande> lignesCommandeTable;
    @FXML private TableColumn<LigneCommande, String> produitCol;
    @FXML private TableColumn<LigneCommande, Integer> quantiteCol;
    @FXML private TableColumn<LigneCommande, Double> prixUnitaireCol;
    @FXML private TableColumn<LigneCommande, Double> sousTotalCol;
    @FXML private Label montantTotalLabel;
    @FXML private TableView<Commande> commandesTable;
    @FXML private TableColumn<Commande, Integer> commandeIdCol;
    @FXML private TableColumn<Commande, Integer> clientCol;
    @FXML private TableColumn<Commande, LocalDate> dateCol;
    @FXML private TableColumn<Commande, Double> montantCol;
    @FXML private TableColumn<Commande, String> statutCol;

    private CommandeService commandeService;
    private ProduitService produitService;
    private ObservableList<LigneCommande> lignesCommandeList;
    private ObservableList<Commande> commandesList;
    private Commande commandeEnCours;

    @FXML
    public void initialize() {
        commandeService = new CommandeServiceImpl();
        produitService = new ProduitServiceImpl();

        // Initialiser les statuts
        ObservableList<String> statuts = FXCollections.observableArrayList(
            "EN_ATTENTE", "CONFIRMEE", "EN_PREPARATION", "EXPEDIEE", "LIVREE", "ANNULEE"
        );
        statutCombo.setItems(statuts);

        // Initialiser les clients (pour l'exemple, nous utilisons des IDs)
        ObservableList<Integer> clients = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        clientCombo.setItems(clients);

        // Initialiser les colonnes de la table des lignes de commande
        produitCol.setCellValueFactory(cellData -> cellData.getValue().getProduit().nomProperty());
        quantiteCol.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());
        prixUnitaireCol.setCellValueFactory(cellData -> cellData.getValue().prixUnitaireProperty().asObject());
        sousTotalCol.setCellValueFactory(cellData -> cellData.getValue().sousTotalProperty().asObject());

        // Initialiser les colonnes de la table des commandes
        commandeIdCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        clientCol.setCellValueFactory(cellData -> cellData.getValue().clientIdProperty().asObject());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateCommandeProperty());
        montantCol.setCellValueFactory(cellData -> cellData.getValue().montantTotalProperty().asObject());
        statutCol.setCellValueFactory(cellData -> cellData.getValue().statutProperty());

        // Initialiser les listes
        lignesCommandeList = FXCollections.observableArrayList();
        lignesCommandeTable.setItems(lignesCommandeList);

        // Charger les données initiales
        chargerCommandes();
        chargerProduits();
    }

    private void chargerCommandes() {
        commandesList = FXCollections.observableArrayList(commandeService.getAllCommandes());
        commandesTable.setItems(commandesList);
    }

    private void chargerProduits() {
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitService.getAllProduits());
        produitCombo.setItems(produits);
    }

    @FXML
    private void ajouterLigneCommande() {
        try {
            Produit produit = produitCombo.getValue();
            int quantite = Integer.parseInt(quantiteField.getText());

            if (produit != null && quantite > 0) {
                LigneCommande ligne = new LigneCommande();
                ligne.setProduit(produit);
                ligne.setQuantite(quantite);
                ligne.setPrixUnitaire(produit.getPrix());

                lignesCommandeList.add(ligne);
                updateMontantTotal();
                clearLigneFields();
            }
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez vérifier les données saisies", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void creerCommande() {
        if (!lignesCommandeList.isEmpty() && clientCombo.getValue() != null) {
            try {
                Commande commande = new Commande();
                commande.setClientId(clientCombo.getValue());
                commande.setDateCommande(LocalDate.now());
                commande.setStatut("EN_ATTENTE");
                
                // Ajouter les lignes de commande une par une
                for (LigneCommande ligne : lignesCommandeList) {
                    commande.ajouterLigneCommande(ligne);
                }

                commandeService.creerCommande(commande);
                chargerCommandes();
                clearAllFields();
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la création de la commande", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Attention", "Veuillez ajouter des produits et sélectionner un client", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void modifierStatut() {
        Commande selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande != null && statutCombo.getValue() != null) {
            selectedCommande.setStatut(statutCombo.getValue());
            commandeService.modifierCommande(selectedCommande);
            chargerCommandes();
        } else {
            showAlert("Attention", "Veuillez sélectionner une commande et un statut", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void annulerCommande() {
        Commande selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            commandeService.annulerCommande(selectedCommande.getId());
            chargerCommandes();
        } else {
            showAlert("Attention", "Veuillez sélectionner une commande à annuler", Alert.AlertType.WARNING);
        }
    }

    private double calculerMontantTotal() {
        double total = 0.0;
        for (LigneCommande ligne : lignesCommandeList) {
            total += ligne.getSousTotal();
        }
        return total;
    }

    private void updateMontantTotal() {
        montantTotalLabel.setText(String.format("%.2f €", calculerMontantTotal()));
    }

    private void clearLigneFields() {
        produitCombo.getSelectionModel().clearSelection();
        quantiteField.clear();
    }

    private void clearAllFields() {
        clientCombo.getSelectionModel().clearSelection();
        statutCombo.getSelectionModel().clearSelection();
        lignesCommandeList.clear();
        updateMontantTotal();
        clearLigneFields();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 