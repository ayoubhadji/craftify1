package controllers.events;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import entity.Evenement;

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
        nomLabel.setText(evenement.getNom());
        descriptionLabel.setText(evenement.getDescription());
        dateLabel.setText("Du " + evenement.getDateDebut() + " au " + evenement.getDateFin());
        lieuLabel.setText("Lieu: " + evenement.getLieu());
        capaciteLabel.setText("Capacité: " + evenement.getCapacite() + " personnes");
        typeLabel.setText("Type: " + evenement.getTypeEvenement());
        prixLabel.setText("Prix: " + evenement.getPrix() + " DT");
        organisateurLabel.setText("Organisé par: " + evenement.getOrganisateur());

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

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) imageView.getScene().getWindow();
        stage.close();
    }
} 