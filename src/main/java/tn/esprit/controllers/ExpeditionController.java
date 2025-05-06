package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tn.esprit.entities.Expedition;
import tn.esprit.services.ExpeditionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ExpeditionController {

    @FXML
    private ListView<Expedition> expeditionListView;

    @FXML
    private Button btnAjouter, btnSupprimer;

    private final ExpeditionService expeditionService = new ExpeditionService();

    @FXML
    public void initialize() {
        chargerExpeditions();
    }

    private void chargerExpeditions() {
        try {
            List<Expedition> list = expeditionService.getAllExpeditions();
            ObservableList<Expedition> observableList = FXCollections.observableArrayList(list);
            expeditionListView.setItems(observableList);

            // 👇 Personnalisation des cellules
            expeditionListView.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
                @Override
                protected void updateItem(Expedition expedition, boolean empty) {
                    super.updateItem(expedition, empty);
                    if (empty || expedition == null) {
                        setGraphic(null);
                    } else {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ExpeditionCell.fxml"));
                            Parent cellRoot = loader.load();
                            FrontController controller = loader.getController();
                            controller.setExpedition(expedition);
                            setGraphic(cellRoot);
                        } catch (IOException e) {
                            e.printStackTrace();
                            setGraphic(null);
                        }
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_expedition.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une expédition");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            chargerExpeditions(); // Rafraîchir la liste après ajout
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSupprimer(ActionEvent event) {
        Expedition selected = expeditionListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                expeditionService.deleteExpedition(selected.getId());
                chargerExpeditions(); // Rafraîchir après suppression
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleModifier(ActionEvent event) {
        Expedition selected = expeditionListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_expedition.fxml"));
                Parent root = loader.load();
                EditExpeditionController controller = loader.getController();
                controller.setExpedition(selected);
                Stage stage = new Stage();
                stage.setTitle("Modifier l'expédition");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                chargerExpeditions();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
