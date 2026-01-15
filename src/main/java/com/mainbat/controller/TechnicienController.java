package com.mainbat.controller;

import com.mainbat.dto.common.ApiResponse;
import com.mainbat.dto.equipe.TechnicienRequest;
import com.mainbat.dto.equipe.TechnicienResponse;
import com.mainbat.service.EquipeService;
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
@RequestMapping("/techniciens")
@RequiredArgsConstructor
@Tag(name = "Techniciens", description = "Gestion des techniciens")
@SecurityRequirement(name = "bearerAuth")
public class TechnicienController {

    private final EquipeService equipeService;

    @GetMapping
    @Operation(summary = "Liste des techniciens")
    public ResponseEntity<ApiResponse<List<TechnicienResponse>>> getAllTechniciens() {
        List<TechnicienResponse> techniciens = equipeService.getAllTechniciens();
        return ResponseEntity.ok(ApiResponse.success(techniciens));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Profil d'un technicien")
    public ResponseEntity<ApiResponse<TechnicienResponse>> getTechnicienById(@PathVariable Long id) {
        TechnicienResponse technicien = equipeService.getTechnicienById(id);
        return ResponseEntity.ok(ApiResponse.success(technicien));
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Techniciens disponibles")
    public ResponseEntity<ApiResponse<List<TechnicienResponse>>> getTechniciensDisponibles() {
        List<TechnicienResponse> techniciens = equipeService.getTechniciensDisponibles();
        return ResponseEntity.ok(ApiResponse.success(techniciens));
    }

    @GetMapping("/competence/{competence}")
    @Operation(summary = "Techniciens par compétence")
    public ResponseEntity<ApiResponse<List<TechnicienResponse>>> getTechniciensByCompetence(
            @PathVariable String competence) {
        List<TechnicienResponse> techniciens = equipeService.getTechniciensByCompetence(competence);
        return ResponseEntity.ok(ApiResponse.success(techniciens));
    }

    @PostMapping
    @Operation(summary = "Créer un profil technicien")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE', 'SUPERVISEUR')")
    public ResponseEntity<ApiResponse<TechnicienResponse>> createTechnicien(
            @Valid @RequestBody TechnicienRequest request) {
        TechnicienResponse technicien = equipeService.createTechnicien(request);
        return ResponseEntity.ok(ApiResponse.success("Technicien créé avec succès", technicien));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un technicien")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE', 'SUPERVISEUR')")
    public ResponseEntity<ApiResponse<TechnicienResponse>> updateTechnicien(
            @PathVariable Long id,
            @Valid @RequestBody TechnicienRequest request) {
        TechnicienResponse technicien = equipeService.updateTechnicien(id, request);
        return ResponseEntity.ok(ApiResponse.success("Technicien mis à jour", technicien));
    }

    @PutMapping("/{id}/disponibilite")
    @Operation(summary = "Changer la disponibilité")
    public ResponseEntity<ApiResponse<TechnicienResponse>> updateDisponibilite(
            @PathVariable Long id,
            @RequestParam Boolean disponible) {
        TechnicienResponse technicien = equipeService.updateDisponibilite(id, disponible);
        return ResponseEntity.ok(ApiResponse.success("Disponibilité mise à jour", technicien));
    }

    @PutMapping("/{id}/astreinte")
    @Operation(summary = "Changer le statut d'astreinte")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE', 'SUPERVISEUR')")
    public ResponseEntity<ApiResponse<TechnicienResponse>> updateAstreinte(
            @PathVariable Long id,
            @RequestParam Boolean enAstreinte) {
        TechnicienResponse technicien = equipeService.updateAstreinte(id, enAstreinte);
        return ResponseEntity.ok(ApiResponse.success("Astreinte mise à jour", technicien));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un technicien")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<Void>> deleteTechnicien(@PathVariable Long id) {
        equipeService.deleteTechnicien(id);
        return ResponseEntity.ok(ApiResponse.success("Technicien supprimé", null));
    }
}
