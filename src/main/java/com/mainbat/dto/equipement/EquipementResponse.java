package com.mainbat.dto.equipement;

import com.mainbat.model.enums.EtatEquipement;
import com.mainbat.model.enums.NiveauCriticite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipementResponse {
    
    private Long id;
    private String nom;
    private String typeEquipement;
    private String marque;
    private String modele;
    private String numeroSerie;
    private LocalDate dateMiseService;
    private LocalDate dateFinGarantie;
    private EtatEquipement etat;
    private NiveauCriticite criticite;
    private String description;
    private String photoUrl;
    private String qrCode;
    private Double coutCumule;
    private Integer heuresFonctionnement;
    private String fournisseur;
    private String fournisseurContact;
    private LocalDateTime createdAt;
    
    // Zone info
    private Long zoneId;
    private String zoneNom;
    
    // Batiment info
    private Long batimentId;
    private String batimentNom;
    
    // Stats
    private Long nombreInterventions;
    private Long nombrePannes;
}
