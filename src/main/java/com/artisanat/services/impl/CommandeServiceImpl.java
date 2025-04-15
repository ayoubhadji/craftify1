package com.artisanat.services.impl;

import com.artisanat.entities.Commande;
import com.artisanat.entities.LigneCommande;
import com.artisanat.services.CommandeService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandeServiceImpl implements CommandeService {
    private List<Commande> commandes = new ArrayList<>();
    private int nextId = 1;

    @Override
    public Commande creerCommande(Commande commande) {
        commande.setId(nextId++);
        commandes.add(commande);
        return commande;
    }

    @Override
    public Commande modifierCommande(Commande commande) {
        for (int i = 0; i < commandes.size(); i++) {
            if (commandes.get(i).getId() == commande.getId()) {
                commandes.set(i, commande);
                return commande;
            }
        }
        return null;
    }

    @Override
    public void annulerCommande(int id) {
        commandes.removeIf(c -> c.getId() == id);
    }

    @Override
    public Commande getCommandeById(int id) {
        return commandes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Commande> getAllCommandes() {
        return new ArrayList<>(commandes);
    }

    @Override
    public List<Commande> getCommandesByClient(int clientId) {
        return commandes.stream()
                .filter(c -> c.getClientId() == clientId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Commande> getCommandesByStatut(String statut) {
        return commandes.stream()
                .filter(c -> c.getStatut().equals(statut))
                .collect(Collectors.toList());
    }

    @Override
    public double calculerMontantTotal(int commandeId) {
        Commande commande = getCommandeById(commandeId);
        if (commande != null && commande.getLignesCommande() != null) {
            return commande.getLignesCommande().stream()
                    .mapToDouble(LigneCommande::getSousTotal)
                    .sum();
        }
        return 0.0;
    }
} 