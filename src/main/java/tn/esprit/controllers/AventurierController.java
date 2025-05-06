package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Aventurier;
import tn.esprit.services.AventurierService;

import java.io.IOException;

public class AventurierController {

    @FXML private ListView<Aventurier> listViewAventuriers;
    private AventurierService service = new AventurierService();

    private ObservableList<Aventurier> aventuriers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            aventuriers.addAll(service.getAllAventuriers());  // Initialize the list of aventuriers
            listViewAventuriers.setItems(aventuriers);
        } catch (Exception e) {  // Handle exceptions
            e.printStackTrace();
        }
    }

    // Add Aventurier
    @FXML
    void handleAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_aventurier.fxml"));
        Parent root = loader.load();

        // Pass the ObservableList to the AddAventurierController
        AddAventurierController addAventurierController = loader.getController();
        addAventurierController.setAventuriersList(aventuriers);  // Pass the list

        Stage stage = new Stage();
        stage.setTitle("Ajouter un Aventurier");
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Delete Aventurier
    @FXML
    void handleDelete(ActionEvent event) {
        Aventurier selected = listViewAventuriers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                if (service.deleteAventurier(selected.getId())) {  // Check for successful deletion
                    aventuriers.remove(selected);  // Update the list
                }
            } catch (Exception e) {  // Handle errors
                e.printStackTrace();
            }
        }
    }

    // Edit Aventurier
    @FXML
    void handleEdit(ActionEvent event) throws IOException {
        Aventurier selected = listViewAventuriers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_aventurier.fxml"));
            Parent root = loader.load();
            EditAventurierController controller = loader.getController();
            controller.setAventurier(selected);
            Stage stage = new Stage();
            stage.setTitle("Modifier Aventurier");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}
