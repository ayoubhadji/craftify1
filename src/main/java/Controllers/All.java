package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.foire;
import services.FoireS;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class All implements Initializable {
    @FXML
    private ListView<foire> view;

    @FXML
    private Button del;
    @FXML
    private Button mod;

    private final FoireS foireService = new FoireS();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        afficherFoires();
        del.setOnAction(event -> {
            // Récupérer l'élément sélectionné dans la ListView
            foire selectedFoire = view.getSelectionModel().getSelectedItem();
            if (selectedFoire != null) {
                try {
                    // Appeler la méthode delete du service
                    foireService.delete(selectedFoire);

                    // Mettre à jour l'affichage : retirer l'élément supprimé de la ListView
                    view.getItems().remove(selectedFoire);

                    // Facultatif : afficher un message de confirmation
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Suppression");
                    alert.setHeaderText(null);
                    alert.setContentText("La foire a été supprimée.");
                    alert.showAndWait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de la suppression");
                    alert.setContentText("Impossible de supprimer la foire : " + ex.getMessage());
                    alert.showAndWait();
                }
            } else {
                // Aucun élément sélectionné
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText("Sélection manquante");
                alert.setContentText("Veuillez sélectionner une foire à supprimer.");
                alert.showAndWait();
            }
        });
    }



    private void afficherFoires() {
        // Récupérer la liste des foires depuis le service.
        List<foire> listeFoires = foireService.getAll();

        // Effacer les anciens éléments
        view.getItems().clear();

        // Définir le CellFactory pour personnaliser l'affichage des foires
        view.setCellFactory(lv -> new ListCell<foire>() {
            @Override
            protected void updateItem(foire f, boolean empty) {
                super.updateItem(f, empty);
                if (empty || f == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Créer un conteneur horizontal (HBox)
                    HBox container = new HBox();
                    container.setSpacing(10);
                    // Appliquer des styles inline pour un fond gris, padding et bordure noire
                    container.setStyle("-fx-background-color: gray; -fx-padding: 5; -fx-border-color: black;");

                    // Création des labels pour chaque attribut
                    Label idLabel = new Label("ID: " + f.getId());
                    Label nomLabel = new Label("Nom: " + f.getNom());
                    Label descriptionLabel = new Label("Description: " + f.getDescription());
                    Label dateDebutLabel = new Label("Déb: " + f.getDate_debut());
                    Label dateFinLabel = new Label("Fin: " + f.getDate_fin());
                    Label lieuLabel = new Label("Lieu: " + f.getLieu());
                    Label capaciteLabel = new Label("Capacité: " + f.getCapacite_max());
                    Label createdAtLabel = new Label("Créé: " + f.getCreated_at());
                    Label prixLabel = new Label("Prix: " + f.getPrix());
                    Label rateLabel = new Label("Rate: " + f.getRate());

                    // Optionnel : ajouter des séparateurs verticaux entre certains labels
                    Separator sep1 = new Separator(Orientation.VERTICAL);
                    Separator sep2 = new Separator(Orientation.VERTICAL);
                    Separator sep3 = new Separator(Orientation.VERTICAL);

                    // Ajouter les labels (et les séparateurs) dans le conteneur
                    container.getChildren().addAll(
                            idLabel,
                            sep1,
                            nomLabel,
                            descriptionLabel,
                            sep2,
                            dateDebutLabel,
                            dateFinLabel,
                            sep3,
                            lieuLabel,
                            capaciteLabel,
                            createdAtLabel,
                            prixLabel,
                            rateLabel
                    );

                    // Affecter le conteneur à la cellule
                    setGraphic(container);
                }
            }
        });

        // Ajouter les foires dans la ListView
        view.getItems().addAll(listeFoires);
    }
    private void ouvrirUpdateFoire(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Update.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la vue chargée
            UpdateFoireController controller = loader.getController();
            controller.initData(id);  // Passe l'ID

            // Créer une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Modifier Foire");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleModifierAction(ActionEvent actionEvent) {
        // Récupérer la foire sélectionnée
        foire selectedFoire = view.getSelectionModel().getSelectedItem();

        if (selectedFoire != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Update.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de la vue chargée
                UpdateFoireController controller = loader.getController();

                // Passer l’ID de la foire sélectionnée
                controller.initData(selectedFoire.getId());

                // Ouvrir la nouvelle fenêtre
                Stage stage = new Stage();
                stage.setTitle("Modifier Foire");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ Veuillez sélectionner une foire à modifier.");
        }
    }

    @FXML
    public void handlevoire(ActionEvent actionEvent) {
        // Récupérer la foire sélectionnée dans la ListView
        foire selectedFoire = view.getSelectionModel().getSelectedItem();

        if (selectedFoire != null) {
            try {
                // Charger le fichier FXML pour la vue Items
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Items.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de la vue chargée
                items controller = loader.getController();

                // Passer l'ID de la foire au contrôleur de Items
                controller.initData(selectedFoire.getId());

                // Créer une nouvelle fenêtre
                Stage stage = new Stage();
                stage.setTitle("Items pour la foire");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Avertir l'utilisateur s'il n'a pas sélectionné de foire
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sélection manquante");
            alert.setHeaderText("Aucune foire sélectionnée");
            alert.setContentText("Veuillez sélectionner une foire pour afficher les items.");
            alert.showAndWait();
        }
    }

}
