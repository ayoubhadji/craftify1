package tn.esprit.services;

import tn.esprit.entities.Aventurier;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AventurierService {
    private final Connection connection;

    public AventurierService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    // CREATE
    public void addAventurier(Aventurier aventurier) {
        String query = "INSERT INTO aventurier (nom, prenom, email, date_inscription, statut, phone_number) VALUES (?, ?, ?, ?, ?, ?)";

        if (connection == null) {
            System.err.println("❌ Impossible d'ajouter l'aventurier : connexion non établie.");
            return;
        }

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, aventurier.getNom());
            pst.setString(2, aventurier.getPrenom());
            pst.setString(3, aventurier.getEmail());
            pst.setDate(4, Date.valueOf(aventurier.getDateInscription()));
            pst.setString(5, aventurier.getStatus());
            pst.setString(6, aventurier.getPhoneNumber());

            pst.executeUpdate();
            System.out.println("✅ Aventurier ajouté avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'aventurier : " + e.getMessage());
        }
    }

    // READ
    public List<Aventurier> getAllAventuriers() {
        List<Aventurier> aventuriers = new ArrayList<>();
        String query = "SELECT * FROM aventurier";

        if (connection == null) {
            System.err.println("❌ Impossible de récupérer les aventuriers : connexion non établie.");
            return aventuriers;
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Aventurier aventurier = new Aventurier(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getDate("date_inscription").toLocalDate(),
                        rs.getString("statut"),
                        rs.getString("phone_number")
                );
                aventuriers.add(aventurier);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des aventuriers : " + e.getMessage());
        }

        return aventuriers;
    }

    // UPDATE
    public void updateAventurier(Aventurier aventurier) {
        String query = "UPDATE aventurier SET nom = ?, prenom = ?, email = ?, date_inscription = ?, statut = ?, phone_number = ? WHERE id = ?";

        if (connection == null) {
            System.err.println("❌ Impossible de mettre à jour l'aventurier : connexion non établie.");
            return;
        }

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, aventurier.getNom());
            pst.setString(2, aventurier.getPrenom());
            pst.setString(3, aventurier.getEmail());
            pst.setDate(4, Date.valueOf(aventurier.getDateInscription()));
            pst.setString(5, aventurier.getStatus());
            pst.setString(6, aventurier.getPhoneNumber());
            pst.setInt(7, aventurier.getId());

            pst.executeUpdate();
            System.out.println("✅ Aventurier mis à jour avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de l'aventurier : " + e.getMessage());
        }
    }

    // DELETE
    public void deleteAventurier(int id) {
        String query = "DELETE FROM aventurier WHERE id = ?";

        if (connection == null) {
            System.err.println("❌ Impossible de supprimer l'aventurier : connexion non établie.");
            return;
        }

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("✅ Aventurier supprimé avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'aventurier : " + e.getMessage());
        }
    }

    // FIND BY ID
    public Aventurier getAventurierById(int id) {
        String query = "SELECT * FROM aventurier WHERE id = ?";
        Aventurier aventurier = null;

        if (connection == null) {
            System.err.println("❌ Impossible de rechercher l'aventurier : connexion non établie.");
            return null;
        }

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                aventurier = new Aventurier(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getDate("date_inscription").toLocalDate(),
                        rs.getString("statut"),
                        rs.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche de l'aventurier : " + e.getMessage());
        }

        return aventurier;
    }
}
