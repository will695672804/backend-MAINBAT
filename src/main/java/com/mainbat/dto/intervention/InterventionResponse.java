package com.mainbat.dto.intervention;

import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.StatutIntervention;
import com.mainbat.model.enums.TypeIntervention;
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
public class InterventionResponse {
    
    private Long id;
    private String titre;
    private String description;
    private TypeIntervention typeIntervention;
    private StatutIntervention statut;
    private NiveauCriticite urgence;
    private String causePresumee;
    private LocalDateTime datePlanifiee;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private LocalDateTime dateCloture;
    private Integer dureeEstimeeMinutes;
    private Integer dureeReelleMinutes;
    private String rapportIntervention;
    private String actionsEffectuees;
    private String piecesUtilisees;
    private LocalDateTime createdAt;
    
    // Equipement info
    private Long equipementId;
    private String equipementNom;
    private String batimentNom;
    
    // Technicien info
    private Long technicienId;
    private String technicienNom;
    
    // Cost info
    private Double coutTotal;
    
    // Checklist
    private List<ChecklistItemDto> checklistItems;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChecklistItemDto {
        private Long id;
        private String libelle;
        private Boolean estComplete;
        private String commentaire;
    }
}
