package controllers.events;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import entity.Evenement;

import java.io.IOException;

public class EvenementCardFrontController {
    @FXML
    private ImageView imageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label lieuLabel;
    @FXML
    private Label prixLabel;

    private Evenement evenement;

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
        titleLabel.setText(evenement.getNom());
        descriptionLabel.setText(evenement.getDescription());
        dateLabel.setText("Du " + evenement.getDateDebut().toLocalDate() + " au " + evenement.getDateFin().toLocalDate());
        lieuLabel.setText("Lieu: " + evenement.getLieu());
        prixLabel.setText(String.format("%.2f DT", evenement.getPrix()));

        String imageUrl = evenement.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image;
                if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                    image = new Image(imageUrl);
                } else {
                    image = new Image(getClass().getResourceAsStream(imageUrl));
                }
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/DetailsEvenementFront.fxml"));
            Parent root = loader.load();
            
            DetailsEvenementFrontController controller = loader.getController();
            controller.setEvenement(evenement);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(evenement.getNom());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleParticipate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/AjouterParticipation.fxml"));
            Parent root = loader.load();
            
            AjouterParticipationController controller = loader.getController();
            controller.preloadEvenement(evenement);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Participer à " + evenement.getNom());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 