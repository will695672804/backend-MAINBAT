package com.mainbat.service;

import com.mainbat.dto.equipe.EquipeRequest;
import com.mainbat.dto.equipe.EquipeResponse;
import com.mainbat.dto.equipe.TechnicienRequest;
import com.mainbat.dto.equipe.TechnicienResponse;
import com.mainbat.exception.BadRequestException;
import com.mainbat.exception.ResourceNotFoundException;
import com.mainbat.model.enums.StatutIntervention;
import com.mainbat.model.equipe.Equipe;
import com.mainbat.model.equipe.Technicien;
import com.mainbat.model.user.User;
import com.mainbat.repository.EquipeRepository;
import com.mainbat.repository.TechnicienRepository;
import com.mainbat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final TechnicienRepository technicienRepository;
    private final UserRepository userRepository;

    // ============ EQUIPE METHODS ============

    @Transactional(readOnly = true)
    public List<EquipeResponse> getAllEquipes() {
        return equipeRepository.findByIsActiveTrue()
                .stream()
                .map(this::toEquipeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipeResponse getEquipeById(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipe", id));
        return toEquipeResponse(equipe);
    }

    @Transactional
    public EquipeResponse createEquipe(EquipeRequest request) {
        Equipe equipe = Equipe.builder()
                .nom(request.getNom())
                .description(request.getDescription())
                .superviseurId(request.getSuperviseurId())
                .build();

        if (request.getSuperviseurId() != null) {
            User superviseur = userRepository.findById(request.getSuperviseurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Superviseur", request.getSuperviseurId()));
            equipe.setSuperviseurNom(superviseur.getFullName());
        }

        equipe = equipeRepository.save(equipe);
        log.info("Équipe créée: {}", equipe.getNom());
        return toEquipeResponse(equipe);
    }

    @Transactional
    public EquipeResponse updateEquipe(Long id, EquipeRequest request) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipe", id));

        equipe.setNom(request.getNom());
        equipe.setDescription(request.getDescription());
        equipe.setSuperviseurId(request.getSuperviseurId());

        if (request.getSuperviseurId() != null) {
            User superviseur = userRepository.findById(request.getSuperviseurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Superviseur", request.getSuperviseurId()));
            equipe.setSuperviseurNom(superviseur.getFullName());
        }

        equipe = equipeRepository.save(equipe);
        log.info("Équipe mise à jour: {}", equipe.getNom());
        return toEquipeResponse(equipe);
    }

    @Transactional
    public void deleteEquipe(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipe", id));
        equipe.setIsActive(false);
        equipeRepository.save(equipe);
        log.info("Équipe supprimée: {}", equipe.getNom());
    }

    // ============ TECHNICIEN METHODS ============

    @Transactional(readOnly = true)
    public List<TechnicienResponse> getAllTechniciens() {
        return technicienRepository.findAll()
                .stream()
                .filter(t -> t.getIsActive())
                .map(this::toTechnicienResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TechnicienResponse> getTechniciensByEquipe(Long equipeId) {
        return technicienRepository.findByEquipeId(equipeId)
                .stream()
                .map(this::toTechnicienResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TechnicienResponse> getTechniciensDisponibles() {
        return technicienRepository.findByDisponibleTrue()
                .stream()
                .map(this::toTechnicienResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TechnicienResponse> getTechniciensByCompetence(String competence) {
        return technicienRepository.findByCompetenceAndDisponible(competence)
                .stream()
                .map(this::toTechnicienResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TechnicienResponse getTechnicienById(Long id) {
        Technicien technicien = technicienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technicien", id));
        return toTechnicienResponse(technicien);
    }

    @Transactional
    public TechnicienResponse createTechnicien(TechnicienRequest request) {
        // Check if technician already exists for this user
        if (technicienRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new BadRequestException("Un profil technicien existe déjà pour cet utilisateur");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", request.getUserId()));

        Technicien technicien = Technicien.builder()
                .user(user)
                .matricule(request.getMatricule())
                .telephoneUrgence(request.getTelephoneUrgence())
                .competences(request.getCompetences() != null ? request.getCompetences() : new HashSet<>())
                .certifications(request.getCertifications() != null ? request.getCertifications() : new HashSet<>())
                .zonesIntervention(request.getZonesIntervention() != null ? request.getZonesIntervention() : new HashSet<>())
                .disponible(true)
                .enAstreinte(false)
                .chargeTravail(0)
                .build();

        if (request.getEquipeId() != null) {
            Equipe equipe = equipeRepository.findById(request.getEquipeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Équipe", request.getEquipeId()));
            technicien.setEquipe(equipe);
        }

        technicien = technicienRepository.save(technicien);
        log.info("Technicien créé: {}", user.getFullName());
        return toTechnicienResponse(technicien);
    }

    @Transactional
    public TechnicienResponse updateTechnicien(Long id, TechnicienRequest request) {
        Technicien technicien = technicienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technicien", id));

        technicien.setMatricule(request.getMatricule());
        technicien.setTelephoneUrgence(request.getTelephoneUrgence());
        if (request.getCompetences() != null) {
            technicien.setCompetences(request.getCompetences());
        }
        if (request.getCertifications() != null) {
            technicien.setCertifications(request.getCertifications());
        }
        if (request.getZonesIntervention() != null) {
            technicien.setZonesIntervention(request.getZonesIntervention());
        }

        if (request.getEquipeId() != null) {
            Equipe equipe = equipeRepository.findById(request.getEquipeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Équipe", request.getEquipeId()));
            technicien.setEquipe(equipe);
        }

        technicien = technicienRepository.save(technicien);
        log.info("Technicien mis à jour: {}", technicien.getUser().getFullName());
        return toTechnicienResponse(technicien);
    }

    @Transactional
    public TechnicienResponse updateDisponibilite(Long id, Boolean disponible) {
        Technicien technicien = technicienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technicien", id));
        technicien.setDisponible(disponible);
        technicien = technicienRepository.save(technicien);
        log.info("Disponibilité technicien {} : {}", id, disponible);
        return toTechnicienResponse(technicien);
    }

    @Transactional
    public TechnicienResponse updateAstreinte(Long id, Boolean enAstreinte) {
        Technicien technicien = technicienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technicien", id));
        technicien.setEnAstreinte(enAstreinte);
        technicien = technicienRepository.save(technicien);
        log.info("Astreinte technicien {} : {}", id, enAstreinte);
        return toTechnicienResponse(technicien);
    }

    @Transactional
    public void deleteTechnicien(Long id) {
        Technicien technicien = technicienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technicien", id));
        technicien.setIsActive(false);
        technicienRepository.save(technicien);
        log.info("Technicien supprimé: {}", id);
    }

    // ============ MAPPERS ============

    private EquipeResponse toEquipeResponse(Equipe equipe) {
        List<EquipeResponse.TechnicienSummary> techniciens = equipe.getTechniciens()
                .stream()
                .filter(t -> t.getIsActive())
                .map(t -> EquipeResponse.TechnicienSummary.builder()
                        .id(t.getId())
                        .nom(t.getUser().getFullName())
                        .disponible(t.getDisponible())
                        .chargeTravail(t.getChargeTravail())
                        .build())
                .collect(Collectors.toList());

        int interventionsEnCours = equipe.getTechniciens().stream()
                .mapToInt(t -> (int) t.getInterventions().stream()
                        .filter(i -> i.getStatut() == StatutIntervention.EN_COURS || 
                                    i.getStatut() == StatutIntervention.ASSIGNE)
                        .count())
                .sum();

        return EquipeResponse.builder()
                .id(equipe.getId())
                .nom(equipe.getNom())
                .description(equipe.getDescription())
                .superviseurId(equipe.getSuperviseurId())
                .superviseurNom(equipe.getSuperviseurNom())
                .nombreTechniciens(techniciens.size())
                .interventionsEnCours(interventionsEnCours)
                .createdAt(equipe.getCreatedAt())
                .techniciens(techniciens)
                .build();
    }

    private TechnicienResponse toTechnicienResponse(Technicien technicien) {
        int interventionsEnCours = (int) technicien.getInterventions().stream()
                .filter(i -> i.getStatut() == StatutIntervention.EN_COURS || 
                            i.getStatut() == StatutIntervention.ASSIGNE)
                .count();

        int interventionsTerminees = (int) technicien.getInterventions().stream()
                .filter(i -> i.getStatut() == StatutIntervention.CLOTURE)
                .count();

        return TechnicienResponse.builder()
                .id(technicien.getId())
                .userId(technicien.getUser().getId())
                .nom(technicien.getUser().getFullName())
                .email(technicien.getUser().getEmail())
                .telephone(technicien.getUser().getPhoneNumber())
                .matricule(technicien.getMatricule())
                .telephoneUrgence(technicien.getTelephoneUrgence())
                .competences(technicien.getCompetences())
                .certifications(technicien.getCertifications())
                .zonesIntervention(technicien.getZonesIntervention())
                .disponible(technicien.getDisponible())
                .enAstreinte(technicien.getEnAstreinte())
                .chargeTravail(technicien.getChargeTravail())
                .equipeId(technicien.getEquipe() != null ? technicien.getEquipe().getId() : null)
                .equipeNom(technicien.getEquipe() != null ? technicien.getEquipe().getNom() : null)
                .interventionsEnCours(interventionsEnCours)
                .interventionsTerminees(interventionsTerminees)
                .createdAt(technicien.getCreatedAt())
                .build();
    }
}
