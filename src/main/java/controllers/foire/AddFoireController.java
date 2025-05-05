package controllers.foire;

import entity.Foire;
import services.foire.FoireService;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.time.LocalDate;

public class AddFoireController {

    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dateDebutField;
    @FXML private DatePicker dateFinField;
    @FXML private TextField lieuField;
    @FXML private TextField capaciteMaxField;
    @FXML private TextField prixField;
    @FXML private TextField rateField;
    @FXML private Button chooseOnMapButton;

    private final FoireService foireService = new FoireService();
    private Stage mapStage; // Store reference to the map window

    @FXML
    private void initialize() {
        chooseOnMapButton.setOnAction(e -> handleChooseOnMap());
    }

    @FXML
    private void handleSave(ActionEvent event) {
        // Field validation
        String nom = nomField.getText();
        String description = descriptionField.getText();
        LocalDate dateDebut = dateDebutField.getValue();
        LocalDate dateFin = dateFinField.getValue();
        String lieu = lieuField.getText();
        String capaciteMaxStr = capaciteMaxField.getText();
        String prixStr = prixField.getText();
        String rateStr = rateField.getText();

        if (nom.isEmpty() || description.isEmpty() || dateDebut == null || dateFin == null || lieu.isEmpty()
                || capaciteMaxStr.isEmpty() || prixStr.isEmpty() || rateStr.isEmpty()) {
            showAlert("Tous les champs doivent être remplis.");
            return;
        }

        int capaciteMax;
        double prix;
        double rate;
        try {
            capaciteMax = Integer.parseInt(capaciteMaxStr);
            prix = Double.parseDouble(prixStr);
            rate = Double.parseDouble(rateStr);
        } catch (NumberFormatException e) {
            showAlert("Capacité, Prix et Rate doivent être des nombres valides.");
            return;
        }

        Foire foire = new Foire();
        foire.setNom(nom);
        foire.setDescription(description);
        foire.setDateDebut(dateDebut);
        foire.setDateFin(dateFin);
        foire.setLieu(lieu);
        foire.setCapaciteMax(capaciteMax);
        foire.setPrix(prix);
        foire.setRate(rate);

        foireService.addFoireItem(foire);

        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void handleChooseOnMap() {
        mapStage = new Stage();
        mapStage.setTitle("Choisir un lieu sur la carte");
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", this); // Pass this controller as the bridge
            }
        });

        webEngine.load(getClass().getResource("/org.example/foire/mapChooser.html").toExternalForm());

        Scene scene = new Scene(webView, 700, 500);
        mapStage.setScene(scene);
        mapStage.initModality(Modality.APPLICATION_MODAL);
        mapStage.showAndWait();
    }

    // Called from JS: javaConnector.setAddress(address)
    public void setAddress(String address) {
        javafx.application.Platform.runLater(() -> {
            lieuField.setText(address);
            if (mapStage != null) {
                mapStage.close();
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
