package Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.foire;
import services.FoireS;

public class Cfoire {

    @FXML
    private TextField nom;

    @FXML
    private TextField desc;

    @FXML
    private DatePicker db;      // Date début

    @FXML
    private DatePicker df;      // Date fin

    @FXML
    private TextField lieu;

    @FXML
    private TextField Cpm;      // Capacité Max

    @FXML
    private DatePicker Crat;    // Created At

    @FXML
    private TextField prix;

    @FXML
    private TextField rated;

    @FXML
    private Button inssert;

    // Service pour manipuler les opérations sur la foire
    private FoireS foireService = new FoireS();

    @FXML
    public void initialize() {
        // Affectation de l'action sur le bouton "Insertion"
        inssert.setOnAction((ActionEvent event) -> {
            handleInsertion(event);
        });
    }

    private void handleInsertion(ActionEvent event) {
        try {
            // Récupération et conversion des valeurs saisies
            String nomText = nom.getText();
            String descText = desc.getText();
            String lieuText = lieu.getText();

            // Conversion des dates (en utilisant l'heure de début de journée)
            LocalDateTime dateDebut = convertToLocalDateTime(db.getValue());
            LocalDateTime dateFin = convertToLocalDateTime(df.getValue());
            LocalDateTime createdAt = convertToLocalDateTime(Crat.getValue());

            int capaciteMax = Integer.parseInt(Cpm.getText());
            double prixValue = Double.parseDouble(prix.getText());
            double rateValue = Double.parseDouble(rated.getText());

            // Création de l'objet foire (l'id peut être généré par la base de données si c'est un auto-increment)
            foire f = new foire(0, nomText, descText, dateDebut, dateFin, lieuText,
                    capaciteMax, createdAt, prixValue, rateValue);

            // Appel de la méthode create pour insérer la foire en base de données
            foireService.create(f);

            // Afficher une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Insertion");
            alert.setHeaderText("Insertion réussie");
            alert.setContentText("La foire a bien été insérée !");
            alert.showAndWait();

            // Vous pouvez ici réinitialiser les champs ou rediriger vers une autre vue si besoin

        } catch (Exception ex) {
            ex.printStackTrace();
            // Afficher une alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'insertion");
            alert.setHeaderText("Echec de l'insertion");
            alert.setContentText("Erreur: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Méthode utilitaire pour convertir une LocalDate en LocalDateTime à 00:00.
     */
    private LocalDateTime convertToLocalDateTime(LocalDate localDate) {
        if (localDate != null) {
            return LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        } else {
            throw new IllegalArgumentException("La date ne peut pas être null");
        }
    }
}
