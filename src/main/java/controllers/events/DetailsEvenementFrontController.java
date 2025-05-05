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

public class DetailsEvenementFrontController {
    @FXML
    private ImageView imageView;
    @FXML
    private Label titleLabel;
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

    private Evenement evenement;

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
        titleLabel.setText(evenement.getNom());
        descriptionLabel.setText(evenement.getDescription());
        dateDebutLabel.setText("Début: " + evenement.getDateDebut().toLocalDate());
        dateFinLabel.setText("Fin: " + evenement.getDateFin().toLocalDate());
        lieuLabel.setText("Lieu: " + evenement.getLieu());
        capaciteLabel.setText("Capacité: " + evenement.getCapacite() + " personnes");
        typeLabel.setText("Type: " + evenement.getTypeEvenement());
        prixLabel.setText(String.format("%.2f DT", evenement.getPrix()));
        organisateurLabel.setText("Organisé par: " + evenement.getOrganisateur());

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
    private void handleRetour() {
        Stage stage = (Stage) imageView.getScene().getWindow();
        stage.close();
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