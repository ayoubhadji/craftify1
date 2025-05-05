package controllers.commande;

import DAO.UserDAO;
import entity.Commande;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.commande.CommandeService;
import services.commande.CommandeServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class StatistiquesController {

    @FXML
    private ComboBox<String> periodeComboBox;
    @FXML
    private BarChart<String, Number> commandesParJourChart;
    @FXML
    private LineChart<String, Number> chiffreAffairesChart;
    @FXML
    private PieChart statutsPieChart;
    @FXML
    private StackedBarChart<String, Number> commandesParClientChart;
    @FXML
    private TableView<StatistiquesLigne> statistiquesTable;
    @FXML
    private TableColumn<StatistiquesLigne, String> dateColumn;
    @FXML
    private TableColumn<StatistiquesLigne, Integer> nbCommandesColumn;
    @FXML
    private TableColumn<StatistiquesLigne, Double> montantColumn;
    @FXML
    private TableColumn<StatistiquesLigne, Double> moyenneColumn;
    @FXML
    private Label totalCommandesLabel;
    @FXML
    private Label chiffreAffairesTotalLabel;

    private final CommandeService commandeService;
    private final UserDAO userDAO;
    private final ObservableList<StatistiquesLigne> statistiquesData;

    public StatistiquesController() {
        this.commandeService = new CommandeServiceImpl();
        this.userDAO = new UserDAO();
        this.statistiquesData = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Initialisation des périodes
        periodeComboBox.setItems(FXCollections.observableArrayList(
            "Aujourd'hui",
            "Cette semaine",
            "Ce mois",
            "Cette année",
            "Tout"
        ));
        periodeComboBox.getSelectionModel().selectFirst();

        // Configuration des colonnes du tableau
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        nbCommandesColumn.setCellValueFactory(new PropertyValueFactory<>("nbCommandes"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        moyenneColumn.setCellValueFactory(new PropertyValueFactory<>("moyenne"));

        statistiquesTable.setItems(statistiquesData);

        // Initialisation des graphiques
        initialiserGraphiques();

        // Chargement initial des données
        actualiserStatistiques();
    }

    @FXML
    private void actualiserStatistiques() {
        String periode = periodeComboBox.getValue();
        LocalDate dateDebut = getDateDebut(periode);
        LocalDate dateFin = LocalDate.now();

        List<Commande> commandes = commandeService.getAllCommandes().stream()
            .filter(c -> !c.getDateCommande().toLocalDate().isBefore(dateDebut))
            .filter(c -> !c.getDateCommande().toLocalDate().isAfter(dateFin))
            .collect(Collectors.toList());

        // Mise à jour des graphiques
        mettreAJourGraphiques(commandes);

        // Mise à jour du tableau
        mettreAJourTableau(commandes);

        // Mise à jour des totaux
        mettreAJourTotaux(commandes);
    }

    private LocalDate getDateDebut(String periode) {
        LocalDate maintenant = LocalDate.now();
        return switch (periode) {
            case "Aujourd'hui" -> maintenant;
            case "Cette semaine" -> maintenant.minusWeeks(1);
            case "Ce mois" -> maintenant.minusMonths(1);
            case "Cette année" -> maintenant.minusYears(1);
            default -> LocalDate.of(2000, 1, 1); // Pour "Tout"
        };
    }

    private void initialiserGraphiques() {
        // Configuration du graphique des statuts
        statutsPieChart.setTitle("Répartition des statuts de commande");
        
        // Configuration du graphique des commandes par client
        commandesParClientChart.setTitle("Commandes par client");
        commandesParClientChart.setLegendVisible(true);
    }

    private void mettreAJourGraphiques(List<Commande> commandes) {
        // Réinitialisation des graphiques
        commandesParJourChart.getData().clear();
        chiffreAffairesChart.getData().clear();
        statutsPieChart.getData().clear();
        commandesParClientChart.getData().clear();

        // Groupement des commandes par date
        Map<LocalDate, List<Commande>> commandesParDate = commandes.stream()
            .collect(Collectors.groupingBy(c -> c.getDateCommande().toLocalDate()));

        // Création des séries pour les graphiques
        XYChart.Series<String, Number> seriesCommandes = new XYChart.Series<>();
        seriesCommandes.setName("Nombre de commandes");
        XYChart.Series<String, Number> seriesChiffreAffaires = new XYChart.Series<>();
        seriesChiffreAffaires.setName("Chiffre d'affaires");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        commandesParDate.forEach((date, cmds) -> {
            String dateStr = date.format(formatter);
            seriesCommandes.getData().add(new XYChart.Data<>(dateStr, cmds.size()));
            double chiffreAffaires = cmds.stream()
                .mapToDouble(Commande::getTotal)
                .sum();
            seriesChiffreAffaires.getData().add(new XYChart.Data<>(dateStr, chiffreAffaires));
        });

        commandesParJourChart.getData().add(seriesCommandes);
        chiffreAffairesChart.getData().add(seriesChiffreAffaires);

        // Mise à jour du graphique des statuts
        Map<String, Long> statutsCount = commandes.stream()
            .collect(Collectors.groupingBy(
                Commande::getStatut,
                Collectors.counting()
            ));
            
        statutsCount.forEach((statut, count) -> {
            PieChart.Data data = new PieChart.Data(statut, count);
            statutsPieChart.getData().add(data);
        });
        
        // Mise à jour du graphique des commandes par client
        Map<Integer, List<Commande>> commandesParClientId = commandes.stream()
            .collect(Collectors.groupingBy(Commande::getIdClient));
            
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre de commandes");
        
        commandesParClientId.forEach((clientId, clientCommandes) -> {
            User client = userDAO.getById(clientId);
            String clientName = client != null ? client.getNom() : "Client #" + clientId;
            series.getData().add(new XYChart.Data<>(clientName, clientCommandes.size()));
        });
        
        commandesParClientChart.getData().add(series);
    }

    private void mettreAJourTableau(List<Commande> commandes) {
        statistiquesData.clear();
        Map<LocalDate, List<Commande>> commandesParDate = commandes.stream()
            .collect(Collectors.groupingBy(c -> c.getDateCommande().toLocalDate()));

        commandesParDate.forEach((date, cmds) -> {
            double montantTotal = cmds.stream()
                .mapToDouble(Commande::getTotal)
                .sum();
            double moyenne = montantTotal / cmds.size();
            
            statistiquesData.add(new StatistiquesLigne(
                date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                cmds.size(),
                montantTotal,
                moyenne
            ));
        });
    }

    private void mettreAJourTotaux(List<Commande> commandes) {
        int totalCommandes = commandes.size();
        double chiffreAffairesTotal = commandes.stream()
            .mapToDouble(Commande::getTotal)
            .sum();

        totalCommandesLabel.setText(String.valueOf(totalCommandes));
        chiffreAffairesTotalLabel.setText(String.format("%.2f €", chiffreAffairesTotal));
    }

    @FXML
    public void retourDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) periodeComboBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de navigation");
            alert.setContentText("Impossible de charger le dashboard : " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Classe interne pour représenter une ligne du tableau
    public static class StatistiquesLigne {
        private final String date;
        private final int nbCommandes;
        private final double montant;
        private final double moyenne;

        public StatistiquesLigne(String date, int nbCommandes, double montant, double moyenne) {
            this.date = date;
            this.nbCommandes = nbCommandes;
            this.montant = montant;
            this.moyenne = moyenne;
        }

        public String getDate() { return date; }
        public int getNbCommandes() { return nbCommandes; }
        public double getMontant() { return montant; }
        public double getMoyenne() { return moyenne; }
    }
} 