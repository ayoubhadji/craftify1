package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.foire;
import services.FoireS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UpdateFoireController {

    @FXML
    private TextField nom;

    @FXML
    private TextField desc;

    @FXML
    private DatePicker db;

    @FXML
    private DatePicker df;

    @FXML
    private TextField lieu;

    @FXML
    private TextField Cpm;

    @FXML
    private TextField prix;

    @FXML
    private TextField rated;

    @FXML
    private DatePicker Crat;

    @FXML
    private Button inssert;

    private final FoireS foireService = new FoireS();
    private int foireId;

    // Méthode appelée pour transmettre l'ID à modifier
    public void initData(int id) {
        this.foireId = id;
        try {
            foire f = foireService.readById(id);
            if (f != null) {
                nom.setText(f.getNom());
                desc.setText(f.getDescription());
                db.setValue(f.getDate_debut().toLocalDate());
                df.setValue(f.getDate_fin().toLocalDate());
                lieu.setText(f.getLieu());
                Cpm.setText(String.valueOf(f.getCapacite_max()));
                prix.setText(String.valueOf(f.getPrix()));
                rated.setText(String.valueOf(f.getRate()));
                Crat.setValue(f.getCreated_at().toLocalDate());
            } else {
                showAlert(Alert.AlertType.WARNING, "Foire non trouvée", "Aucune foire avec l'ID : " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement : " + e.getMessage());
        }
    }

    // Initialisation
    @FXML
    private void initialize() {
        inssert.setOnAction(event -> updateFoire());
    }

    private void updateFoire() {

        if (nom.getText().isEmpty() || desc.getText().isEmpty() || db.getValue() == null || df.getValue() == null ||
                lieu.getText().isEmpty() || Cpm.getText().isEmpty() || prix.getText().isEmpty() ||
                rated.getText().isEmpty() || Crat.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérification cohérence des dates
        if (df.getValue().isBefore(db.getValue())) {
            showAlert(Alert.AlertType.WARNING, "Dates invalides", "La date de fin doit être après la date de début.");
            return;
        }

        try {
            int capacite = Integer.parseInt(Cpm.getText());
            double prixVal = Double.parseDouble(prix.getText());
            double rateVal = Double.parseDouble(rated.getText());

            foire f = new foire();
            f.setId(foireId);
            f.setNom(nom.getText());
            f.setDescription(desc.getText());
            f.setDate_debut(convertToDateTime(db.getValue()));
            f.setDate_fin(convertToDateTime(df.getValue()));
            f.setLieu(lieu.getText());
            f.setCapacite_max(capacite);
            f.setPrix(prixVal);
            f.setRate(rateVal);
            f.setCreated_at(convertToDateTime(Crat.getValue()));

            foireService.update(f);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Foire mise à jour avec succès !");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Capacité, prix et rate doivent être des nombres valides.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    private LocalDateTime convertToDateTime(LocalDate date) {
        return date != null ? LocalDateTime.of(date, LocalTime.MIDNIGHT) : null;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
