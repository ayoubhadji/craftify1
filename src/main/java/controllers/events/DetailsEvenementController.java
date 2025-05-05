package controllers.events;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import entity.Evenement;
import javafx.scene.image.Image;


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

        String imageUrl = evenement.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image;
                if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                    // Load from URL
                    image = new Image(imageUrl);
                } else {
                    // Load from local resources
                    image = new Image(getClass().getResourceAsStream(imageUrl));
                }
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
            }
        }
    }
} 