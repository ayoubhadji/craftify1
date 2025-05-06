package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private final String URL = "jdbc:mysql://localhost:3306/craftify";
    private final String USER = "root";
    private final String PSW = ""; // TODO: Mettre à jour avec le mot de passe correct

    private Connection myConnection;

    private static MyDataBase instance;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Erreur lors du chargement du driver MySQL : " + e.getMessage());
        }
    }

    private MyDataBase() {
        try {
            myConnection = DriverManager.getConnection(URL, USER, PSW);
            System.out.println("✅ Connexion à la base de données établie avec succès");
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public Connection getMyConnection() {
        return myConnection;
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            synchronized (MyDataBase.class) {
                if (instance == null) {
                    instance = new MyDataBase();
                }
            }
        }
        return instance;
    }
}