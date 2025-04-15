package com.artisanat.entities;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class Commande {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty clientId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dateCommande = new SimpleObjectProperty<>();
    private final StringProperty statut = new SimpleStringProperty();
    private final DoubleProperty montantTotal = new SimpleDoubleProperty();
    private final ObservableList<LigneCommande> lignesCommande = FXCollections.observableArrayList();

    public Commande() {
        this.dateCommande.set(LocalDate.now());
        this.statut.set("EN_ATTENTE");
        this.montantTotal.set(0.0);
        
        // Ajouter un listener pour recalculer le montant total
        lignesCommande.addListener((javafx.collections.ListChangeListener.Change<? extends LigneCommande> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
                    calculerMontantTotal();
                }
            }
        });
    }

    public Commande(int id, int clientId, LocalDate dateCommande, String statut) {
        this();
        this.id.set(id);
        this.clientId.set(clientId);
        if (dateCommande != null) {
            this.dateCommande.set(dateCommande);
        }
        this.statut.set(statut);
    }

    // Getters et Setters pour les propriétés
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getClientId() {
        return clientId.get();
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId.set(clientId);
    }

    public LocalDate getDateCommande() {
        return dateCommande.get();
    }

    public ObjectProperty<LocalDate> dateCommandeProperty() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        if (dateCommande == null) {
            throw new IllegalArgumentException("La date de commande ne peut pas être nulle");
        }
        this.dateCommande.set(dateCommande);
    }

    public String getStatut() {
        return statut.get();
    }

    public StringProperty statutProperty() {
        return statut;
    }

    public void setStatut(String statut) {
        if (statut == null || statut.trim().isEmpty()) {
            throw new IllegalArgumentException("Le statut ne peut pas être vide");
        }
        this.statut.set(statut);
    }

    public double getMontantTotal() {
        return montantTotal.get();
    }

    public DoubleProperty montantTotalProperty() {
        return montantTotal;
    }

    public ObservableList<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void ajouterLigneCommande(LigneCommande ligneCommande) {
        if (ligneCommande == null) {
            throw new IllegalArgumentException("La ligne de commande ne peut pas être nulle");
        }
        lignesCommande.add(ligneCommande);
        calculerMontantTotal();
    }

    public void supprimerLigneCommande(LigneCommande ligneCommande) {
        if (ligneCommande == null) {
            throw new IllegalArgumentException("La ligne de commande ne peut pas être nulle");
        }
        lignesCommande.remove(ligneCommande);
        calculerMontantTotal();
    }

    private void calculerMontantTotal() {
        double total = 0.0;
        for (LigneCommande ligne : lignesCommande) {
            total += ligne.getSousTotal();
        }
        montantTotal.set(total);
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id.get() +
                ", clientId=" + clientId.get() +
                ", dateCommande=" + dateCommande.get() +
                ", statut='" + statut.get() + '\'' +
                ", montantTotal=" + montantTotal.get() +
                ", lignesCommande=" + lignesCommande +
                '}';
    }
} 