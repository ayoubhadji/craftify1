package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import tn.esprit.entities.Evenement;

public class DetailsEvenementController {
    @FXML
    private Label nomLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label dateDebutLabel;

    @FXML
    private Label dateFinLabel;

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

    @FXML
    private ImageView imageView;

    public void setEvenement(Evenement evenement) {
        nomLabel.setText(evenement.getNom());
        descriptionLabel.setText(evenement.getDescription());
        dateDebutLabel.setText("Début: " + evenement.getDateDebut().toLocalDate());
        dateFinLabel.setText("Fin: " + evenement.getDateFin().toLocalDate());
        lieuLabel.setText("Lieu: " + evenement.getLieu());
        capaciteLabel.setText("Capacité: " + evenement.getCapacite());
        typeLabel.setText("Type: " + evenement.getTypeEvenement());
        prixLabel.setText("Prix: " + evenement.getPrix() + " DT");
        organisateurLabel.setText("Organisateur: " + evenement.getOrganisateur());

        // TODO: Load image if available
        // For now, we'll just hide the ImageView if there's no image
        imageView.setVisible(false);
    }
}