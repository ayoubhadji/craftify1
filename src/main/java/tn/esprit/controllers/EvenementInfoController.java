package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;

public class EvenementInfoController {
    @FXML
    private ImageView imageView;
    @FXML
    private Label nomLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label lieuLabel;
    @FXML
    private Label capaciteLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label prixLabel;
    @FXML
    private Label organisateurLabel;

    private Evenement evenement;

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
        if (evenement.getImage() != null && !evenement.getImage().isEmpty()) {
            imageView.setImage(new Image(evenement.getImage()));
        }
        nomLabel.setText(evenement.getNom());
        descriptionLabel.setText(evenement.getDescription());
        dateLabel.setText("Du " + evenement.getDateDebut() + " au " + evenement.getDateFin());
        lieuLabel.setText("Lieu: " + evenement.getLieu());
        capaciteLabel.setText("Capacité: " + evenement.getCapacite() + " personnes");
        typeLabel.setText("Type: " + evenement.getTypeEvenement());
        prixLabel.setText("Prix: " + evenement.getPrix() + " DT");
        organisateurLabel.setText("Organisé par: " + evenement.getOrganisateur());
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) imageView.getScene().getWindow();
        stage.close();
    }
}