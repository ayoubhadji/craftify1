package services;

import models.foire;
import services.Crud;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoireS implements Crud<foire> {

    private final Connection cnx;

    public FoireS() {
        cnx = MyDb.getInstance().getConn();
    }

    @Override
    public void create(foire f) throws Exception {
        String req = "INSERT INTO foire (nom, description, date_debut, date_fin, lieu, capacite_max, created_at, prix, rate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, f.getNom());
            pst.setString(2, f.getDescription());
            pst.setTimestamp(3, Timestamp.valueOf(f.getDate_debut()));
            pst.setTimestamp(4, Timestamp.valueOf(f.getDate_fin()));
            pst.setString(5, f.getLieu());
            pst.setInt(6, f.getCapacite_max());
            pst.setTimestamp(7, Timestamp.valueOf(f.getCreated_at()));
            pst.setDouble(8, f.getPrix());
            pst.setDouble(9, f.getRate());

            pst.executeUpdate();
        }
    }

    @Override
    public void update(foire f) throws Exception {
        String req = "UPDATE foire SET nom=?, description=?, date_debut=?, date_fin=?, lieu=?, capacite_max=?, created_at=?, prix=?, rate=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, f.getNom());
            pst.setString(2, f.getDescription());
            pst.setTimestamp(3, Timestamp.valueOf(f.getDate_debut()));
            pst.setTimestamp(4, Timestamp.valueOf(f.getDate_fin()));
            pst.setString(5, f.getLieu());
            pst.setInt(6, f.getCapacite_max());
            pst.setTimestamp(7, Timestamp.valueOf(f.getCreated_at()));
            pst.setDouble(8, f.getPrix());
            pst.setDouble(9, f.getRate());
            pst.setInt(10, f.getId());

            pst.executeUpdate();
        }
    }

    @Override
    public void delete(foire f) throws Exception {
        String req = "DELETE FROM foire WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, f.getId());
            pst.executeUpdate();
        }
    }
    @Override
    public List<foire> getAll() {
        List<foire> list = new ArrayList<>();
        String req = "SELECT * FROM foire";
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(req)) {

            while (rs.next()) {
                foire f = new foire();
                f.setId(rs.getInt("id"));
                f.setNom(rs.getString("nom"));
                f.setDescription(rs.getString("description"));
                f.setDate_debut(rs.getTimestamp("date_debut").toLocalDateTime());
                f.setDate_fin(rs.getTimestamp("date_fin").toLocalDateTime());
                f.setLieu(rs.getString("lieu"));
                f.setCapacite_max(rs.getInt("capacite_max"));
                f.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                f.setPrix(rs.getDouble("prix"));
                f.setRate(rs.getDouble("rate"));

                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ou loggue le message d’erreur
        }
        return list;
    }


    public foire readById(int id) throws Exception {
        String req = "SELECT * FROM foire WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    foire f = new foire();
                    f.setId(rs.getInt("id"));
                    f.setNom(rs.getString("nom"));
                    f.setDescription(rs.getString("description"));
                    f.setDate_debut(rs.getTimestamp("date_debut").toLocalDateTime());
                    f.setDate_fin(rs.getTimestamp("date_fin").toLocalDateTime());
                    f.setLieu(rs.getString("lieu"));
                    f.setCapacite_max(rs.getInt("capacite_max"));
                    f.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                    f.setPrix(rs.getDouble("prix"));
                    f.setRate(rs.getDouble("rate"));
                    return f;
                }
            }
        }
        return null;
    }
}
