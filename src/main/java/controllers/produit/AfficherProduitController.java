package controllers.produit;

import entity.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import services.produit.AfficherProduitServiceImpl;
import services.produit.AfficherProduitService;
import java.util.List;

public class AfficherProduitController {
    
    private final AfficherProduitService afficherProduitService = new AfficherProduitServiceImpl();
    
    @FXML
    private TableView<Produit> produitsTable;
    
    @FXML
    private TableColumn<Produit, Integer> idColumn;
    
    @FXML
    private TableColumn<Produit, String> nomColumn;
    
    @FXML
    private TableColumn<Produit, String> descriptionColumn;
    
    @FXML
    private TableColumn<Produit, Double> prixColumn;
    
    @FXML
    private TableColumn<Produit, Integer> stockColumn;
    
    @FXML
    private TableColumn<Produit, String> imageUrlColumn;
    
    @FXML
    private TableColumn<Produit, Integer> artisanIdColumn;
    
    @FXML
    private TextField idField;
    
    @FXML
    private TextField stockMinField;
    
    @FXML
    private TextField prixMaxField;
    
    @FXML
    private void initialize() {
        // Initialiser les colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        imageUrlColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        artisanIdColumn.setCellValueFactory(new PropertyValueFactory<>("artisanId"));
        
        // Charger tous les produits
        loadAllProduits();
    }
    
    @FXML
    private void loadAllProduits() {
        List<Produit> produitsList = afficherProduitService.getAllProduits();
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitsList);
        produitsTable.setItems(produits);
    }
    
    @FXML
    private void loadProduitById() {
        try {
            int id = Integer.parseInt(idField.getText());
            Produit produit = afficherProduitService.getProduitById(id);
            if (produit != null) {
                ObservableList<Produit> produits = FXCollections.observableArrayList(produit);
                produitsTable.setItems(produits);
            }
        } catch (NumberFormatException e) {
            // Gérer l'erreur
        }
    }
    
    @FXML
    private void loadProduitsByStock() {
        try {
            int stockMin = Integer.parseInt(stockMinField.getText());
            ObservableList<Produit> produits = FXCollections.observableArrayList(
                afficherProduitService.getProduitsByStock(stockMin)
            );
            produitsTable.setItems(produits);
        } catch (NumberFormatException e) {
            // Gérer l'erreur
        }
    }
    
    @FXML
    private void loadProduitsByPrix() {
        try {
            double prixMax = Double.parseDouble(prixMaxField.getText());
            ObservableList<Produit> produits = FXCollections.observableArrayList(
                afficherProduitService.getProduitsByPrix(prixMax)
            );
            produitsTable.setItems(produits);
        } catch (NumberFormatException e) {
            // Gérer l'erreur
        }
    }
} 