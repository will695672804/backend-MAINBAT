package com.mainbat.service;

import com.mainbat.dto.equipement.EquipementRequest;
import com.mainbat.dto.equipement.EquipementResponse;
import com.mainbat.exception.BadRequestException;
import com.mainbat.exception.ResourceNotFoundException;
import com.mainbat.model.batiment.Zone;
import com.mainbat.model.enums.EtatEquipement;
import com.mainbat.model.equipement.Equipement;
import com.mainbat.repository.EquipementRepository;
import com.mainbat.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipementService {

    private final EquipementRepository equipementRepository;
    private final ZoneRepository zoneRepository;

    @Transactional(readOnly = true)
    public Page<EquipementResponse> getAllEquipements(Pageable pageable) {
        return equipementRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<EquipementResponse> searchEquipements(String search, Pageable pageable) {
        return equipementRepository.searchEquipements(search, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public EquipementResponse getEquipementById(Long id) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement", id));
        return toResponse(equipement);
    }

    @Transactional(readOnly = true)
    public List<EquipementResponse> getEquipementsByZone(Long zoneId) {
        return equipementRepository.findByZoneId(zoneId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipementResponse> getEquipementsByBatiment(Long batimentId) {
        return equipementRepository.findByBatimentId(batimentId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipementResponse> getEquipementsEnPanne() {
        return equipementRepository.findEquipementsEnPanne()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EquipementResponse createEquipement(EquipementRequest request) {
        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone", request.getZoneId()));

        // Check unique serial number
        if (request.getNumeroSerie() != null && 
            equipementRepository.findByNumeroSerie(request.getNumeroSerie()).isPresent()) {
            throw new BadRequestException("Ce numéro de série existe déjà");
        }

        Equipement equipement = Equipement.builder()
                .nom(request.getNom())
                .typeEquipement(request.getTypeEquipement())
                .marque(request.getMarque())
                .modele(request.getModele())
                .numeroSerie(request.getNumeroSerie())
                .dateMiseService(request.getDateMiseService())
                .dateFinGarantie(request.getDateFinGarantie())
                .etat(request.getEtat() != null ? request.getEtat() : EtatEquipement.OK)
                .criticite(request.getCriticite())
                .description(request.getDescription())
                .photoUrl(request.getPhotoUrl())
                .heuresFonctionnement(request.getHeuresFonctionnement())
                .fournisseur(request.getFournisseur())
                .fournisseurContact(request.getFournisseurContact())
                .qrCode(UUID.randomUUID().toString())
                .zone(zone)
                .build();

        equipement = equipementRepository.save(equipement);
        log.info("Équipement créé: {}", equipement.getNom());
        return toResponse(equipement);
    }

    @Transactional
    public EquipementResponse updateEquipement(Long id, EquipementRequest request) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement", id));

        equipement.setNom(request.getNom());
        equipement.setTypeEquipement(request.getTypeEquipement());
        equipement.setMarque(request.getMarque());
        equipement.setModele(request.getModele());
        equipement.setNumeroSerie(request.getNumeroSerie());
        equipement.setDateMiseService(request.getDateMiseService());
        equipement.setDateFinGarantie(request.getDateFinGarantie());
        equipement.setCriticite(request.getCriticite());
        equipement.setDescription(request.getDescription());
        equipement.setPhotoUrl(request.getPhotoUrl());
        equipement.setHeuresFonctionnement(request.getHeuresFonctionnement());
        equipement.setFournisseur(request.getFournisseur());
        equipement.setFournisseurContact(request.getFournisseurContact());

        if (request.getEtat() != null) {
            equipement.setEtat(request.getEtat());
        }

        equipement = equipementRepository.save(equipement);
        log.info("Équipement mis à jour: {}", equipement.getNom());
        return toResponse(equipement);
    }

    @Transactional
    public EquipementResponse updateEtat(Long id, EtatEquipement etat) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement", id));
        equipement.setEtat(etat);
        equipement = equipementRepository.save(equipement);
        log.info("État équipement {} changé: {}", equipement.getNom(), etat);
        return toResponse(equipement);
    }

    @Transactional
    public void deleteEquipement(Long id) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement", id));
        equipement.setIsActive(false);
        equipementRepository.save(equipement);
        log.info("Équipement supprimé: {}", equipement.getNom());
    }

    private EquipementResponse toResponse(Equipement equipement) {
        return EquipementResponse.builder()
                .id(equipement.getId())
                .nom(equipement.getNom())
                .typeEquipement(equipement.getTypeEquipement())
                .marque(equipement.getMarque())
                .modele(equipement.getModele())
                .numeroSerie(equipement.getNumeroSerie())
                .dateMiseService(equipement.getDateMiseService())
                .dateFinGarantie(equipement.getDateFinGarantie())
                .etat(equipement.getEtat())
                .criticite(equipement.getCriticite())
                .description(equipement.getDescription())
                .photoUrl(equipement.getPhotoUrl())
                .qrCode(equipement.getQrCode())
                .coutCumule(equipement.getCoutCumule())
                .heuresFonctionnement(equipement.getHeuresFonctionnement())
                .fournisseur(equipement.getFournisseur())
                .fournisseurContact(equipement.getFournisseurContact())
                .createdAt(equipement.getCreatedAt())
                .zoneId(equipement.getZone().getId())
                .zoneNom(equipement.getZone().getNom())
                .batimentId(equipement.getZone().getBatiment().getId())
                .batimentNom(equipement.getZone().getBatiment().getNom())
                .nombreInterventions((long) equipement.getInterventions().size())
                .nombrePannes((long) equipement.getHistoriquePannes().size())
                .build();
    }
}
