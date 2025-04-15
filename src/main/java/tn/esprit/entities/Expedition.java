package tn.esprit.entities;

import java.time.LocalDate;

/**
 * Représente une expédition associée à un aventurier.
 */
public class Expedition {
    private int id;
    private String titre;
    private String objectif;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int aventurierId;
    private String videoUrl;

    // Constructeur vide (utile pour certaines opérations JavaFX ou ORM)
    public Expedition() {}

    // Constructeur sans ID (utilisé pour la création d’une nouvelle expédition)
    public Expedition(String titre, String objectif, LocalDate dateDebut, LocalDate dateFin, int aventurierId, String videoUrl) {
        this.titre = titre;
        this.objectif = objectif;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.aventurierId = aventurierId;
        this.videoUrl = videoUrl;
    }

    // Constructeur avec ID (utilisé pour les objets récupérés de la base)
    public Expedition(int id, String titre, String objectif, LocalDate dateDebut, LocalDate dateFin, int aventurierId, String videoUrl) {
        this(titre, objectif, dateDebut, dateFin, aventurierId, videoUrl);
        this.id = id;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getAventurierId() {
        return aventurierId;
    }

    public void setAventurierId(int aventurierId) {
        this.aventurierId = aventurierId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "Expédition : " + titre +
                "\nObjectif : " + objectif +
                "\nDate Début : " + dateDebut +
                "\nDate Fin : " + dateFin +
                "\nAventurier ID : " + aventurierId +
                "\nVidéo : " + videoUrl;
    }
}
