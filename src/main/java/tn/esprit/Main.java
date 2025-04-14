package tn.esprit;

import controllers.user.UserControllerConsole;
import entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserControllerConsole controller = new UserControllerConsole();
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU UTILISATEUR ===");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Afficher tous les utilisateurs");
            System.out.println("3. Supprimer un utilisateur");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consomme le saut de ligne

            switch (choix) {
                case 1:
                    User user = new User();

                    System.out.print("Nom : ");
                    user.setNom(scanner.nextLine());

                    System.out.print("Email : ");
                    user.setEmail(scanner.nextLine());

                    System.out.print("Code : ");
                    user.setCode(scanner.nextLine());

                    System.out.print("Rôle : ");
                    user.setRole(scanner.nextLine());

                    System.out.print("Sexe : ");
                    user.setSexe(scanner.nextLine());

                    user.setDateNaissance(LocalDateTime.of(2000, 1, 1, 0, 0));
                    user.setDateJoin(LocalDateTime.now());

                    System.out.print("Téléphone : ");
                    user.setTel(scanner.nextLine());

                    System.out.print("Adresse : ");
                    user.setAddress(scanner.nextLine());

                    System.out.print("Fiscal : ");
                    user.setFiscal(scanner.nextLine());

                    System.out.print("Reset Token : ");
                    user.setResetToken(scanner.nextLine());

                    boolean success = controller.ajouterUser(user);
                    System.out.println(success ? "✅ Utilisateur ajouté avec succès." : "❌ Échec de l'ajout.");
                    break;

                case 2:
                    List<User> users = controller.getAllUsers();
                    System.out.println("\n📋 Liste des utilisateurs :");
                    for (User u : users) {
                        System.out.println(u.getId() + " - " + u.getNom() + " - " + u.getEmail());
                    }
                    break;

                case 3:
                    System.out.print("ID de l'utilisateur à supprimer : ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consomme le saut de ligne
                    boolean deleted = controller.supprimerUser(id);
                    System.out.println(deleted ? "🗑️ Utilisateur supprimé." : "⚠️ Utilisateur non trouvé.");
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("⛔ Choix invalide.");
                    break;
            }
        }

        scanner.close();
        System.out.println("👋 Fin du programme.");
    }
}
