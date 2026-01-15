package com.mainbat.dto.equipe;

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
public class EquipeResponse {
    
    private Long id;
    private String nom;
    private String description;
    private Long superviseurId;
    private String superviseurNom;
    private Integer nombreTechniciens;
    private Integer interventionsEnCours;
    private LocalDateTime createdAt;
    private List<TechnicienSummary> techniciens;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechnicienSummary {
        private Long id;
        private String nom;
        private Boolean disponible;
        private Integer chargeTravail;
    }
}
