package com.mainbat.controller;

import com.mainbat.dto.batiment.ZoneRequest;
import com.mainbat.dto.batiment.ZoneResponse;
import com.mainbat.dto.common.ApiResponse;
import com.mainbat.service.ZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
@Tag(name = "Zones", description = "Gestion des zones/étages")
@SecurityRequirement(name = "bearerAuth")
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping("/batiment/{batimentId}")
    @Operation(summary = "Liste des zones d'un bâtiment")
    public ResponseEntity<ApiResponse<List<ZoneResponse>>> getZonesByBatiment(
            @PathVariable Long batimentId) {
        List<ZoneResponse> zones = zoneService.getZonesByBatiment(batimentId);
        return ResponseEntity.ok(ApiResponse.success(zones));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une zone")
    public ResponseEntity<ApiResponse<ZoneResponse>> getZoneById(@PathVariable Long id) {
        ZoneResponse zone = zoneService.getZoneById(id);
        return ResponseEntity.ok(ApiResponse.success(zone));
    }

    @PostMapping
    @Operation(summary = "Créer une zone")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<ZoneResponse>> createZone(
            @Valid @RequestBody ZoneRequest request) {
        ZoneResponse zone = zoneService.createZone(request);
        return ResponseEntity.ok(ApiResponse.success("Zone créée avec succès", zone));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une zone")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<ZoneResponse>> updateZone(
            @PathVariable Long id,
            @Valid @RequestBody ZoneRequest request) {
        ZoneResponse zone = zoneService.updateZone(id, request);
        return ResponseEntity.ok(ApiResponse.success("Zone mise à jour avec succès", zone));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une zone")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<Void>> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.ok(ApiResponse.success("Zone supprimée avec succès", null));
    }
}
