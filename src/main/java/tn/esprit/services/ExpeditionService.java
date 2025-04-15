package tn.esprit.services;

import tn.esprit.entities.Expedition;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class ExpeditionService {
    private final Connection connection;

    public ExpeditionService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    // CREATE
    public void addExpedition(Expedition expedition) {
        String query = "INSERT INTO expedition (titre, objectif, date_debut, date_fin, aventurier_id, video_url) VALUES (?, ?, ?, ?, ?, ?)";

        if (connection == null) {
            System.err.println("❌ Impossible d'ajouter l'expédition : connexion non établie.");
            return;
        }

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, expedition.getTitre());
            pst.setString(2, expedition.getObjectif());
            pst.setDate(3, Date.valueOf(expedition.getDateDebut()));
            pst.setDate(4, Date.valueOf(expedition.getDateFin()));
            pst.setInt(5, expedition.getAventurierId());
            pst.setString(6, expedition.getVideoUrl());

            pst.executeUpdate();
            System.out.println("✅ Expédition ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'expédition : " + e.getMessage());
        }
    }

    // READ
    public List<Expedition> getAllExpeditions() {
        List<Expedition> expeditions = new ArrayList<>();
        String query = "SELECT * FROM expedition";

        if (connection == null) {
            System.err.println("❌ Impossible de récupérer les expéditions : connexion non établie.");
            return expeditions;
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Expedition expedition = new Expedition(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("objectif"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getInt("aventurier_id"),
                        rs.getString("video_url")
                );
                expeditions.add(expedition);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des expéditions : " + e.getMessage());
        }

        return expeditions;
    }

    // UPDATE
    public void updateExpedition(Expedition expedition) {
        String query = "UPDATE expedition SET titre = ?, objectif = ?, date_debut = ?, date_fin = ?, aventurier_id = ?, video_url = ? WHERE id = ?";

        if (connection == null) {
            System.err.println("❌ Impossible de mettre à jour l'expédition : connexion non établie.");
            return;
        }

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, expedition.getTitre());
            pst.setString(2, expedition.getObjectif());
            pst.setDate(3, Date.valueOf(expedition.getDateDebut()));
            pst.setDate(4, Date.valueOf(expedition.getDateFin()));
            pst.setInt(5, expedition.getAventurierId());
            pst.setString(6, expedition.getVideoUrl());
            pst.setInt(7, expedition.getId());

            pst.executeUpdate();
            System.out.println("✅ Expédition mise à jour avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de l'expédition : " + e.getMessage());
        }
    }

    // DELETE
    public void deleteExpedition(int id) {
        String query = "DELETE FROM expedition WHERE id = ?";

        if (connection == null) {
            System.err.println("❌ Impossible de supprimer l'expédition : connexion non établie.");
            return;
        }

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("✅ Expédition supprimée avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'expédition : " + e.getMessage());
        }
    }

    // FIND BY ID
    public Expedition getExpeditionById(int id) {
        String query = "SELECT * FROM expedition WHERE id = ?";
        Expedition expedition = null;

        if (connection == null) {
            System.err.println("❌ Impossible de rechercher l'expédition : connexion non établie.");
            return null;
        }

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                expedition = new Expedition(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("objectif"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getInt("aventurier_id"),
                        rs.getString("video_url")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche de l'expédition : " + e.getMessage());
        }

        return expedition;
    }
}
