package com.mainbat.dto.equipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicienResponse {
    
    private Long id;
    private Long userId;
    private String nom;
    private String email;
    private String telephone;
    private String matricule;
    private String telephoneUrgence;
    private Set<String> competences;
    private Set<String> certifications;
    private Set<String> zonesIntervention;
    private Boolean disponible;
    private Boolean enAstreinte;
    private Integer chargeTravail;
    private Long equipeId;
    private String equipeNom;
    private Integer interventionsEnCours;
    private Integer interventionsTerminees;
    private LocalDateTime createdAt;
}
