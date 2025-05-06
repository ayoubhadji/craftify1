package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Aventurier;
import tn.esprit.services.AventurierService;
import javafx.collections.ObservableList;

public class AddAventurierController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField phoneField;

    private AventurierService service = new AventurierService();

    // List to refresh in parent controller
    private ObservableList<Aventurier> aventuriers;

    // Method to pass the ObservableList from the parent controller
    public void setAventuriersList(ObservableList<Aventurier> aventuriers) {
        this.aventuriers = aventuriers;
    }

    // Initializing the ComboBox with possible status values
    @FXML
    public void initialize() {
        statusComboBox.getItems().addAll("Actif", "Inactif", "Suspendu");
    }

    // Handle save action
    @FXML
    void handleSave(ActionEvent event) {
        try {
            // Validation logic
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    datePicker.getValue() == null || statusComboBox.getValue() == null || phoneField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Tous les champs doivent être remplis.");
                return;
            }

            // Create a new Aventurier object
            Aventurier a = new Aventurier(
                    nomField.getText(),
                    prenomField.getText(),
                    emailField.getText(),
                    datePicker.getValue(),
                    statusComboBox.getValue(),
                    phoneField.getText()
            );

            // Add the new Aventurier
            service.addAventurier(a);

            // Refresh the Aventuriers list in the parent controller
            aventuriers.add(a);

            showAlert(Alert.AlertType.INFORMATION, "Aventurier ajouté avec succès !");
            ((Stage) nomField.getScene().getWindow()).close();  // Close the AddAventurier window
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Une erreur inattendue s'est produite.");
        }
    }

    // Handle cancel action
    @FXML
    void handleCancel(ActionEvent event) {
        // Close the window without saving
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    // Method to display alerts
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}
