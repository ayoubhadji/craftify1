package DAO;

import entity.User;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private final Connection conn;
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    // Constructeur de UserDAO, initialise la connexion à la base de données
    public UserDAO() {
        conn = MyDataBase.getInstance().getMyConnection();
    }

    // Ajouter un utilisateur dans la base de données
    public boolean insert(User user) {
        String sql = "INSERT INTO user (nom, email, code, role, sexe, date_naissance, date_join, tel, address, fiscal, reset_token) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Vérification du nom avant insertion
            if (user.getNom() == null || user.getNom().isEmpty()) {
                throw new SQLException("Le nom ne peut pas être null ou vide.");
            }
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getCode());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getSexe());

            // Vérification de la date de naissance avant conversion
            if (user.getDateNaissance() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(user.getDateNaissance()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            // Vérification de la date d'adhésion avant conversion
            if (user.getDateJoin() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(user.getDateJoin()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            stmt.setString(8, user.getTel());
            stmt.setString(9, user.getAddress());
            stmt.setString(10, user.getFiscal());
            stmt.setString(11, user.getResetToken());

            return stmt.executeUpdate() > 0;  // Si l'insertion a réussi, retourne true
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'insertion de l'utilisateur : " + e.getMessage(), e);
            return false;  // Si une erreur se produit, retourne false
        }
    }

    // Mettre à jour un utilisateur dans la base de données
    public boolean update(User user) {
        String sql = "UPDATE user SET nom=?, email=?, code=?, role=?, sexe=?, date_naissance=?, date_join=?, tel=?, address=?, fiscal=?, reset_token=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getCode());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getSexe());

            // Vérification de la date de naissance avant conversion
            if (user.getDateNaissance() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(user.getDateNaissance()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            // Vérification de la date d'adhésion avant conversion
            if (user.getDateJoin() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(user.getDateJoin()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            stmt.setString(8, user.getTel());
            stmt.setString(9, user.getAddress());
            stmt.setString(10, user.getFiscal());
            stmt.setString(11, user.getResetToken());
            stmt.setInt(12, user.getId());  // ID de l'utilisateur à mettre à jour

            return stmt.executeUpdate() > 0;  // Si la mise à jour a réussi, retourne true
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage(), e);
            return false;  // Si une erreur se produit, retourne false
        }
    }

    // Supprimer un utilisateur par ID dans la base de données
    public boolean delete(int id) {
        String sql = "DELETE FROM user WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);  // ID de l'utilisateur à supprimer
            return stmt.executeUpdate() > 0;  // Si la suppression a réussi, retourne true
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la suppression de l'utilisateur : " + e.getMessage(), e);
            return false;  // Si une erreur se produit, retourne false
        }
    }

    // Récupérer un utilisateur par son ID
    public User getById(int id) {
        String sql = "SELECT * FROM user WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);  // Récupérer un utilisateur à partir du ResultSet
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération de l'utilisateur par ID : " + e.getMessage(), e);
        }
        return null;  // Si aucun utilisateur trouvé, retourne null
    }

    // Récupérer tous les utilisateurs
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractUserFromResultSet(rs));  // Ajouter chaque utilisateur à la liste
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des utilisateurs : " + e.getMessage(), e);
        }
        return list;  // Retourner la liste des utilisateurs
    }

    // Méthode utilitaire pour construire un objet User à partir du ResultSet
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setNom(rs.getString("nom"));
        user.setEmail(rs.getString("email"));
        user.setCode(rs.getString("code"));
        user.setRole(rs.getString("role"));
        user.setSexe(rs.getString("sexe"));
        user.setTel(rs.getString("tel"));
        user.setAddress(rs.getString("address"));
        user.setFiscal(rs.getString("fiscal"));
        user.setResetToken(rs.getString("reset_token"));

        Timestamp tsNaissance = rs.getTimestamp("date_naissance");
        Timestamp tsJoin = rs.getTimestamp("date_join");

        if (tsNaissance != null) {
            user.setDateNaissance(tsNaissance.toLocalDateTime());
        }

        if (tsJoin != null) {
            user.setDateJoin(tsJoin.toLocalDateTime());
        }

        return user;
    }

    public User login(String email, String code) {
        String sql = "SELECT * FROM user WHERE email = ? AND code = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, code); // ou password si tu l’as nommé ainsi

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);  // Utilisateur trouvé
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Aucun utilisateur trouvé
    }

    public boolean resetPasswordByToken(String token, String newPassword) {
        String sql = "UPDATE user SET code = ?, reset_token = NULL WHERE reset_token = ?";

        try (PreparedStatement stmt = MyDataBase.getInstance().getMyConnection().prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, token);
            return stmt.executeUpdate() > 0;  // Retourne true si l'update a réussi
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la réinitialisation du mot de passe : " + e.getMessage(), e);
            return false;
        }
    }
    public User getByResetToken(String token) {
        User user = null;

        try (Connection conn = MyDataBase.getInstance().getMyConnection()) {
            String sql = "SELECT * FROM user WHERE reset_token = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, token);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        user = new User();
                        user.setId(rs.getInt("id"));
                        user.setEmail(rs.getString("email"));
                        user.setNom(rs.getString("nom"));
                        // Ajoute les autres champs si besoin
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur par token : " + e.getMessage());
            e.printStackTrace();
        }

        return user;
    }


}