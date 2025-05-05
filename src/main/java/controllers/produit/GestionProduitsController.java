package controllers.produit;

import entity.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.produit.ProduitService;
import services.produit.ProduitServiceImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GestionProduitsController implements Initializable {

    private final ProduitService produitService = new ProduitServiceImpl();
    private final ObservableList<Produit> produitsList = FXCollections.observableArrayList();

    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private TextField imageUrlField;
    @FXML private TextField artisanIdField;
    @FXML private Button selectImageButton;

    @FXML private TextField idModifField;
    @FXML private TextField nomModifField;
    @FXML private TextArea descriptionModifField;
    @FXML private TextField prixModifField;
    @FXML private TextField stockModifField;
    @FXML private TextField imageUrlModifField;
    @FXML private Button selectImageModifButton;

    @FXML private TableView<Produit> produitsTable;
    @FXML private TableColumn<Produit, Integer> idColumn;
    @FXML private TableColumn<Produit, String> nomColumn;
    @FXML private TableColumn<Produit, String> descriptionColumn;
    @FXML private TableColumn<Produit, Double> prixColumn;
    @FXML private TableColumn<Produit, Integer> stockColumn;
    @FXML private TableColumn<Produit, String> imageUrlColumn;
    @FXML private TableColumn<Produit, Integer> artisanIdColumn;

    @FXML private Label messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeColumns();
        setupImageButtons();
        setupTableSelection();
        loadProduits();
    }

    private void initializeColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        artisanIdColumn.setCellValueFactory(new PropertyValueFactory<>("artisanId"));

        imageUrlColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(80);
                imageView.setFitHeight(60);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new Image(imagePath));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(new Label("Erreur image"));
                    }
                }
            }
        });
        imageUrlColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
    }

    private void setupImageButtons() {
        if (selectImageButton != null) {
            selectImageButton.setOnAction(e -> selectImage(imageUrlField));
        }
        if (selectImageModifButton != null) {
            selectImageModifButton.setOnAction(e -> selectImage(imageUrlModifField));
        }
    }

    private void setupTableSelection() {
        produitsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idModifField.setText(String.valueOf(newSelection.getId()));
                nomModifField.setText(newSelection.getNom());
                descriptionModifField.setText(newSelection.getDescription());
                prixModifField.setText(String.valueOf(newSelection.getPrix()));
                stockModifField.setText(String.valueOf(newSelection.getStock()));
                if (imageUrlModifField != null) {
                    imageUrlModifField.setText(newSelection.getImageUrl());
                }
            }
        });
    }

    private void selectImage(TextField targetField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            targetField.setText(selectedFile.toURI().toString());
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            validateFields();

            Produit produit = new Produit(
                    nomField.getText(),
                    descriptionField.getText(),
                    Double.parseDouble(prixField.getText()),
                    Integer.parseInt(stockField.getText()),
                    imageUrlField.getText(),
                    Integer.parseInt(artisanIdField.getText())
            );

            produitService.addProduit(produit);
            showSuccess("Produit ajouté avec succès !");
            clearAjoutFields();
            loadProduits();
        } catch (Exception e) {
            showError("Erreur lors de l'ajout", e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        try {
            if (idModifField.getText().isEmpty()) {
                showError("Erreur", "Veuillez sélectionner un produit à modifier");
                return;
            }

            validateModificationFields();

            int id = Integer.parseInt(idModifField.getText());
            Produit produit = produitService.getProduitById(id);
            if (produit == null) {
                showError("Erreur", "Produit non trouvé");
                return;
            }

            produit.setNom(nomModifField.getText());
            produit.setDescription(descriptionModifField.getText());
            produit.setPrix(Double.parseDouble(prixModifField.getText()));
            produit.setStock(Integer.parseInt(stockModifField.getText()));
            if (imageUrlModifField != null && !imageUrlModifField.getText().isEmpty()) {
                produit.setImageUrl(imageUrlModifField.getText());
            }

            produitService.updateProduit(produit);
            showSuccess("Produit modifié avec succès !");
            clearModifFields();
            loadProduits();
        } catch (Exception e) {
            showError("Erreur lors de la modification", e.getMessage());
        }
    }

    private void validateModificationFields() throws IllegalArgumentException {
        if (nomModifField.getText().isEmpty()) throw new IllegalArgumentException("Le nom est requis");
        if (descriptionModifField.getText().isEmpty()) throw new IllegalArgumentException("La description est requise");
        if (prixModifField.getText().isEmpty()) throw new IllegalArgumentException("Le prix est requis");
        if (stockModifField.getText().isEmpty()) throw new IllegalArgumentException("Le stock est requis");

        try {
            Double.parseDouble(prixModifField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le prix doit être un nombre valide");
        }

        try {
            Integer.parseInt(stockModifField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le stock doit être un nombre entier");
        }
    }

    @FXML
    private void handleSupprimer() {
        Produit selectedProduit = produitsTable.getSelectionModel().getSelectedItem();
        if (selectedProduit == null) {
            showError("Erreur", "Veuillez sélectionner un produit à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le produit");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                produitService.deleteProduit(selectedProduit.getId());
                showSuccess("Produit supprimé avec succès !");
                loadProduits();
            } catch (Exception e) {
                showError("Erreur lors de la suppression", e.getMessage());
            }
        }
    }

    private void validateFields() throws IllegalArgumentException {
        if (nomField.getText().isEmpty()) throw new IllegalArgumentException("Le nom est requis");
        if (descriptionField.getText().isEmpty()) throw new IllegalArgumentException("La description est requise");
        if (prixField.getText().isEmpty()) throw new IllegalArgumentException("Le prix est requis");
        if (stockField.getText().isEmpty()) throw new IllegalArgumentException("Le stock est requis");
        if (artisanIdField.getText().isEmpty()) throw new IllegalArgumentException("L'ID de l'artisan est requis");

        try {
            Double.parseDouble(prixField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le prix doit être un nombre valide");
        }

        try {
            Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le stock doit être un nombre entier");
        }

        try {
            Integer.parseInt(artisanIdField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'ID de l'artisan doit être un nombre entier");
        }
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }

    private void showError(String title, String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadProduits() {
        produitsList.clear();
        produitsList.addAll(produitService.getAllProduits());
        produitsTable.setItems(produitsList);
    }

    private void clearAjoutFields() {
        nomField.clear();
        descriptionField.clear();
        prixField.clear();
        stockField.clear();
        imageUrlField.clear();
        artisanIdField.clear();
    }

    private void clearModifFields() {
        idModifField.clear();
        nomModifField.clear();
        descriptionModifField.clear();
        prixModifField.clear();
        stockModifField.clear();
        if (imageUrlModifField != null) {
            imageUrlModifField.clear();
        }
    }

    @FXML
    private void retourMenuPrincipal(ActionEvent event) {
        try {
            loadScene(event, "/org.example/dashboard.fxml", "Menu Principal");
        } catch (IOException e) {
            showError("Erreur de navigation", "Impossible de retourner au menu principal");
        }
    }


    public void handleRafraichir(ActionEvent actionEvent) {
    }

    @FXML
    private void naviguerVersAjout(ActionEvent event) throws IOException {
        loadScene(event, "/org.example/produit/AjouterProduit.fxml", "Ajouter un Produit");
    }

    @FXML
    private void naviguerVersModification(ActionEvent event) throws IOException {
        loadScene(event, "/org.example/produit/ModifierProduit.fxml", "Modifier un Produit");
    }

    @FXML
    private void naviguerVersSuppression(ActionEvent event) throws IOException {
        loadScene(event, "/org.example/produit/SupprimerProduit.fxml", "Supprimer un Produit");
    }

    @FXML
    private void naviguerVersAffichage(ActionEvent event) throws IOException {
        loadScene(event, "/org.example/produit/AfficherProduit.fxml", "Liste des Produits");
    }

    private void loadScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la scène : " + e.getMessage());
            throw e;
        }
    }
}
