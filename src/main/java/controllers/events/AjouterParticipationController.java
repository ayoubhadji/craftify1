package controllers.events;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entity.Evenement;
import entity.Participation;
import services.events.ServiceEvenement;
import services.events.ServiceParticipation;
import utils.QRCode;
import utils.EmailService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.google.zxing.WriterException;

public class AjouterParticipationController implements Initializable {
    @FXML
    private ComboBox<Integer> userComboBox;
    @FXML
    private ComboBox<Evenement> evenementComboBox;
    @FXML
    private TextField prixField;
    @FXML
    private ComboBox<String> statutComboBox;
    @FXML
    private TextField nomParticipantField;

    @FXML
    private TextField emailField;

    private ServiceParticipation serviceParticipation;
    private ServiceEvenement serviceEvenement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceParticipation = new ServiceParticipation();
        serviceEvenement = new ServiceEvenement();

        // Initialize status options
        ObservableList<String> statuts = FXCollections.observableArrayList(
            "En attente",
            "Confirmée",
            "Annulée"
        );
        statutComboBox.setItems(statuts);

        // Load events into combo box
        ObservableList<Evenement> evenements = FXCollections.observableArrayList(serviceEvenement.afficher());
        evenementComboBox.setItems(evenements);
        
        // Set cell factory for event ComboBox to show event names
        evenementComboBox.setCellFactory(listView -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement evenement, boolean empty) {
                super.updateItem(evenement, empty);
                if (empty || evenement == null) {
                    setText(null);
                } else {
                    setText(evenement.getNom());
                }
            }
        });

        // Load user IDs (you should replace this with actual user data from your database)
        ObservableList<Integer> userIds = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        userComboBox.setItems(userIds);
    }

    @FXML
    private void handleAjouter() {
        try {
            Integer selectedUserId = userComboBox.getValue();
            Evenement selectedEvenement = evenementComboBox.getValue();
            double prix = Double.parseDouble(prixField.getText());
            String statut = statutComboBox.getValue();

            if (selectedUserId == null || selectedEvenement == null || statut == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            Participation participation = new Participation();
            participation.setUserId(selectedUserId);
            participation.setEvenement(selectedEvenement);
            participation.setPrix(prix);
            participation.setStatut(statut);
            
            serviceParticipation.ajouter(participation);

            // Generate and download QR code
            try {
                String participantName = nomParticipantField.getText();
                if (participantName == null || participantName.trim().isEmpty()) {
                    participantName = "Anonymous";
                }
                
                File qrCodeFile = QRCode.generateQRCode(selectedEvenement, participantName);

                // Get email address
                String email = emailField.getText().trim();
                if (!email.isEmpty() && email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    try {
                        // Send email with QR code
                        EmailService.sendQRCodeEmail(email, selectedEvenement.getNom(), qrCodeFile);
                        
                        // Show success message with QR code location and email confirmation
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Participation ajoutée");
                        successAlert.setHeaderText("La participation a été ajoutée avec succès!");
                        successAlert.setContentText("Le QR code a été généré et envoyé à votre email: " + email + 
                                                  "\n\nIl est également sauvegardé dans: " + qrCodeFile.getAbsolutePath());
                        successAlert.showAndWait();
                    } catch (Exception e) {
                        // Show email error but continue with local file
                        Alert emailAlert = new Alert(Alert.AlertType.WARNING);
                        emailAlert.setTitle("Erreur d'envoi d'email");
                        emailAlert.setHeaderText("Impossible d'envoyer le QR code par email");
                        emailAlert.setContentText("Le QR code a été sauvegardé localement dans: " + qrCodeFile.getAbsolutePath());
                        emailAlert.showAndWait();
                    }
                } else {
                    // Show success message with QR code location only
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Participation ajoutée");
                    successAlert.setHeaderText("La participation a été ajoutée avec succès!");
                    successAlert.setContentText("Le QR code a été généré et sauvegardé dans: " + qrCodeFile.getAbsolutePath());
                    successAlert.showAndWait();
                }
                
            } catch (WriterException | IOException e) {
                showAlert("Erreur", "Erreur lors de la génération du QR code: " + e.getMessage());
            }

            // Close the window
            Stage stage = (Stage) userComboBox.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide");
        }
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) userComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void preloadEvenement(Evenement evenement) {
        if (evenementComboBox != null) {
            evenementComboBox.setValue(evenement);
        }
    }

} 