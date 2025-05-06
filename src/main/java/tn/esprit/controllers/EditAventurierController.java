package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Aventurier;
import tn.esprit.services.AventurierService;

public class EditAventurierController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private DatePicker dateInscriptionField;
    @FXML private ComboBox<String> statutComboBox;

    private AventurierService service = new AventurierService();
    private Aventurier aventurier;

    // ✅ Initialiser les valeurs du ComboBox (exécuté au chargement)
    @FXML
    public void initialize() {
        statutComboBox.getItems().addAll("Actif", "Inactif", "Suspendu");
    }

    // ✅ Méthode appelée depuis le contrôleur principal, après ouverture de la fenêtre
    public void setAventurier(Aventurier aventurier) {
        this.aventurier = aventurier;

        // Pré-remplir les champs du formulaire
        nomField.setText(aventurier.getNom());
        prenomField.setText(aventurier.getPrenom());
        emailField.setText(aventurier.getEmail());
        phoneNumberField.setText(aventurier.getPhoneNumber());
        dateInscriptionField.setValue(aventurier.getDateInscription());
        statutComboBox.setValue(aventurier.getStatus());
    }

    @FXML
    void handleSave(ActionEvent event) {
        try {
            // Vérifier que tous les champs sont remplis
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || phoneNumberField.getText().isEmpty() ||
                    dateInscriptionField.getValue() == null || statutComboBox.getValue() == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
                return;
            }

            // Validation de l'email
            if (!emailField.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                showAlert("Erreur", "L'email n'est pas valide.", Alert.AlertType.ERROR);
                return;
            }

            // Validation du numéro de téléphone (format simple : 8 chiffres)
            if (!phoneNumberField.getText().matches("\\d{8}")) {
                showAlert("Erreur", "Le numéro de téléphone doit contenir 8 chiffres.", Alert.AlertType.ERROR);
                return;
            }

            // Mettre à jour l'aventurier avec les nouvelles valeurs
            aventurier.setNom(nomField.getText());
            aventurier.setPrenom(prenomField.getText());
            aventurier.setEmail(emailField.getText());
            aventurier.setPhoneNumber(phoneNumberField.getText());
            aventurier.setDateInscription(dateInscriptionField.getValue());
            aventurier.setStatus(statutComboBox.getValue());

            boolean isUpdated = service.updateAventurier(aventurier);

            if (isUpdated) {
                showAlert("Succès", "L'aventurier a été mis à jour avec succès.", Alert.AlertType.INFORMATION);
                closeWindow(event);
            } else {
                showAlert("Erreur", "La mise à jour de l'aventurier a échoué.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour de l'aventurier.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
