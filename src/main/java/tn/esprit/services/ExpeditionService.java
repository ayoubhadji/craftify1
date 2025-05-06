package tn.esprit.services;

import tn.esprit.entities.Expedition;
import tn.esprit.entities.Aventurier;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpeditionService {
    private Connection connection;

    public ExpeditionService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    /**
     * Ajoute une expédition à la base de données. Si la liste des aventuriers
     * n'est pas vide, elle ajoute les associations dans la table de liaison.
     *
     * @param e L'expédition à ajouter.
     * @throws SQLException Si une erreur SQL survient.
     */
    public void addExpedition(Expedition e) throws SQLException {
        String sql = "INSERT INTO expedition (titre, objectif, date_debut, date_fin, video_url) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getTitre());
            ps.setString(2, e.getObjectif());
            ps.setDate(3, Date.valueOf(e.getDateDebut()));
            ps.setDate(4, Date.valueOf(e.getDateFin()));
            ps.setString(5, e.getVideoUrl());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int expeditionId = rs.getInt(1);

                    // Vérification si la liste des aventuriers n'est pas vide
                    if (e.getAventuriers() != null && !e.getAventuriers().isEmpty()) {
                        for (Aventurier a : e.getAventuriers()) {
                            linkAventurier(expeditionId, a.getId());
                        }
                    }
                }
            }
        }
    }

    /**
     * Supprime une expédition de la base de données.
     *
     * @param id L'ID de l'expédition à supprimer.
     * @throws SQLException Si une erreur SQL survient.
     */
    public void deleteExpedition(int id) throws SQLException {
        String sql = "DELETE FROM expedition_aventurier WHERE expedition_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

            sql = "DELETE FROM expedition WHERE id = ?";
            try (PreparedStatement ps2 = connection.prepareStatement(sql)) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }
        }
    }

    /**
     * Récupère toutes les expéditions.
     *
     * @return Une liste d'expéditions.
     * @throws SQLException Si une erreur SQL survient.
     */
    public List<Expedition> getAllExpeditions() throws SQLException {
        List<Expedition> list = new ArrayList<>();
        String sql = "SELECT * FROM expedition";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Expedition e = new Expedition();
                e.setId(rs.getInt("id"));
                e.setTitre(rs.getString("titre"));
                e.setObjectif(rs.getString("objectif"));
                e.setDateDebut(rs.getDate("date_debut").toLocalDate());
                e.setDateFin(rs.getDate("date_fin").toLocalDate());
                e.setVideoUrl(rs.getString("video_url"));
                e.setAventuriers(getAventuriersForExpedition(e.getId()));
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Récupère les aventuriers associés à une expédition.
     *
     * @param expeditionId L'ID de l'expédition.
     * @return Une liste d'aventuriers.
     * @throws SQLException Si une erreur SQL survient.
     */
    private List<Aventurier> getAventuriersForExpedition(int expeditionId) throws SQLException {
        List<Aventurier> list = new ArrayList<>();
        String sql = "SELECT a.id, a.nom FROM aventurier a JOIN expedition_aventurier ea ON a.id = ea.aventurier_id WHERE ea.expedition_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, expeditionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Aventurier(rs.getInt("id"), rs.getString("nom")));
                }
            }
        }
        return list;
    }

    /**
     * Ajoute un lien entre une expédition et un aventurier.
     *
     * @param expeditionId L'ID de l'expédition.
     * @param aventurierId L'ID de l'aventurier.
     * @throws SQLException Si une erreur SQL survient.
     */
    public void linkAventurier(int expeditionId, int aventurierId) throws SQLException {
        String sql = "INSERT INTO expedition_aventurier (expedition_id, aventurier_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, expeditionId);
            ps.setInt(2, aventurierId);
            ps.executeUpdate();
        }
    }

    /**
     * Met à jour une expédition dans la base de données.
     * Met à jour l'expédition et réassocie les aventuriers à l'expédition.
     *
     * @param e L'expédition mise à jour.
     * @throws SQLException Si une erreur SQL survient.
     */
    public void updateExpedition(Expedition e) throws SQLException {
        // Mise à jour des champs de l'expédition
        String sql = "UPDATE expedition SET titre = ?, objectif = ?, date_debut = ?, date_fin = ?, video_url = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getTitre());
            ps.setString(2, e.getObjectif());
            ps.setDate(3, Date.valueOf(e.getDateDebut()));
            ps.setDate(4, Date.valueOf(e.getDateFin()));
            ps.setString(5, e.getVideoUrl());
            ps.setInt(6, e.getId());
            ps.executeUpdate();

            // Suppression des anciennes associations avec les aventuriers
            sql = "DELETE FROM expedition_aventurier WHERE expedition_id = ?";
            try (PreparedStatement ps2 = connection.prepareStatement(sql)) {
                ps2.setInt(1, e.getId());
                ps2.executeUpdate();
            }

            // Ajout des nouvelles associations
            if (e.getAventuriers() != null && !e.getAventuriers().isEmpty()) {
                for (Aventurier a : e.getAventuriers()) {
                    linkAventurier(e.getId(), a.getId());
                }
            }
        }
    }
}
