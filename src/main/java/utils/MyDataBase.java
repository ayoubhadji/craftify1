package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private final String URL = "jdbc:mysql://localhost:3306/craftify";
    private final String USER = "root";
    private final String PSW = "";

    private Connection myConnection;

    private static MyDataBase instance;

    private MyDataBase() {
        try {
            myConnection = DriverManager.getConnection(URL, USER, PSW);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getMyConnection() {
        try {
            if (myConnection == null || myConnection.isClosed()) {
                myConnection = DriverManager.getConnection(URL, USER, PSW);
                System.out.println("Reconnected to database");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la reconnexion : " + e.getMessage());
        }
        return myConnection;
    }


    public static MyDataBase getInstance() {
        if (instance == null)
            instance = new MyDataBase();
        return instance;
    }
}