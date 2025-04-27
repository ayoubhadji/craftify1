module craftify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires jakarta.mail;
    requires jakarta.activation;
    requires java.net.http;
    requires org.json;
    requires stripe.java;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires nanohttpd;
    requires jdk.jsobject;

    opens controllers.commande to javafx.fxml;
    opens controllers.payment to javafx.fxml;
    opens controllers.user to javafx.fxml;
    opens controllers.produit to javafx.fxml;
    opens controllers to javafx.fxml;
    opens entity to javafx.base;
    opens tn.esprit to javafx.fxml;
    
    exports controllers.commande;
    exports controllers.payment;
    exports controllers.user;
    exports controllers.produit;
    exports controllers;
    exports entity;
    exports tn.esprit;
    exports services.payment;
    exports services.commande;
    exports utils;
    exports DAO;
}
