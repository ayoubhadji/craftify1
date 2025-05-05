package services.events;

import entity.Evenement;
import entity.Participation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipation {
    private Connection connection;
    private ServiceEvenement serviceEvenement;

    public ServiceParticipation() {
        // Use your existing database connection
        try {
            // Replace with your actual database connection details
            String url = "jdbc:mysql://localhost:3306/craftify";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
            serviceEvenement = new ServiceEvenement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouter(Participation participation) {
        String query = "INSERT INTO participation (id_user_id, id_evenement_id, prix, statut) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, participation.getUserId());
            statement.setInt(2, participation.getEvenement().getId());
            statement.setDouble(3, participation.getPrix());
            statement.setString(4, participation.getStatut());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifier(Participation participation) {
        String query = "UPDATE participation SET id_user_id=?, id_evenement_id=?, prix=?, statut=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, participation.getUserId());
            statement.setInt(2, participation.getEvenement().getId());
            statement.setDouble(3, participation.getPrix());
            statement.setString(4, participation.getStatut());
            statement.setInt(5, participation.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String query = "DELETE FROM participation WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Participation getById(int id) {
        String query = "SELECT * FROM participation WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Participation participation = new Participation();
                participation.setId(resultSet.getInt("id"));
                participation.setUserId(resultSet.getInt("id_user_id"));
                
                // Load the event data
                int evenementId = resultSet.getInt("id_evenement_id");
                Evenement evenement = serviceEvenement.getById(evenementId);
                participation.setEvenement(evenement);
                
                participation.setPrix(resultSet.getDouble("prix"));
                participation.setStatut(resultSet.getString("statut"));
                return participation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Participation> afficher() {
        List<Participation> participations = new ArrayList<>();
        String query = "SELECT * FROM participation";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Participation participation = new Participation();
                participation.setId(resultSet.getInt("id"));
                participation.setUserId(resultSet.getInt("id_user_id"));
                
                // Load the event data
                int evenementId = resultSet.getInt("id_evenement_id");
                Evenement evenement = serviceEvenement.getById(evenementId);
                participation.setEvenement(evenement);
                
                participation.setPrix(resultSet.getDouble("prix"));
                participation.setStatut(resultSet.getString("statut"));
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    public List<Participation> rechercher(String searchTerm) {
        List<Participation> participations = new ArrayList<>();
        String query = "SELECT p.* FROM participation p " +
                      "JOIN evenement e ON p.id_evenement_id = e.id " +
                      "WHERE e.nom LIKE ? OR p.statut LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchTerm + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Participation participation = new Participation();
                participation.setId(resultSet.getInt("id"));
                participation.setUserId(resultSet.getInt("id_user_id"));
                
                // Load the event data
                int evenementId = resultSet.getInt("id_evenement_id");
                Evenement evenement = serviceEvenement.getById(evenementId);
                participation.setEvenement(evenement);
                
                participation.setPrix(resultSet.getDouble("prix"));
                participation.setStatut(resultSet.getString("statut"));
                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }
} 