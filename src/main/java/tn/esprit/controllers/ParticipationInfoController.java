package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.entities.Participation;
import tn.esprit.services.ServiceParticipation;

import java.net.URL;
import java.util.ResourceBundle;

public class ParticipationInfoController implements Initializable {

    @FXML
    private Label evenementLabel;

    @FXML
    private Label prixLabel;

    @FXML
    private Label statutLabel;

    private Participation participation;
    private ServiceParticipation serviceParticipation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceParticipation = new ServiceParticipation();
    }

    public void setParticipation(Participation participation) {
        this.participation = participation;
        updateLabels();
    }

    private void updateLabels() {
        if (participation != null) {
            evenementLabel.setText(participation.getEvenement().getNom());
            prixLabel.setText(String.valueOf(participation.getPrix()) + " DT");
            statutLabel.setText(participation.getStatut());
        }
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) evenementLabel.getScene().getWindow();
        stage.close();
    }
}