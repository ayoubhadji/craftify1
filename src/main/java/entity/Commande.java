package entity;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.List;

public class Commande {
    private final IntegerProperty id;
    private final ObjectProperty<LocalDateTime> dateCommande;
    private final StringProperty statut;
    private final DoubleProperty total;
    private final IntegerProperty idClient;

    public Commande() {
        this.id = new SimpleIntegerProperty();
        this.dateCommande = new SimpleObjectProperty<>();
        this.statut = new SimpleStringProperty();
        this.total = new SimpleDoubleProperty();
        this.idClient = new SimpleIntegerProperty();
    }

    // Getters et Setters pour id
    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }

    // Getters et Setters pour dateCommande
    public LocalDateTime getDateCommande() {
        return dateCommande.get();
    }
    public ObjectProperty<LocalDateTime> dateCommandeProperty() {
        return dateCommande;
    }
    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande.set(dateCommande);
    }

    // Getters et Setters pour statut
    public String getStatut() {
        return statut.get();
    }
    public StringProperty statutProperty() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    // Getters et Setters pour total
    public double getTotal() {
        return total.get();
    }
    public DoubleProperty totalProperty() {
        return total;
    }
    public void setTotal(double total) {
        this.total.set(total);
    }

    // Getters et Setters pour idClient
    public int getIdClient() {
        return idClient.get();
    }
    public IntegerProperty idClientProperty() {
        return idClient;
    }
    public void setIdClient(int idClient) {
        this.idClient.set(idClient);
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id.get() +
                ", dateCommande=" + dateCommande.get() +
                ", statut='" + statut.get() + '\'' +
                ", total=" + total.get() +
                ", idClient=" + idClient.get() +
                '}';
    }
} 