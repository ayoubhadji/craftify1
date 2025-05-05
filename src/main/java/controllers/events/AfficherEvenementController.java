package controllers.events;

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
import javafx.stage.Stage;
import entity.Evenement;
import services.events.ServiceEvenement;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AfficherEvenementController implements Initializable {

    @FXML
    private ListView<Evenement> evenementListView;

    @FXML
    private TextField searchField;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Pagination pagination;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private ObservableList<Evenement> evenementList = FXCollections.observableArrayList();
    private ObservableList<Evenement> currentPageList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int ITEMS_PER_PAGE = 3; //max pagination per page !!

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListView();
        loadEvenementData();
        setupSearchListener();
        setupPagination();
    }

    private void setupListView() {
        evenementListView.setCellFactory(listView -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement evenement, boolean empty) {
                super.updateItem(evenement, empty);
                if (empty || evenement == null) {
                    setText(null);
                    setStyle("");
                } else {
                    // display text bel event details
                    String displayText = String.format("Nom: %s | Lieu: %s | Type: %s | Date: %s - %s | Prix: %.2f DT",
                            evenement.getNom(),
                            evenement.getLieu(),
                            evenement.getTypeEvenement(),
                            evenement.getDateDebut().format(dateFormatter),
                            evenement.getDateFin().format(dateFormatter),
                            evenement.getPrix());
                    setText(displayText);
                    
                    setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                }
            }
        });
    }

    private void loadEvenementData() {
        evenementList.clear();
        evenementList.addAll(serviceEvenement.afficher());
        updatePagination();
    }

    //el paginationnnnnn
    private void setupPagination() {
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            updatePageContent(newIndex.intValue());
        });
    }

    private void updatePagination() {
        int pageCount = (evenementList.size() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
        pagination.setPageCount(Math.max(1, pageCount));
        updatePageContent(0);
    }

    private void updatePageContent(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, evenementList.size());

        if (fromIndex > toIndex) {
            currentPageList.clear();
        } else {
            currentPageList = FXCollections.observableArrayList(
                evenementList.subList(fromIndex, toIndex)
            );
        }
        evenementListView.setItems(currentPageList);
    }

    //recherche dynamique :
    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                evenementList.clear();
                evenementList.addAll(serviceEvenement.afficher());
            } else {
                ObservableList<Evenement> filteredList = FXCollections.observableArrayList();
                for (Evenement evenement : serviceEvenement.afficher()) {
                    if (evenement.getNom().toLowerCase().contains(newValue.toLowerCase()) ||
                        evenement.getLieu().toLowerCase().contains(newValue.toLowerCase()) ||
                        evenement.getTypeEvenement().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(evenement);
                    }
                }
                evenementList.clear();
                evenementList.addAll(filteredList);
            }
            updatePagination();
        });
    }

    //supression window for check:
    @FXML
    void handleSupprimer(ActionEvent event) {
        Evenement selectedEvenement = evenementListView.getSelectionModel().getSelectedItem();
        if (selectedEvenement != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer l'événement");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                serviceEvenement.supprimer(selectedEvenement.getId());
                loadEvenementData();
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
        }
    }

    @FXML
    void handleModifier(ActionEvent event) {
        Evenement selectedEvenement = evenementListView.getSelectionModel().getSelectedItem();
        if (selectedEvenement != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/ModifierEvenement.fxml"));
                Parent root = loader.load();
                ModifierEvenementController controller = loader.getController();
                controller.initData(selectedEvenement);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Événement");
                stage.showAndWait();
                loadEvenementData();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
        }
    }

    // window pour ajouter un event :
    @FXML
    void handleAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/AjouterEvenement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Événement");
            stage.showAndWait();
            loadEvenementData();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    //window pour l'event en detail:
    @FXML
    void handleDetails(ActionEvent event) {
        Evenement selectedEvenement = evenementListView.getSelectionModel().getSelectedItem();
        if (selectedEvenement != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/DetailsEvenement.fxml"));
                Parent root = loader.load();
                DetailsEvenementController controller = loader.getController();
                controller.setEvenement(selectedEvenement);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de l'événement");
                stage.show();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre des détails.");
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement pour voir les détails.");
        }
    }

    //next tab page ta3 l participation

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    @FXML
    private void test(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/AfficherParticipation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}