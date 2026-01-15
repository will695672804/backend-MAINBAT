package com.mainbat.dto.equipement;

import com.mainbat.model.enums.EtatEquipement;
import com.mainbat.model.enums.NiveauCriticite;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipementRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
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
    private Integer heuresFonctionnement;
    private String fournisseur;
    private String fournisseurContact;
    
    @NotNull(message = "La zone est obligatoire")
    private Long zoneId;
}
