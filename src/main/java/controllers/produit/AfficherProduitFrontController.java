package controllers.produit;

import entity.Panier;
import entity.Produit;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import services.produit.ProduitService;
import services.produit.ProduitServiceImpl;
import services.panier.PanierService;
import javafx.geometry.Insets;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import controllers.payment.StripePaymentController;
import utils.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherProduitFrontController {

    @FXML
    private FlowPane produitsContainer;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox panierItems;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField searchField;

    @FXML
    private VBox searchResultsContainer;

    private final ProduitService produitService;
    private final PanierService panierService;
    private List<Panier> panierItemsList;
    private List<Produit> allProduits;
    private double total;

    public AfficherProduitFrontController() {
        produitService = new ProduitServiceImpl();
        panierService = new PanierService();
        panierItemsList = new ArrayList<>();
        total = 0.0;
    }

    @FXML
    public void initialize() {
        allProduits = produitService.getAllProduits();
        afficherProduits();
        try {
            User currentUser = Session.getCurrentUser();
            if (currentUser != null) {
                panierItemsList = panierService.getPanierByUserId(currentUser.getId());
                updatePanier();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("❌ Erreur lors du chargement du panier");
        }
        setupSearchListener();
    }

    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                searchResultsContainer.getChildren().clear();
                searchResultsContainer.setVisible(false);
                searchResultsContainer.setManaged(false);
                afficherProduits();
                return;
            }

            PauseTransition pause = new PauseTransition(Duration.millis(300));
            pause.setOnFinished(event -> {
                List<Produit> filteredProduits = allProduits.stream()
                        .filter(produit -> produit.getNom().toLowerCase().contains(newValue.toLowerCase()) ||
                                produit.getDescription().toLowerCase().contains(newValue.toLowerCase()))
                        .collect(Collectors.toList());

                Platform.runLater(() -> {
                    if (filteredProduits.isEmpty()) {
                        searchResultsContainer.getChildren().clear();
                        Label noResults = new Label("Aucun produit trouvé");
                        noResults.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");
                        searchResultsContainer.getChildren().add(noResults);
                    } else {
                        displaySearchResults(filteredProduits);
                    }
                    searchResultsContainer.setVisible(true);
                    searchResultsContainer.setManaged(true);
                });
            });
            pause.play();
        });
    }

    private void displaySearchResults(List<Produit> produits) {
        searchResultsContainer.getChildren().clear();
        searchResultsContainer.setStyle("-fx-background-color: #2c2c2c; -fx-background-radius: 5; -fx-padding: 10;");

        for (Produit produit : produits) {
            HBox produitBox = createSearchResultBox(produit);
            produitBox.setOpacity(0);
            searchResultsContainer.getChildren().add(produitBox);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), produitBox);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        }
    }

    private HBox createSearchResultBox(Produit produit) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(5));
        box.setStyle("-fx-background-color: #363636; -fx-padding: 10; -fx-background-radius: 5; -fx-cursor: hand;");

        box.setOnMouseEntered(e -> box.setStyle("-fx-background-color: #404040; -fx-padding: 10; -fx-background-radius: 5; -fx-cursor: hand;"));
        box.setOnMouseExited(e -> box.setStyle("-fx-background-color: #363636; -fx-padding: 10; -fx-background-radius: 5; -fx-cursor: hand;"));
        box.setOnMouseClicked(e -> ajouterAuPanier(produit, 1));

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(produit.getImageUrl());
            imageView.setImage(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Erreur de chargement de l'image: " + e.getMessage());
        }

        VBox infoBox = new VBox(5);

        Label nomLabel = new Label(produit.getNom());
        nomLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold;");

        Label prixLabel = new Label(String.format("%.2f DT", produit.getPrix()));
        prixLabel.setStyle("-fx-text-fill: #00adb5;");

        infoBox.getChildren().addAll(nomLabel, prixLabel);

        box.getChildren().addAll(imageView, infoBox);
        return box;
    }

    private void afficherProduits() {
        List<Produit> produits = produitService.getAllProduits();
        produitsContainer.getChildren().clear();

        for (Produit produit : produits) {
            // Création de la carte principale
            VBox card = new VBox(10);
            card.setPrefWidth(250);
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-background-color: #1e1e1e; -fx-background-radius: 10; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");

            // Image du produit
            ImageView imageView = new ImageView();
            try {
                Image image = new Image(produit.getImageUrl());
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
            } catch (Exception e) {
                System.out.println("Erreur de chargement de l'image: " + e.getMessage());
            }

            // Conteneur pour les informations du produit
            VBox infoBox = new VBox(8);
            infoBox.setAlignment(Pos.CENTER_LEFT);
            infoBox.setStyle("-fx-padding: 10;");

            // Nom du produit
            Label nomLabel = new Label(produit.getNom());
            nomLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");
            nomLabel.setWrapText(true);

            // Description du produit
            Label descriptionLabel = new Label(produit.getDescription());
            descriptionLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");
            descriptionLabel.setWrapText(true);

            // Prix du produit
            Label prixLabel = new Label(String.format("%.2f DT", produit.getPrix()));
            prixLabel.setStyle("-fx-text-fill: #00adb5; -fx-font-size: 18px; -fx-font-weight: bold;");

            // Stock du produit
            Label stockLabel = new Label("Stock: " + produit.getStock());
            stockLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");

            // Conteneur pour la quantité
            HBox quantityBox = new HBox(10);
            quantityBox.setAlignment(Pos.CENTER);

            Spinner<Integer> quantitySpinner = new Spinner<>();
            SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, produit.getStock(), 1);
            quantitySpinner.setValueFactory(valueFactory);
            quantitySpinner.setPrefWidth(80);
            quantitySpinner.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white;");

            // Bouton Ajouter au panier
            Button btnAjouter = new Button("Ajouter au panier");
            btnAjouter.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
            btnAjouter.setPrefWidth(200);
            btnAjouter.setOnAction(e -> ajouterAuPanier(produit, quantitySpinner.getValue()));
            btnAjouter.setDisable(produit.getStock() <= 0);

            quantityBox.getChildren().addAll(new Label("Quantité:"), quantitySpinner);
            infoBox.getChildren().addAll(nomLabel, descriptionLabel, prixLabel, stockLabel, quantityBox);
            card.getChildren().addAll(imageView, infoBox, btnAjouter);
            produitsContainer.getChildren().add(card);
        }
    }

    private void ajouterAuPanier(Produit produit, int quantity) {
        // Vérifier si l'utilisateur est connecté
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            messageLabel.setText("❌ Veuillez vous connecter pour ajouter au panier");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

            // Rediriger vers la page de connexion après 2 secondes
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/login.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) messageLabel.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            pause.play();
            return;
        }

        // Vérifier le stock
        if (quantity > produit.getStock()) {
            messageLabel.setText("❌ Stock insuffisant !");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            return;
        }

        try {
            if (panierService.produitExisteDansPanier(currentUser.getId(), produit.getId())) {
                // Si le produit existe déjà, vérifier si la nouvelle quantité totale ne dépasse pas le stock
                for (Panier item : panierItemsList) {
                    if (item.getProduitId() == produit.getId()) {
                        int newQuantity = item.getQuantity() + quantity;
                        if (newQuantity > produit.getStock()) {
                            messageLabel.setText("❌ Stock insuffisant !");
                            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                            return;
                        }
                        panierService.updateQuantity(currentUser.getId(), produit.getId(), newQuantity);
                        item.setQuantity(newQuantity);
                        break;
                    }
                }
            } else {
                // Si le produit n'existe pas, l'ajouter
                Panier nouveauPanier = new Panier(currentUser.getId(), produit.getId(), quantity);
                panierService.ajouterAuPanier(nouveauPanier);
                panierItemsList.add(nouveauPanier);
            }

            updatePanier();

            // Afficher un message de succès
            messageLabel.setText("✅ Produit ajouté au panier");
            messageLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14px;");

            // Faire disparaître le message après 3 secondes
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> messageLabel.setText(""));
            pause.play();

        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("❌ Erreur lors de l'ajout au panier : " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        }
    }

    private void updatePanier() {
        if (panierItems == null) {
            System.out.println("panierItems est null");
            return;
        }

        panierItems.getChildren().clear();
        total = 0.0;

        // En-tête du panier
        Label panierTitle = new Label("🛒 Votre Panier");
        panierTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        panierItems.getChildren().add(panierTitle);

        if (panierItemsList.isEmpty()) {
            Label emptyLabel = new Label("Votre panier est vide");
            emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px; -fx-padding: 20;");
            panierItems.getChildren().add(emptyLabel);
        } else {
            for (Panier panier : panierItemsList) {
                Produit produit = produitService.getProduitById(panier.getProduitId());
                if (produit != null) {
                    // Carte du produit dans le panier
                    VBox produitCard = new VBox(10);
                    produitCard.setStyle("-fx-background-color: #2c2c2c; -fx-background-radius: 8; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);");

                    // Image du produit
                    ImageView produitImage = new ImageView();
                    try {
                        Image image = new Image(produit.getImageUrl());
                        produitImage.setImage(image);
                        produitImage.setFitWidth(100);
                        produitImage.setFitHeight(100);
                        produitImage.setPreserveRatio(true);
                    } catch (Exception e) {
                        System.out.println("Erreur de chargement de l'image: " + e.getMessage());
                    }

                    // Informations du produit
                    VBox infoBox = new VBox(5);
                    infoBox.setStyle("-fx-padding: 5;");

                    Label nomLabel = new Label(produit.getNom() + " (x" + panier.getQuantity() + ")");
                    nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

                    Label prixLabel = new Label(String.format("%.2f DT", produit.getPrix() * panier.getQuantity()));
                    prixLabel.setStyle("-fx-text-fill: #00adb5; -fx-font-size: 14px;");

                    // Boutons d'action
                    HBox actionBox = new HBox(10);
                    actionBox.setAlignment(Pos.CENTER_RIGHT);

                    Button moinsBtn = new Button("-");
                    moinsBtn.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; -fx-background-radius: 5;");
                    moinsBtn.setOnAction(e -> {
                        if (panier.getQuantity() > 1) {
                            try {
                                panierService.updateQuantity(Session.getCurrentUser().getId(), produit.getId(), panier.getQuantity() - 1);
                                panier.setQuantity(panier.getQuantity() - 1);
                                updatePanier();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    Button plusBtn = new Button("+");
                    plusBtn.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; -fx-background-radius: 5;");
                    plusBtn.setOnAction(e -> {
                        if (panier.getQuantity() < produit.getStock()) {
                            try {
                                panierService.updateQuantity(Session.getCurrentUser().getId(), produit.getId(), panier.getQuantity() + 1);
                                panier.setQuantity(panier.getQuantity() + 1);
                                updatePanier();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    Button supprimerBtn = new Button("❌");
                    supprimerBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-background-radius: 5;");
                    supprimerBtn.setOnAction(e -> {
                        try {
                            panierService.supprimerDuPanier(Session.getCurrentUser().getId(), produit.getId());
                            panierItemsList.remove(panier);
                            updatePanier();
                            messageLabel.setText("❌ Produit retiré du panier : " + produit.getNom());
                            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 10;");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    });

                    actionBox.getChildren().addAll(moinsBtn, plusBtn, supprimerBtn);
                    infoBox.getChildren().addAll(nomLabel, prixLabel, actionBox);

                    HBox cardContent = new HBox(10);
                    cardContent.getChildren().addAll(produitImage, infoBox);
                    produitCard.getChildren().add(cardContent);

                    panierItems.getChildren().add(produitCard);
                    total += produit.getPrix() * panier.getQuantity();
                }
            }

            // Séparateur
            panierItems.getChildren().add(new Separator());

            // Total
            HBox totalBox = new HBox(10);
            totalBox.setAlignment(Pos.CENTER_RIGHT);
            totalBox.setPadding(new javafx.geometry.Insets(10));

            Label totalTitleLabel = new Label("Total :");
            totalTitleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

            totalLabel.setText(String.format("%.2f DT", total));
            totalLabel.setStyle("-fx-text-fill: #00adb5; -fx-font-size: 18px; -fx-font-weight: bold;");

            totalBox.getChildren().addAll(totalTitleLabel, totalLabel);
            panierItems.getChildren().add(totalBox);

            // Bouton Commander
            Button commanderBtn = new Button("Commander");
            commanderBtn.setStyle("-fx-background-color: #00adb5; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 5; -fx-padding: 10 20;");
            commanderBtn.setMaxWidth(Double.MAX_VALUE);
            commanderBtn.setOnAction(e -> commander());

            panierItems.getChildren().add(commanderBtn);
        }
    }

    @FXML
    public void commander() {
        if (panierItemsList.isEmpty()) {
            messageLabel.setText("❌ Votre panier est vide !");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 10;");
            return;
        }

        try {
            // Load the payment form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/payment/stripe-payment.fxml"));
            Parent root = loader.load();

            // Get the controller after loading the FXML
            StripePaymentController stripeController = loader.getController();
            if (stripeController == null) {
                throw new RuntimeException("Failed to get StripePaymentController");
            }

            // Set the payment amount and cart items
            stripeController.setAmount(total);
            stripeController.setPanierItems(panierItemsList);

            // Show the payment scene
            Stage stage = (Stage) produitsContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("❌ Erreur lors du chargement de la page de paiement: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 10;");
        }
    }

    @FXML
    private void goToProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/profile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) produitsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Connexion");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de navigation", "Impossible de retourner à la page de connexion");
        }
    }

    private void showError(String erreurDeNavigation, String s) {

    }

    @FXML
    private void goHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) produitsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goprudect() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/produit/AfficherProduitFront.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) produitsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
