package controllers.events;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import entity.Evenement;
import javafx.scene.image.Image;


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
                imageView.setVisible(true);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
                imageView.setVisible(false);
            }
        } else {
            imageView.setVisible(false);
        }
    }
} 