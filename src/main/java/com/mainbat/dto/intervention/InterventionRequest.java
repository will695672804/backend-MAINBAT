package com.mainbat.dto.intervention;

import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.TypeIntervention;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class InterventionRequest {
    
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    private String description;
    
    @NotNull(message = "Le type d'intervention est obligatoire")
    private TypeIntervention typeIntervention;
    
    @NotNull(message = "L'équipement est obligatoire")
    private Long equipementId;
    
    private NiveauCriticite urgence;
    private String causePresumee;
    private LocalDateTime datePlanifiee;
    private Integer dureeEstimeeMinutes;
    private Long technicienId;
    private Integer frequenceJours;  // Pour interventions préventives
    private List<String> checklistItems;
}
