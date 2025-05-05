package controllers.events;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import entity.Participation;

public class ParticipationCellController {
    @FXML
    private Label userLabel;
    @FXML
    private Label evenementLabel;
    @FXML
    private Label prixLabel;
    @FXML
    private Label statutLabel;

    private Participation participation;

    public void setParticipation(Participation participation) {
        this.participation = participation;
        updateLabels();
    }

    private void updateLabels() {
        if (participation != null) {
            userLabel.setText("User ID: " + participation.getUserId());
            evenementLabel.setText(participation.getEvenement().getNom());
            prixLabel.setText(String.format("%.2f", participation.getPrix()));
            statutLabel.setText(participation.getStatut());
        }
    }

    @FXML
    private void handleDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/DetailsParticipation.fxml"));
            Parent root = loader.load();
            DetailsParticipationController controller = loader.getController();
            controller.setParticipation(participation);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la participation");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 