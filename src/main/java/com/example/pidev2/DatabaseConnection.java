package com.example.pidev2;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/craftify";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie à la base de données !");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur de connexion à la base de données.");
        }
        return connection;
    }
}