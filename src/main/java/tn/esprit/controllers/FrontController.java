package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import tn.esprit.entities.Expedition;

public class FrontController {

    @FXML
    private Label titreLabel;
    @FXML
    private Label dateDebutLabel;
    @FXML
    private Label dateFinLabel;
    @FXML
    private Label videoUrlLabel;
    @FXML
    private Button participerButton;

    private Expedition expedition;

    public void setExpedition(Expedition expedition) {
        this.expedition = expedition;
        titreLabel.setText(expedition.getTitre());
        dateDebutLabel.setText("Début : " + expedition.getDateDebut());
        dateFinLabel.setText("Fin : " + expedition.getDateFin());
        videoUrlLabel.setText("Vidéo : " + expedition.getVideoUrl());
    }

    @FXML
    private void handleParticiper() {
        System.out.println("Participation à : " + expedition.getTitre());
        // Ici, tu peux ouvrir une page ou enregistrer la participation en base
    }
}
