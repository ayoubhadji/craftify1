package tn.esprit.services;

import tn.esprit.entities.Evenement;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement {
    private Connection connection;

    public ServiceEvenement() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    public void ajouter(Evenement evenement) {
        String query = "INSERT INTO evenement (nom, description, date_debut, date_fin, lieu, capacite, type_evenement, prix, organisateur, image, date_creation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, evenement.getNom());
            statement.setString(2, evenement.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(evenement.getDateDebut()));
            statement.setTimestamp(4, Timestamp.valueOf(evenement.getDateFin()));
            statement.setString(5, evenement.getLieu());
            statement.setInt(6, evenement.getCapacite());
            statement.setString(7, evenement.getTypeEvenement());
            statement.setDouble(8, evenement.getPrix());
            statement.setString(9, evenement.getOrganisateur());
            statement.setString(10, evenement.getImage());
            statement.setTimestamp(11, Timestamp.valueOf(evenement.getDateCreation()));
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'événement: " + e.getMessage());
        }
    }

    public void modifier(Evenement evenement) {
        String query = "UPDATE evenement SET nom=?, description=?, date_debut=?, date_fin=?, lieu=?, capacite=?, type_evenement=?, prix=?, organisateur=?, image=?, date_creation=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, evenement.getNom());
            statement.setString(2, evenement.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(evenement.getDateDebut()));
            statement.setTimestamp(4, Timestamp.valueOf(evenement.getDateFin()));
            statement.setString(5, evenement.getLieu());
            statement.setInt(6, evenement.getCapacite());
            statement.setString(7, evenement.getTypeEvenement());
            statement.setDouble(8, evenement.getPrix());
            statement.setString(9, evenement.getOrganisateur());
            statement.setString(10, evenement.getImage());
            statement.setTimestamp(11, Timestamp.valueOf(evenement.getDateCreation()));
            statement.setInt(12, evenement.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'événement: " + e.getMessage());
        }
    }

    public void supprimer(int id) {
        String query = "DELETE FROM evenement WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'événement: " + e.getMessage());
        }
    }

    public List<Evenement> afficher() {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Evenement evenement = new Evenement();
                evenement.setId(resultSet.getInt("id"));
                evenement.setNom(resultSet.getString("nom"));
                evenement.setDescription(resultSet.getString("description"));
                evenement.setDateDebut(resultSet.getTimestamp("date_debut").toLocalDateTime());
                evenement.setDateFin(resultSet.getTimestamp("date_fin").toLocalDateTime());
                evenement.setLieu(resultSet.getString("lieu"));
                evenement.setCapacite(resultSet.getInt("capacite"));
                evenement.setTypeEvenement(resultSet.getString("type_evenement"));
                evenement.setPrix(resultSet.getDouble("prix"));
                evenement.setOrganisateur(resultSet.getString("organisateur"));
                evenement.setImage(resultSet.getString("image"));
                evenement.setDateCreation(resultSet.getTimestamp("date_creation").toLocalDateTime());
                evenements.add(evenement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des événements: " + e.getMessage());
        }
        return evenements;
    }

    public Evenement getById(int id) {
        String query = "SELECT * FROM evenement WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Evenement evenement = new Evenement();
                    evenement.setId(resultSet.getInt("id"));
                    evenement.setNom(resultSet.getString("nom"));
                    evenement.setDescription(resultSet.getString("description"));
                    evenement.setDateDebut(resultSet.getTimestamp("date_debut").toLocalDateTime());
                    evenement.setDateFin(resultSet.getTimestamp("date_fin").toLocalDateTime());
                    evenement.setLieu(resultSet.getString("lieu"));
                    evenement.setCapacite(resultSet.getInt("capacite"));
                    evenement.setTypeEvenement(resultSet.getString("type_evenement"));
                    evenement.setPrix(resultSet.getDouble("prix"));
                    evenement.setOrganisateur(resultSet.getString("organisateur"));
                    evenement.setImage(resultSet.getString("image"));
                    evenement.setDateCreation(resultSet.getTimestamp("date_creation").toLocalDateTime());
                    return evenement;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'événement: " + e.getMessage());
        }
        return null;
    }
}