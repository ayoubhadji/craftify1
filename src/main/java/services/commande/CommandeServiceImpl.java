package services.commande;

import entity.Commande;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeServiceImpl implements CommandeService {
    private final Connection connection;

    public CommandeServiceImpl() {
        this.connection = MyDataBase.getInstance().getMyConnection();
    }

    @Override
    public List<Commande> getAllCommandes() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setId(rs.getInt("id"));
                commande.setIdClient(rs.getInt("id_client_id"));
                commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
                commande.setStatut(rs.getString("statut"));
                commande.setTotal(rs.getDouble("total"));
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }

    @Override
    public List<Commande> getAll() {
        return getAllCommandes(); // Réutiliser la méthode existante
    }

    @Override
    public void save(Commande commande) {
        String sql = "INSERT INTO commande (id_client_id, date_commande, statut, total) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, commande.getIdClient());
            pstmt.setTimestamp(2, Timestamp.valueOf(commande.getDateCommande()));
            pstmt.setString(3, commande.getStatut());
            pstmt.setDouble(4, commande.getTotal());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Commande commande) {
        String sql = "UPDATE commande SET id_client_id = ?, date_commande = ?, statut = ?, total = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, commande.getIdClient());
            pstmt.setTimestamp(2, Timestamp.valueOf(commande.getDateCommande()));
            pstmt.setString(3, commande.getStatut());
            pstmt.setDouble(4, commande.getTotal());
            pstmt.setInt(5, commande.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM commande WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
