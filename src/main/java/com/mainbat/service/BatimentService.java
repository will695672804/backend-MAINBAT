package com.mainbat.service;

import com.mainbat.dto.batiment.BatimentRequest;
import com.mainbat.dto.batiment.BatimentResponse;
import com.mainbat.exception.ResourceNotFoundException;
import com.mainbat.model.batiment.Batiment;
import com.mainbat.repository.BatimentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatimentService {

    private final BatimentRepository batimentRepository;

    @Transactional(readOnly = true)
    public Page<BatimentResponse> getAllBatiments(Pageable pageable) {
        return batimentRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BatimentResponse> searchBatiments(String search, Pageable pageable) {
        return batimentRepository.searchBatiments(search, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public BatimentResponse getBatimentById(Long id) {
        Batiment batiment = batimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bâtiment", id));
        return toResponse(batiment);
    }

    @Transactional
    public BatimentResponse createBatiment(BatimentRequest request) {
        Batiment batiment = Batiment.builder()
                .nom(request.getNom())
                .adresse(request.getAdresse())
                .ville(request.getVille())
                .codePostal(request.getCodePostal())
                .pays(request.getPays())
                .typeBatiment(request.getTypeBatiment())
                .surface(request.getSurface())
                .nombreEtages(request.getNombreEtages())
                .description(request.getDescription())
                .planUrl(request.getPlanUrl())
                .contactNom(request.getContactNom())
                .contactTelephone(request.getContactTelephone())
                .contactEmail(request.getContactEmail())
                .criticite(request.getCriticite())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        batiment = batimentRepository.save(batiment);
        log.info("Bâtiment créé: {}", batiment.getNom());
        return toResponse(batiment);
    }

    @Transactional
    public BatimentResponse updateBatiment(Long id, BatimentRequest request) {
        Batiment batiment = batimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bâtiment", id));

        batiment.setNom(request.getNom());
        batiment.setAdresse(request.getAdresse());
        batiment.setVille(request.getVille());
        batiment.setCodePostal(request.getCodePostal());
        batiment.setPays(request.getPays());
        batiment.setTypeBatiment(request.getTypeBatiment());
        batiment.setSurface(request.getSurface());
        batiment.setNombreEtages(request.getNombreEtages());
        batiment.setDescription(request.getDescription());
        batiment.setPlanUrl(request.getPlanUrl());
        batiment.setContactNom(request.getContactNom());
        batiment.setContactTelephone(request.getContactTelephone());
        batiment.setContactEmail(request.getContactEmail());
        batiment.setCriticite(request.getCriticite());
        batiment.setLatitude(request.getLatitude());
        batiment.setLongitude(request.getLongitude());

        batiment = batimentRepository.save(batiment);
        log.info("Bâtiment mis à jour: {}", batiment.getNom());
        return toResponse(batiment);
    }

    @Transactional
    public void deleteBatiment(Long id) {
        Batiment batiment = batimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bâtiment", id));
        batiment.setIsActive(false);
        batimentRepository.save(batiment);
        log.info("Bâtiment supprimé: {}", batiment.getNom());
    }

    private BatimentResponse toResponse(Batiment batiment) {
        Long nombreEquipements = batimentRepository.countEquipementsByBatiment(batiment.getId());
        
        return BatimentResponse.builder()
                .id(batiment.getId())
                .nom(batiment.getNom())
                .adresse(batiment.getAdresse())
                .ville(batiment.getVille())
                .codePostal(batiment.getCodePostal())
                .pays(batiment.getPays())
                .typeBatiment(batiment.getTypeBatiment())
                .surface(batiment.getSurface())
                .nombreEtages(batiment.getNombreEtages())
                .description(batiment.getDescription())
                .planUrl(batiment.getPlanUrl())
                .contactNom(batiment.getContactNom())
                .contactTelephone(batiment.getContactTelephone())
                .contactEmail(batiment.getContactEmail())
                .criticite(batiment.getCriticite())
                .latitude(batiment.getLatitude())
                .longitude(batiment.getLongitude())
                .createdAt(batiment.getCreatedAt())
                .updatedAt(batiment.getUpdatedAt())
                .nombreZones((long) batiment.getZones().size())
                .nombreEquipements(nombreEquipements)
                .build();
    }
}
