package entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "participation")
public class Participation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_user_id")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "id_evenement_id")

    private Evenement evenement;

    private double prix;
    private String statut;

    // Constructors
    public Participation() {
    }

    public Participation(int userId, Evenement evenement, double prix, String statut) {
        this.userId = userId;
        this.evenement = evenement;
        this.prix = prix;
        this.statut = statut;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
} 