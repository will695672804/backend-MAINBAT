package com.mainbat.dto.batiment;

import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.TypeBatiment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatimentResponse {
    
    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pays;
    private TypeBatiment typeBatiment;
    private Double surface;
    private Integer nombreEtages;
    private String description;
    private String planUrl;
    private String contactNom;
    private String contactTelephone;
    private String contactEmail;
    private NiveauCriticite criticite;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Stats
    private Long nombreZones;
    private Long nombreEquipements;
    private Long nombreInterventionsEnCours;
}
