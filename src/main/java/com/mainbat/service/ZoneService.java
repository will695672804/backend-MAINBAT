package com.mainbat.service;

import com.mainbat.dto.batiment.ZoneRequest;
import com.mainbat.dto.batiment.ZoneResponse;
import com.mainbat.exception.ResourceNotFoundException;
import com.mainbat.model.batiment.Batiment;
import com.mainbat.model.batiment.Zone;
import com.mainbat.repository.BatimentRepository;
import com.mainbat.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final BatimentRepository batimentRepository;

    @Transactional(readOnly = true)
    public List<ZoneResponse> getZonesByBatiment(Long batimentId) {
        return zoneRepository.findByBatimentIdAndIsActiveTrue(batimentId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ZoneResponse getZoneById(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone", id));
        return toResponse(zone);
    }

    @Transactional
    public ZoneResponse createZone(ZoneRequest request) {
        Batiment batiment = batimentRepository.findById(request.getBatimentId())
                .orElseThrow(() -> new ResourceNotFoundException("Bâtiment", request.getBatimentId()));

        Zone zone = Zone.builder()
                .nom(request.getNom())
                .etage(request.getEtage())
                .description(request.getDescription())
                .typeZone(request.getTypeZone())
                .batiment(batiment)
                .build();

        zone = zoneRepository.save(zone);
        log.info("Zone créée: {} dans bâtiment {}", zone.getNom(), batiment.getNom());
        return toResponse(zone);
    }

    @Transactional
    public ZoneResponse updateZone(Long id, ZoneRequest request) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone", id));

        zone.setNom(request.getNom());
        zone.setEtage(request.getEtage());
        zone.setDescription(request.getDescription());
        zone.setTypeZone(request.getTypeZone());

        zone = zoneRepository.save(zone);
        log.info("Zone mise à jour: {}", zone.getNom());
        return toResponse(zone);
    }

    @Transactional
    public void deleteZone(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone", id));
        zone.setIsActive(false);
        zoneRepository.save(zone);
        log.info("Zone supprimée: {}", zone.getNom());
    }

    private ZoneResponse toResponse(Zone zone) {
        return ZoneResponse.builder()
                .id(zone.getId())
                .nom(zone.getNom())
                .etage(zone.getEtage())
                .description(zone.getDescription())
                .typeZone(zone.getTypeZone())
                .batimentId(zone.getBatiment().getId())
                .batimentNom(zone.getBatiment().getNom())
                .nombreEquipements((long) zone.getEquipements().size())
                .createdAt(zone.getCreatedAt())
                .build();
    }
}
