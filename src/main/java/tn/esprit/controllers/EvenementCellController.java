package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import tn.esprit.entities.Evenement;

public class EvenementCellController {
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
    private Label prixLabel;

    public void setEvenement(Evenement evenement) {
        nomLabel.setText(evenement.getNom());
        descriptionLabel.setText(evenement.getDescription());
        dateLabel.setText("Du " + evenement.getDateDebut().toLocalDate() + " au " + evenement.getDateFin().toLocalDate());
        lieuLabel.setText("Lieu: " + evenement.getLieu());
        prixLabel.setText("Prix: " + evenement.getPrix() + " DT");

        // TODO: Load image if available
        // For now, we'll just hide the ImageView if there's no image
        imageView.setVisible(false);
    }
}