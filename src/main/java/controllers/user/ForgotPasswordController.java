package controllers.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.MyDataBase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;
//
import java.util.Base64;
import java.nio.charset.StandardCharsets;
//
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleForgotPassword() {
        String email = emailField.getText().trim();


        if (email.isEmpty()) {
            messageLabel.setText("Veuillez entrer une adresse email.");
            messageLabel.setVisible(true);
            return;
        }

        try (Connection conn = MyDataBase.getInstance().getMyConnection()) {

            String checkUserQuery = "SELECT * FROM user WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkUserQuery)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //String token = UUID.randomUUID().toString();
                        long expirationTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes
                        String rawToken = UUID.randomUUID().toString() + ":" + expirationTime;
                        String token = Base64.getEncoder().encodeToString(rawToken.getBytes(StandardCharsets.UTF_8));
                        // nom
                        String name = rs.getString("nom");

                        String updateTokenQuery = "UPDATE user SET reset_token = ? WHERE email = ?";
                        try (PreparedStatement updatePs = conn.prepareStatement(updateTokenQuery)) {
                            updatePs.setString(1, token);
                            updatePs.setString(2, email);
                            updatePs.executeUpdate();
                        }

                        sendResetEmail(email,name, token);
                        messageLabel.setText("Un lien de réinitialisation a été envoyé à : " + email);
                    } else {
                        messageLabel.setText("Aucun compte trouvé avec cet email.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Erreur lors de la connexion à la base de données.");
        }

        messageLabel.setVisible(true);
    }

    private void sendResetEmail(String email, String name , String token) {
        final String fromEmail = "a7434302@gmail.com"; // Ton adresse Gmail
        final String password = "iwqlcrwtkrdtdspr";     // Mot de passe d'application

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Réinitialisation de votre mot de passe");

            String htmlContent = "<img src='https://i.imgur.com/QA1OZMX.png' alt='Logo' style='width:150px;'><br><br>"
                    +"<h3>Bonjour " + name + ",</h3>"
                    + "<p>Voici votre code de réinitialisation :</p>"
                    + "<b style='font-size:18px;'>" + token + "</b><br>"
                    + "<p>Copiez ce code dans l'application pour réinitialiser votre mot de passe.</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("E-mail de réinitialisation envoyé à : " + email);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenResetPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/user/ResetPassword.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Réinitialiser le mot de passe");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
