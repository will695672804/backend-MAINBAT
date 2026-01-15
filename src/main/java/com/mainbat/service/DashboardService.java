package com.mainbat.service;

import com.mainbat.dto.dashboard.DashboardResponse;
import com.mainbat.model.enums.EtatEquipement;
import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.StatutIntervention;
import com.mainbat.model.enums.TypeIntervention;
import com.mainbat.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BatimentRepository batimentRepository;
    private final EquipementRepository equipementRepository;
    private final InterventionRepository interventionRepository;
    private final AlerteRepository alerteRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // Count equipment by status
        Long equipementsHS = equipementRepository.countByEtat(EtatEquipement.HS);
        Long equipementsDegrades = equipementRepository.countByEtat(EtatEquipement.DEGRADE);

        // Count interventions
        Long interventionsEnCours = interventionRepository.countByStatut(StatutIntervention.EN_COURS);
        Long interventionsEnRetard = (long) interventionRepository.findInterventionsEnRetard(now).size();

        // Interventions by status
        Map<String, Long> interventionsParStatut = new HashMap<>();
        for (StatutIntervention statut : StatutIntervention.values()) {
            interventionsParStatut.put(statut.name(), interventionRepository.countByStatut(statut));
        }

        // Today's interventions
        List<DashboardResponse.InterventionSummary> interventionsAujourdhui = 
            interventionRepository.findByDatePlanifieeBetween(startOfDay, endOfDay)
                .stream()
                .map(i -> DashboardResponse.InterventionSummary.builder()
                        .id(i.getId())
                        .titre(i.getTitre())
                        .statut(i.getStatut().name())
                        .urgence(i.getUrgence() != null ? i.getUrgence().name() : null)
                        .batimentNom(i.getEquipement().getZone().getBatiment().getNom())
                        .technicienNom(i.getTechnicien() != null ? i.getTechnicien().getUser().getFullName() : null)
                        .build())
                .collect(Collectors.toList());

        // Critical alerts
        List<DashboardResponse.AlerteSummary> alertesCritiques =
            alerteRepository.findByNiveauAndIsResolvedFalse(NiveauCriticite.CRITIQUE)
                .stream()
                .limit(5)
                .map(a -> DashboardResponse.AlerteSummary.builder()
                        .id(a.getId())
                        .titre(a.getTitre())
                        .niveau(a.getNiveau().name())
                        .targetType(a.getTargetType())
                        .targetId(a.getTargetId())
                        .build())
                .collect(Collectors.toList());

        // MTTR
        Double mttr = interventionRepository.calculateMTTR();

        return DashboardResponse.builder()
                .totalBatiments(batimentRepository.count())
                .totalEquipements(equipementRepository.count())
                .equipementsHS(equipementsHS)
                .equipementsDegrades(equipementsDegrades)
                .interventionsEnCours(interventionsEnCours)
                .interventionsEnRetard(interventionsEnRetard)
                .interventionsUrgentes((long) interventionRepository.findUrgentInterventions(NiveauCriticite.CRITIQUE).size())
                .interventionsParStatut(interventionsParStatut)
                .mttr(mttr)
                .alertesCritiques(alertesCritiques)
                .interventionsAujourdhui(interventionsAujourdhui)
                .build();
    }
}
