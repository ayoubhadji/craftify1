package com.example.pidev2.controllers;

import com.example.pidev2.models.Foire;
import com.example.pidev2.service.FoireService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class ViewFoire2Controller {
    @FXML
    private ListView<Foire> foireListView;

    private final FoireService foireService = new FoireService();
    private final ObservableList<Foire> foires = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        foires.setAll(foireService.getAllFoires());
        foireListView.setItems(foires);
        foireListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Foire foire, boolean empty) {
                super.updateItem(foire, empty);
                if (empty || foire == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox box = new VBox(6);
                    box.setPadding(new Insets(10, 18, 10, 18));
                    box.getStyleClass().add("list-cell");

                    Label nom = new Label();
                    nom.getStyleClass().add("card-title");
                    Label description = new Label();
                    description.getStyleClass().add("card-label");
                    Label lieu = new Label();
                    lieu.getStyleClass().add("card-value");
                    Label dateDebut = new Label();
                    dateDebut.getStyleClass().add("card-value");
                    Label dateFin = new Label();
                    dateFin.getStyleClass().add("card-value");
                    Label capaciteMax = new Label();
                    capaciteMax.getStyleClass().add("card-value");
                    Label prix = new Label();
                    prix.getStyleClass().add("card-value");
                    Label rate = new Label();
                    rate.getStyleClass().add("card-value");

                    // Try getters, fallback to fields
                    String nomText = "";
                    String descText = "";
                    String lieuText = "";
                    String dateDebutText = "";
                    String dateFinText = "";
                    String capaciteMaxText = "";
                    String prixText = "";
                    String rateText = "";
                    try { nomText = String.valueOf(foire.getClass().getMethod("getNom").invoke(foire)); } catch (Exception ignored) {}
                    try { descText = String.valueOf(foire.getClass().getMethod("getDescription").invoke(foire)); } catch (Exception ignored) {}
                    try { lieuText = String.valueOf(foire.getClass().getMethod("getLieu").invoke(foire)); } catch (Exception ignored) {}
                    try { dateDebutText = String.valueOf(foire.getClass().getMethod("getDateDebut").invoke(foire)); } catch (Exception ignored) {}
                    try { dateFinText = String.valueOf(foire.getClass().getMethod("getDateFin").invoke(foire)); } catch (Exception ignored) {}
                    try { capaciteMaxText = String.valueOf(foire.getClass().getMethod("getCapaciteMax").invoke(foire)); } catch (Exception ignored) {}
                    try { prixText = String.valueOf(foire.getClass().getMethod("getPrix").invoke(foire)); } catch (Exception ignored) {}
                    try { rateText = String.valueOf(foire.getClass().getMethod("getRate").invoke(foire)); } catch (Exception ignored) {}
                    if (nomText.isEmpty()) {
                        try { nomText = String.valueOf(foire.getClass().getField("nom").get(foire)); } catch (Exception ignored) {}
                    }
                    if (descText.isEmpty()) {
                        try { descText = String.valueOf(foire.getClass().getField("description").get(foire)); } catch (Exception ignored) {}
                    }
                    if (lieuText.isEmpty()) {
                        try { lieuText = String.valueOf(foire.getClass().getField("lieu").get(foire)); } catch (Exception ignored) {}
                    }
                    if (dateDebutText.isEmpty()) {
                        try { dateDebutText = String.valueOf(foire.getClass().getField("dateDebut").get(foire)); } catch (Exception ignored) {}
                    }
                    if (dateFinText.isEmpty()) {
                        try { dateFinText = String.valueOf(foire.getClass().getField("dateFin").get(foire)); } catch (Exception ignored) {}
                    }
                    if (capaciteMaxText.isEmpty()) {
                        try { capaciteMaxText = String.valueOf(foire.getClass().getField("capaciteMax").get(foire)); } catch (Exception ignored) {}
                    }
                    if (prixText.isEmpty()) {
                        try { prixText = String.valueOf(foire.getClass().getField("prix").get(foire)); } catch (Exception ignored) {}
                    }
                    if (rateText.isEmpty()) {
                        try { rateText = String.valueOf(foire.getClass().getField("rate").get(foire)); } catch (Exception ignored) {}
                    }

                    nom.setText(nomText);
                    description.setText(descText);
                    lieu.setText("Lieu: " + lieuText);
                    dateDebut.setText("Date début: " + dateDebutText);
                    dateFin.setText("Date fin: " + dateFinText);
                    capaciteMax.setText("Capacité max: " + capaciteMaxText);
                    prix.setText("Prix: " + prixText);
                    rate.setText("Rate: " + rateText);

                    box.getChildren().addAll(nom, description, lieu, dateDebut, dateFin, capaciteMax, prix, rate);
                    setGraphic(box);
                    setText(null);
                }
            }
        });
    }
}
