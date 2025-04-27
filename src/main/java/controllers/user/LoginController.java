package controllers.user;

import DAO.UserDAO;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Session;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import utils.MiniServer;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private WebView captchaWebView;

    private String captchaToken = null;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        MiniServer.startServer();
        int port = MiniServer.getPort();

        WebEngine webEngine = captchaWebView.getEngine();
        webEngine.load("http://localhost:" + port);

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", new Object() {
                    public void receiveToken(String token) {
                        captchaToken = token;
                        System.out.println("reCAPTCHA v3 token reçu : " + token);
                    }
                });
            }
        });
    }

    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        //if (captchaToken == null || !verifyRecaptcha(captchaToken)) {
          //  Alert alert = new Alert(Alert.AlertType.ERROR);
            //alert.setTitle("Erreur reCAPTCHA");
           // alert.setHeaderText("Échec de la vérification reCAPTCHA !");
            //alert.showAndWait();
          //  return;
        //}

        User user = userDAO.login(email, password);

        if (user != null) {
            Session.setCurrentUser(user);
            try {
                FXMLLoader loader;
                if (user.getRole().equalsIgnoreCase("ADMIN")) {
                    loader = new FXMLLoader(getClass().getResource("/org.example/dashboard.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/org.example/home.fxml"));
                }

                Parent root = loader.load();
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Accueil");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Email ou mot de passe incorrect !");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org.example/user/ForgotPasswordView.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Réinitialisation du mot de passe");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goTosignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org.example/user/signup.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign up");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean verifyRecaptcha(String token) {
        String secretKey = "6LfDeR8rAAAAAEkXh85rG86_kySFjCQ_6RBrLP_u"; // Secret V3
        try {
            URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String params = "secret=" + secretKey + "&response=" + token;

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
            }

            String json;
            try (Scanner sc = new Scanner(conn.getInputStream())) {
                json = sc.useDelimiter("\\A").next();
            }

            JSONObject jsonObject = new JSONObject(json);
            System.out.println("Réponse Google : " + jsonObject);

            return jsonObject.getBoolean("success") && jsonObject.getDouble("score") >= 0.5;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
