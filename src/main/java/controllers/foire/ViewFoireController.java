package controllers.foire;

import entity.Foire;
import javafx.scene.Node;
import services.foire.FoireService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ViewFoireController {
    @FXML
    private ListView<Foire> foireListView;
    @FXML
    private TextField searchField;

    private final FoireService foireService = new FoireService();
    private ObservableList<Foire> allFoires = FXCollections.observableArrayList();
    private ObservableList<Foire> filteredFoires = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        allFoires.setAll(foireService.getAllFoires());
        sortAndFilter("");
        foireListView.setItems(filteredFoires);

        // 🔍 Recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            sortAndFilter(newValue);
            foireListView.setItems(filteredFoires);
        });

        foireListView.setCellFactory(list -> new ListCell<>() {
            private final Label content = new Label();
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox hbox = new HBox(10, content, btnModifier, btnSupprimer);

            {
                btnModifier.getStyleClass().add("card-btn");
                btnSupprimer.getStyleClass().add("card-btn");
                HBox.setHgrow(content, Priority.ALWAYS);

                btnModifier.setOnAction(e -> {
                    Foire foire = getItem();
                    if (foire != null) {
                        handleUpdateFoire(foire);
                    }
                });
                btnSupprimer.setOnAction(e -> {
                    Foire foire = getItem();
                    if (foire != null) {
                        handleDeleteFoire(foire);
                    }
                });
            }

            @Override
            protected void updateItem(Foire foire, boolean empty) {
                super.updateItem(foire, empty);
                if (empty || foire == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    content.setText(foire.toString());
                    setGraphic(hbox);
                    setStyle("-fx-padding: 18px 24px;");
                }
            }
        });
    }

    private void sortAndFilter(String search) {
        List<Foire> sorted = allFoires.stream()
                .filter(f -> search == null || search.isEmpty() ||
                        f.getNom().toLowerCase().contains(search.toLowerCase()))
                .sorted(Comparator.comparing(Foire::getDateDebut))
                .collect(Collectors.toList());
        filteredFoires.setAll(sorted);
    }

    @FXML
    private void handleAddFoire(ActionEvent event) {
        try {
            String fxmlPath = "/org.example/foire/addFoire.fxml";
            var resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Le fichier addFoire.fxml est introuvable !");
                alert.showAndWait();
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Foire");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            allFoires.setAll(foireService.getAllFoires());
            sortAndFilter(searchField.getText());
            foireListView.setItems(filteredFoires);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir la fenêtre d'ajout de foire.\n" + e.getMessage());
            alert.showAndWait();
        }
    }

    private void handleUpdateFoire(Foire foire) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/foire/updateFoire.fxml"));
            Parent root = loader.load();
            UpdateFoireController controller = loader.getController();
            controller.setFoire(foire);
            Stage stage = new Stage();
            stage.setTitle("Modifier la Foire");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            allFoires.setAll(foireService.getAllFoires());
            sortAndFilter(searchField.getText());
            foireListView.setItems(filteredFoires);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteFoire(Foire foire) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette foire ?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                foireService.deleteFoire(foire.getId());
                allFoires.setAll(foireService.getAllFoires());
                sortAndFilter(searchField.getText());
                foireListView.setItems(filteredFoires);
            }
        });
    }

    @FXML
    public void handleGoToItemsList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/foire/viewItems.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir la liste des items.\n" + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void retourDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
