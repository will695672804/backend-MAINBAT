package com.mainbat.controller;

import com.mainbat.dto.common.ApiResponse;
import com.mainbat.dto.equipement.EquipementRequest;
import com.mainbat.dto.equipement.EquipementResponse;
import com.mainbat.model.enums.EtatEquipement;
import com.mainbat.service.EquipementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Équipements", description = "Gestion des équipements")
@SecurityRequirement(name = "bearerAuth")
public class EquipementController {

    private final EquipementService equipementService;

    @GetMapping
    @Operation(summary = "Liste des équipements")
    public ResponseEntity<ApiResponse<Page<EquipementResponse>>> getAllEquipements(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<EquipementResponse> equipements;
        if (search != null && !search.isEmpty()) {
            equipements = equipementService.searchEquipements(search, pageable);
        } else {
            equipements = equipementService.getAllEquipements(pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(equipements));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un équipement")
    public ResponseEntity<ApiResponse<EquipementResponse>> getEquipementById(@PathVariable Long id) {
        EquipementResponse equipement = equipementService.getEquipementById(id);
        return ResponseEntity.ok(ApiResponse.success(equipement));
    }

    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "Équipements d'une zone")
    public ResponseEntity<ApiResponse<List<EquipementResponse>>> getEquipementsByZone(
            @PathVariable Long zoneId) {
        List<EquipementResponse> equipements = equipementService.getEquipementsByZone(zoneId);
        return ResponseEntity.ok(ApiResponse.success(equipements));
    }

    @GetMapping("/batiment/{batimentId}")
    @Operation(summary = "Équipements d'un bâtiment")
    public ResponseEntity<ApiResponse<List<EquipementResponse>>> getEquipementsByBatiment(
            @PathVariable Long batimentId) {
        List<EquipementResponse> equipements = equipementService.getEquipementsByBatiment(batimentId);
        return ResponseEntity.ok(ApiResponse.success(equipements));
    }

    @GetMapping("/en-panne")
    @Operation(summary = "Équipements en panne (HS ou dégradés)")
    public ResponseEntity<ApiResponse<List<EquipementResponse>>> getEquipementsEnPanne() {
        List<EquipementResponse> equipements = equipementService.getEquipementsEnPanne();
        return ResponseEntity.ok(ApiResponse.success(equipements));
    }

    @PostMapping
    @Operation(summary = "Créer un équipement")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<EquipementResponse>> createEquipement(
            @Valid @RequestBody EquipementRequest request) {
        EquipementResponse equipement = equipementService.createEquipement(request);
        return ResponseEntity.ok(ApiResponse.success("Équipement créé avec succès", equipement));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un équipement")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<EquipementResponse>> updateEquipement(
            @PathVariable Long id,
            @Valid @RequestBody EquipementRequest request) {
        EquipementResponse equipement = equipementService.updateEquipement(id, request);
        return ResponseEntity.ok(ApiResponse.success("Équipement mis à jour avec succès", equipement));
    }

    @PutMapping("/{id}/etat")
    @Operation(summary = "Changer l'état d'un équipement")
    public ResponseEntity<ApiResponse<EquipementResponse>> updateEtat(
            @PathVariable Long id,
            @RequestParam EtatEquipement etat) {
        EquipementResponse equipement = equipementService.updateEtat(id, etat);
        return ResponseEntity.ok(ApiResponse.success("État mis à jour", equipement));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un équipement")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<Void>> deleteEquipement(@PathVariable Long id) {
        equipementService.deleteEquipement(id);
        return ResponseEntity.ok(ApiResponse.success("Équipement supprimé avec succès", null));
    }
}
