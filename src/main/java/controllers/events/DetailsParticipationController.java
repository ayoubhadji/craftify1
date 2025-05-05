package controllers.events;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import entity.Participation;

public class DetailsParticipationController {
    @FXML
    private Label idLabel;
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
            idLabel.setText(String.valueOf(participation.getId()));
            userLabel.setText("User ID: " + participation.getUserId());
            evenementLabel.setText(participation.getEvenement().getNom());
            prixLabel.setText(String.format("%.2f", participation.getPrix()));
            statutLabel.setText(participation.getStatut());
        }
    }

    @FXML
    private void handleFermer() {
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }
} 