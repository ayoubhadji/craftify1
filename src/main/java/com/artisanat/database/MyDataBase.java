package com.artisanat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDataBase {
    private static final String URL = "jdbc:h2:./data/artisanat;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver H2 non trouvé", e);
            }
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Création de la table produits
            String createTableSQL = "CREATE TABLE IF NOT EXISTS produits (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nom VARCHAR(255) NOT NULL, " +
                    "description TEXT, " +
                    "prix DOUBLE NOT NULL, " +
                    "stock INT NOT NULL, " +
                    "date_creation TIMESTAMP NOT NULL, " +
                    "date_modification TIMESTAMP NOT NULL, " +
                    "image_url VARCHAR(255)" +
                    ")";
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} 