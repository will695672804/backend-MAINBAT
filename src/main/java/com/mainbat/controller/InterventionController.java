package com.mainbat.controller;

import com.mainbat.dto.common.ApiResponse;
import com.mainbat.dto.intervention.InterventionRequest;
import com.mainbat.dto.intervention.InterventionResponse;
import com.mainbat.model.enums.StatutIntervention;
import com.mainbat.model.enums.TypeIntervention;
import com.mainbat.service.InterventionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/interventions")
@RequiredArgsConstructor
@Tag(name = "Interventions", description = "Gestion des interventions de maintenance")
@SecurityRequirement(name = "bearerAuth")
public class InterventionController {

    private final InterventionService interventionService;

    @GetMapping
    @Operation(summary = "Liste des interventions")
    public ResponseEntity<ApiResponse<Page<InterventionResponse>>> getAllInterventions(Pageable pageable) {
        Page<InterventionResponse> interventions = interventionService.getAllInterventions(pageable);
        return ResponseEntity.ok(ApiResponse.success(interventions));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une intervention")
    public ResponseEntity<ApiResponse<InterventionResponse>> getInterventionById(@PathVariable Long id) {
        InterventionResponse intervention = interventionService.getInterventionById(id);
        return ResponseEntity.ok(ApiResponse.success(intervention));
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Interventions par statut")
    public ResponseEntity<ApiResponse<List<InterventionResponse>>> getInterventionsByStatut(
            @PathVariable StatutIntervention statut) {
        List<InterventionResponse> interventions = interventionService.getInterventionsByStatut(statut);
        return ResponseEntity.ok(ApiResponse.success(interventions));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Interventions par type")
    public ResponseEntity<ApiResponse<List<InterventionResponse>>> getInterventionsByType(
            @PathVariable TypeIntervention type) {
        List<InterventionResponse> interventions = interventionService.getInterventionsByType(type);
        return ResponseEntity.ok(ApiResponse.success(interventions));
    }

    @GetMapping("/equipement/{equipementId}")
    @Operation(summary = "Interventions d'un équipement")
    public ResponseEntity<ApiResponse<List<InterventionResponse>>> getInterventionsByEquipement(
            @PathVariable Long equipementId) {
        List<InterventionResponse> interventions = interventionService.getInterventionsByEquipement(equipementId);
        return ResponseEntity.ok(ApiResponse.success(interventions));
    }

    @GetMapping("/technicien/{technicienId}")
    @Operation(summary = "Interventions d'un technicien")
    public ResponseEntity<ApiResponse<List<InterventionResponse>>> getInterventionsByTechnicien(
            @PathVariable Long technicienId) {
        List<InterventionResponse> interventions = interventionService.getInterventionsByTechnicien(technicienId);
        return ResponseEntity.ok(ApiResponse.success(interventions));
    }

    @GetMapping("/en-retard")
    @Operation(summary = "Interventions en retard")
    public ResponseEntity<ApiResponse<List<InterventionResponse>>> getInterventionsEnRetard() {
        List<InterventionResponse> interventions = interventionService.getInterventionsEnRetard();
        return ResponseEntity.ok(ApiResponse.success(interventions));
    }

    @GetMapping("/calendrier")
    @Operation(summary = "Interventions pour le calendrier")
    public ResponseEntity<ApiResponse<List<InterventionResponse>>> getInterventionsCalendrier(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<InterventionResponse> interventions = interventionService.getInterventionsCalendrier(start, end);
        return ResponseEntity.ok(ApiResponse.success(interventions));
    }

    @PostMapping
    @Operation(summary = "Créer une intervention")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE', 'SUPERVISEUR', 'TECHNICIEN')")
    public ResponseEntity<ApiResponse<InterventionResponse>> createIntervention(
            @Valid @RequestBody InterventionRequest request) {
        InterventionResponse intervention = interventionService.createIntervention(request);
        return ResponseEntity.ok(ApiResponse.success("Intervention créée avec succès", intervention));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une intervention")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE', 'SUPERVISEUR')")
    public ResponseEntity<ApiResponse<InterventionResponse>> updateIntervention(
            @PathVariable Long id,
            @Valid @RequestBody InterventionRequest request) {
        InterventionResponse intervention = interventionService.updateIntervention(id, request);
        return ResponseEntity.ok(ApiResponse.success("Intervention mise à jour", intervention));
    }

    @PutMapping("/{id}/statut")
    @Operation(summary = "Changer le statut d'une intervention")
    public ResponseEntity<ApiResponse<InterventionResponse>> updateStatut(
            @PathVariable Long id,
            @RequestParam StatutIntervention statut) {
        InterventionResponse intervention = interventionService.updateStatut(id, statut);
        return ResponseEntity.ok(ApiResponse.success("Statut mis à jour", intervention));
    }

    @PutMapping("/{id}/affecter")
    @Operation(summary = "Affecter une intervention à un technicien")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE', 'SUPERVISEUR')")
    public ResponseEntity<ApiResponse<InterventionResponse>> affecterTechnicien(
            @PathVariable Long id,
            @RequestParam Long technicienId) {
        InterventionResponse intervention = interventionService.affecterTechnicien(id, technicienId);
        return ResponseEntity.ok(ApiResponse.success("Intervention affectée", intervention));
    }

    @PostMapping("/{id}/cloturer")
    @Operation(summary = "Clôturer une intervention")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE', 'SUPERVISEUR')")
    public ResponseEntity<ApiResponse<InterventionResponse>> cloturerIntervention(
            @PathVariable Long id,
            @RequestParam(required = false) String rapport,
            @RequestParam(required = false) String actionsEffectuees) {
        InterventionResponse intervention = interventionService.cloturerIntervention(id, rapport, actionsEffectuees);
        return ResponseEntity.ok(ApiResponse.success("Intervention clôturée", intervention));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une intervention")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<Void>> deleteIntervention(@PathVariable Long id) {
        interventionService.deleteIntervention(id);
        return ResponseEntity.ok(ApiResponse.success("Intervention supprimée", null));
    }
}
