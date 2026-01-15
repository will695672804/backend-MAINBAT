package com.mainbat.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    
    // KPIs
    private Long totalBatiments;
    private Long totalEquipements;
    private Long equipementsHS;
    private Long equipementsDegrades;
    private Long interventionsEnCours;
    private Long interventionsEnRetard;
    private Long interventionsUrgentes;
    private Double coutsMois;
    
    // MTTR & MTBF
    private Double mttr;  // Mean Time To Repair (minutes)
    private Double mtbf;  // Mean Time Between Failures (hours)
    
    // Interventions by status
    private Map<String, Long> interventionsParStatut;
    
    // Interventions by type
    private Map<String, Long> interventionsParType;
    
    // Recent alerts
    private List<AlerteSummary> alertesCritiques;
    
    // Today's interventions
    private List<InterventionSummary> interventionsAujourdhui;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlerteSummary {
        private Long id;
        private String titre;
        private String niveau;
        private String targetType;
        private Long targetId;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterventionSummary {
        private Long id;
        private String titre;
        private String statut;
        private String urgence;
        private String batimentNom;
        private String technicienNom;
    }
}
