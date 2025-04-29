package com.example.pidev2.service;

import com.example.pidev2.models.Foire;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoireService {
    private final Connection connection;

    public FoireService() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/craftify", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public void addFoire(Foire foire) {
        String query = "INSERT INTO foire (nom, description, date_debut, date_fin, lieu, capacite_max, created_at, prix, rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, foire.getNom());
            stmt.setString(2, foire.getDescription());
            stmt.setDate(3, foire.getDateDebut() != null ? Date.valueOf(foire.getDateDebut()) : null);
            stmt.setDate(4, foire.getDateFin() != null ? Date.valueOf(foire.getDateFin()) : null);
            stmt.setString(5, foire.getLieu());
            stmt.setInt(6, foire.getCapaciteMax());
            stmt.setDate(7, Date.valueOf(LocalDate.now())); // set created_at to current date
            stmt.setDouble(8, foire.getPrix());
            stmt.setDouble(9, foire.getRate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Foire> getAllFoires() {
        List<Foire> foires = new ArrayList<>();
        String query = "SELECT * FROM foire";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Foire foire = new Foire();
                foire.setId(rs.getInt("id"));
                foire.setNom(rs.getString("nom"));
                foire.setDescription(rs.getString("description"));
                foire.setDateDebut(rs.getDate("date_debut") != null ? rs.getDate("date_debut").toLocalDate() : null);
                foire.setDateFin(rs.getDate("date_fin") != null ? rs.getDate("date_fin").toLocalDate() : null);
                foire.setLieu(rs.getString("lieu"));
                foire.setCapaciteMax(rs.getInt("capacite_max"));
                foire.setCreatedAt(rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null);
                foire.setPrix(rs.getDouble("prix"));
                foire.setRate(rs.getDouble("rate"));
                foires.add(foire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foires;
    }

    public void updateFoire(Foire foire) {
        String query = "UPDATE foire SET nom=?, description=?, date_debut=?, date_fin=?, lieu=?, capacite_max=?, prix=?, rate=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, foire.getNom());
            stmt.setString(2, foire.getDescription());
            stmt.setDate(3, foire.getDateDebut() != null ? Date.valueOf(foire.getDateDebut()) : null);
            stmt.setDate(4, foire.getDateFin() != null ? Date.valueOf(foire.getDateFin()) : null);
            stmt.setString(5, foire.getLieu());
            stmt.setInt(6, foire.getCapaciteMax());
            stmt.setDouble(7, foire.getPrix());
            stmt.setDouble(8, foire.getRate());
            stmt.setInt(9, foire.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFoire(int id) {
        String query = "DELETE FROM foire WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Controller compatibility methods ---

    public List<Foire> getAllFoireList() {
        return getAllFoires();
    }

    public void addFoireItem(Foire foire) {
        addFoire(foire);
    }

    public void updateFoireItem(Foire foire) {
        updateFoire(foire);
    }

    public void deleteFoireItem(int id) {
        deleteFoire(id);
    }
}
