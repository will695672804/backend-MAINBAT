package com.mainbat.service;

import com.mainbat.dto.intervention.InterventionRequest;
import com.mainbat.dto.intervention.InterventionResponse;
import com.mainbat.exception.BadRequestException;
import com.mainbat.exception.ResourceNotFoundException;
import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.StatutIntervention;
import com.mainbat.model.enums.TypeIntervention;
import com.mainbat.model.equipement.Equipement;
import com.mainbat.model.equipe.Technicien;
import com.mainbat.model.intervention.ChecklistItem;
import com.mainbat.model.intervention.Intervention;
import com.mainbat.repository.EquipementRepository;
import com.mainbat.repository.InterventionRepository;
import com.mainbat.repository.TechnicienRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterventionService {

    private final InterventionRepository interventionRepository;
    private final EquipementRepository equipementRepository;
    private final TechnicienRepository technicienRepository;

    @Transactional(readOnly = true)
    public Page<InterventionResponse> getAllInterventions(Pageable pageable) {
        return interventionRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> getInterventionsByStatut(StatutIntervention statut) {
        return interventionRepository.findByStatut(statut)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> getInterventionsByType(TypeIntervention type) {
        return interventionRepository.findByTypeIntervention(type)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> getInterventionsByEquipement(Long equipementId) {
        return interventionRepository.findByEquipementId(equipementId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> getInterventionsByTechnicien(Long technicienId) {
        return interventionRepository.findByTechnicienId(technicienId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> getInterventionsEnRetard() {
        return interventionRepository.findInterventionsEnRetard(LocalDateTime.now())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterventionResponse> getInterventionsCalendrier(LocalDateTime start, LocalDateTime end) {
        return interventionRepository.findByDatePlanifieeBetween(start, end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InterventionResponse getInterventionById(Long id) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention", id));
        return toResponse(intervention);
    }

    @Transactional
    public InterventionResponse createIntervention(InterventionRequest request) {
        Equipement equipement = equipementRepository.findById(request.getEquipementId())
                .orElseThrow(() -> new ResourceNotFoundException("Équipement", request.getEquipementId()));

        Intervention intervention = Intervention.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .typeIntervention(request.getTypeIntervention())
                .statut(StatutIntervention.CREE)
                .urgence(request.getUrgence())
                .causePresumee(request.getCausePresumee())
                .datePlanifiee(request.getDatePlanifiee())
                .dureeEstimeeMinutes(request.getDureeEstimeeMinutes())
                .frequenceJours(request.getFrequenceJours())
                .equipement(equipement)
                .build();

        // Add checklist items if provided
        if (request.getChecklistItems() != null) {
            int ordre = 1;
            for (String item : request.getChecklistItems()) {
                ChecklistItem checklistItem = ChecklistItem.builder()
                        .libelle(item)
                        .ordre(ordre++)
                        .intervention(intervention)
                        .build();
                intervention.getChecklistItems().add(checklistItem);
            }
        }

        // Assign technician if provided
        if (request.getTechnicienId() != null) {
            Technicien technicien = technicienRepository.findById(request.getTechnicienId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technicien", request.getTechnicienId()));
            intervention.setTechnicien(technicien);
            intervention.setStatut(StatutIntervention.ASSIGNE);
        }

        intervention = interventionRepository.save(intervention);
        log.info("Intervention créée: {} - {}", intervention.getId(), intervention.getTitre());
        return toResponse(intervention);
    }

    @Transactional
    public InterventionResponse updateIntervention(Long id, InterventionRequest request) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention", id));

        intervention.setTitre(request.getTitre());
        intervention.setDescription(request.getDescription());
        intervention.setUrgence(request.getUrgence());
        intervention.setCausePresumee(request.getCausePresumee());
        intervention.setDatePlanifiee(request.getDatePlanifiee());
        intervention.setDureeEstimeeMinutes(request.getDureeEstimeeMinutes());

        intervention = interventionRepository.save(intervention);
        log.info("Intervention mise à jour: {}", intervention.getId());
        return toResponse(intervention);
    }

    @Transactional
    public InterventionResponse updateStatut(Long id, StatutIntervention statut) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention", id));

        // Update timestamps based on status
        switch (statut) {
            case EN_COURS:
                if (intervention.getDateDebut() == null) {
                    intervention.setDateDebut(LocalDateTime.now());
                }
                break;
            case TERMINE:
                intervention.setDateFin(LocalDateTime.now());
                if (intervention.getDateDebut() != null) {
                    long minutes = java.time.Duration.between(
                            intervention.getDateDebut(), intervention.getDateFin()).toMinutes();
                    intervention.setDureeReelleMinutes((int) minutes);
                }
                break;
            case CLOTURE:
                intervention.setDateCloture(LocalDateTime.now());
                break;
            default:
                break;
        }

        intervention.setStatut(statut);
        intervention = interventionRepository.save(intervention);
        log.info("Statut intervention {} changé: {}", id, statut);
        return toResponse(intervention);
    }

    @Transactional
    public InterventionResponse affecterTechnicien(Long id, Long technicienId) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention", id));

        Technicien technicien = technicienRepository.findById(technicienId)
                .orElseThrow(() -> new ResourceNotFoundException("Technicien", technicienId));

        intervention.setTechnicien(technicien);
        if (intervention.getStatut() == StatutIntervention.CREE) {
            intervention.setStatut(StatutIntervention.ASSIGNE);
        }

        // Update technician workload
        technicien.setChargeTravail(technicien.getChargeTravail() + 1);
        technicienRepository.save(technicien);

        intervention = interventionRepository.save(intervention);
        log.info("Intervention {} affectée à {}", id, technicien.getUser().getFullName());
        return toResponse(intervention);
    }

    @Transactional
    public InterventionResponse cloturerIntervention(Long id, String rapport, String actionsEffectuees) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention", id));

        if (intervention.getStatut() != StatutIntervention.TERMINE) {
            throw new BadRequestException("L'intervention doit être terminée avant d'être clôturée");
        }

        intervention.setRapportIntervention(rapport);
        intervention.setActionsEffectuees(actionsEffectuees);
        intervention.setStatut(StatutIntervention.CLOTURE);
        intervention.setDateCloture(LocalDateTime.now());

        // Update equipment cumulative cost
        Double coutTotal = intervention.getCoutTotal();
        if (coutTotal != null && coutTotal > 0) {
            Equipement equipement = intervention.getEquipement();
            equipement.setCoutCumule(equipement.getCoutCumule() + coutTotal);
            equipementRepository.save(equipement);
        }

        // Update technician workload
        if (intervention.getTechnicien() != null) {
            Technicien technicien = intervention.getTechnicien();
            technicien.setChargeTravail(Math.max(0, technicien.getChargeTravail() - 1));
            technicienRepository.save(technicien);
        }

        intervention = interventionRepository.save(intervention);
        log.info("Intervention {} clôturée", id);
        return toResponse(intervention);
    }

    @Transactional
    public void deleteIntervention(Long id) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention", id));
        intervention.setIsActive(false);
        interventionRepository.save(intervention);
        log.info("Intervention {} supprimée", id);
    }

    private InterventionResponse toResponse(Intervention intervention) {
        List<InterventionResponse.ChecklistItemDto> checklistItems = intervention.getChecklistItems()
                .stream()
                .map(item -> InterventionResponse.ChecklistItemDto.builder()
                        .id(item.getId())
                        .libelle(item.getLibelle())
                        .estComplete(item.getEstComplete())
                        .commentaire(item.getCommentaire())
                        .build())
                .collect(Collectors.toList());

        return InterventionResponse.builder()
                .id(intervention.getId())
                .titre(intervention.getTitre())
                .description(intervention.getDescription())
                .typeIntervention(intervention.getTypeIntervention())
                .statut(intervention.getStatut())
                .urgence(intervention.getUrgence())
                .causePresumee(intervention.getCausePresumee())
                .datePlanifiee(intervention.getDatePlanifiee())
                .dateDebut(intervention.getDateDebut())
                .dateFin(intervention.getDateFin())
                .dateCloture(intervention.getDateCloture())
                .dureeEstimeeMinutes(intervention.getDureeEstimeeMinutes())
                .dureeReelleMinutes(intervention.getDureeReelleMinutes())
                .rapportIntervention(intervention.getRapportIntervention())
                .actionsEffectuees(intervention.getActionsEffectuees())
                .piecesUtilisees(intervention.getPiecesUtilisees())
                .createdAt(intervention.getCreatedAt())
                .equipementId(intervention.getEquipement().getId())
                .equipementNom(intervention.getEquipement().getNom())
                .batimentNom(intervention.getEquipement().getZone().getBatiment().getNom())
                .technicienId(intervention.getTechnicien() != null ? intervention.getTechnicien().getId() : null)
                .technicienNom(intervention.getTechnicien() != null ? 
                        intervention.getTechnicien().getUser().getFullName() : null)
                .coutTotal(intervention.getCoutTotal())
                .checklistItems(checklistItems)
                .build();
    }
}
