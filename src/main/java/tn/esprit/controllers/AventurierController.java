package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import tn.esprit.entities.Aventurier;
import tn.esprit.services.AventurierService;
import javafx.stage.Stage;
import javafx.scene.Scene;  // Importer la classe Scene

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AventurierController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker dateInscriptionPicker;
    @FXML
    private TextField statutField;
    @FXML
    private TextField phoneNumberField;

    @FXML
    private ListView<String> aventurierListView;

    private final AventurierService aventurierService = new AventurierService();
    private ObservableList<Aventurier> aventurierObservableList = FXCollections.observableArrayList();
    private Aventurier selectedAventurier = null;

    @FXML
    public void initialize() {
        afficherAventuriers();

        aventurierListView.setOnMouseClicked(event -> {
            int index = aventurierListView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < aventurierObservableList.size()) {
                selectedAventurier = aventurierObservableList.get(index);
                remplirChamps(selectedAventurier);
            }
        });
    }

    private void remplirChamps(Aventurier aventurier) {
        nomField.setText(aventurier.getNom());
        prenomField.setText(aventurier.getPrenom());
        emailField.setText(aventurier.getEmail());
        dateInscriptionPicker.setValue(aventurier.getDateInscription());
        statutField.setText(aventurier.getStatus());
        phoneNumberField.setText(aventurier.getPhoneNumber());
    }


    @FXML
    public void ajouterAventurier() {
        if (validerSaisie()) {
            Aventurier aventurier = new Aventurier(
                    nomField.getText(),
                    prenomField.getText(),
                    emailField.getText(),
                    dateInscriptionPicker.getValue(),
                    statutField.getText(),
                    phoneNumberField.getText()
            );

            aventurierService.addAventurier(aventurier);
            clearFields();
            afficherAventuriers();
        }
    }

    @FXML
    public void modifierAventurier() {
        if (selectedAventurier != null && validerSaisie()) {
            selectedAventurier.setNom(nomField.getText());
            selectedAventurier.setPrenom(prenomField.getText());
            selectedAventurier.setEmail(emailField.getText());
            selectedAventurier.setDateInscription(dateInscriptionPicker.getValue());
            selectedAventurier.setStatus(statutField.getText());
            selectedAventurier.setPhoneNumber(phoneNumberField.getText());

            aventurierService.updateAventurier(selectedAventurier);
            clearFields();
            afficherAventuriers();
        }
    }

    @FXML
    public void supprimerAventurier() {
        if (selectedAventurier != null) {
            aventurierService.deleteAventurier(selectedAventurier.getId());
            clearFields();
            afficherAventuriers();
        }
    }

    private void afficherAventuriers() {
        aventurierObservableList.setAll(aventurierService.getAllAventuriers());
        ObservableList<String> aventurierStringList = FXCollections.observableArrayList();

        for (Aventurier aventurier : aventurierObservableList) {
            aventurierStringList.add(aventurier.toString());
        }

        aventurierListView.setItems(aventurierStringList);
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        dateInscriptionPicker.setValue(null);
        statutField.clear();
        phoneNumberField.clear();
        selectedAventurier = null;
    }

    // Méthode de validation
    private boolean validerSaisie() {
        // Validation du nom et prénom
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty()) {
            showAlert("Erreur", "Le nom et le prénom sont obligatoires.");
            return false;
        }

        // Validation de l'email
        if (!isValidEmail(emailField.getText())) {
            showAlert("Erreur", "L'email n'est pas valide.");
            return false;
        }

        // Validation de la date d'inscription
        if (dateInscriptionPicker.getValue() == null || dateInscriptionPicker.getValue().isAfter(LocalDate.now())) {
            showAlert("Erreur", "La date d'inscription ne peut pas être dans le futur.");
            return false;
        }

        // Validation du numéro de téléphone
        if (!isValidPhoneNumber(phoneNumberField.getText())) {
            showAlert("Erreur", "Le numéro de téléphone est invalide.");
            return false;
        }

        // Validation du statut
        if (statutField.getText().isEmpty()) {
            showAlert("Erreur", "Le statut est obligatoire.");
            return false;
        }

        return true;
    }

    // Vérification de la validité de l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Vérification de la validité du numéro de téléphone (format basique, ex : 123-456-7890)
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\d{10}$";  // Exemple pour un numéro de téléphone à 10 chiffres
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // Affichage des messages d'alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onVoirExpeditionClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/expedition_view.fxml"));
            Parent root = fxmlLoader.load();

            // Utilise un composant existant comme nomField pour obtenir la scène
            Scene currentScene = nomField.getScene(); // Utiliser nomField au lieu de titreField
            currentScene.setRoot(root); // Remplacer le root de la scène actuelle

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue des expéditions.");
        }
    }



}