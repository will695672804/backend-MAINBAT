package com.mainbat.controller;

import com.mainbat.dto.common.ApiResponse;
import com.mainbat.dto.equipe.EquipeRequest;
import com.mainbat.dto.equipe.EquipeResponse;
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
@RequestMapping("/equipes")
@RequiredArgsConstructor
@Tag(name = "Équipes", description = "Gestion des équipes techniques")
@SecurityRequirement(name = "bearerAuth")
public class EquipeController {

    private final EquipeService equipeService;

    @GetMapping
    @Operation(summary = "Liste des équipes")
    public ResponseEntity<ApiResponse<List<EquipeResponse>>> getAllEquipes() {
        List<EquipeResponse> equipes = equipeService.getAllEquipes();
        return ResponseEntity.ok(ApiResponse.success(equipes));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une équipe")
    public ResponseEntity<ApiResponse<EquipeResponse>> getEquipeById(@PathVariable Long id) {
        EquipeResponse equipe = equipeService.getEquipeById(id);
        return ResponseEntity.ok(ApiResponse.success(equipe));
    }

    @PostMapping
    @Operation(summary = "Créer une équipe")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<EquipeResponse>> createEquipe(
            @Valid @RequestBody EquipeRequest request) {
        EquipeResponse equipe = equipeService.createEquipe(request);
        return ResponseEntity.ok(ApiResponse.success("Équipe créée avec succès", equipe));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une équipe")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<EquipeResponse>> updateEquipe(
            @PathVariable Long id,
            @Valid @RequestBody EquipeRequest request) {
        EquipeResponse equipe = equipeService.updateEquipe(id, request);
        return ResponseEntity.ok(ApiResponse.success("Équipe mise à jour", equipe));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une équipe")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<Void>> deleteEquipe(@PathVariable Long id) {
        equipeService.deleteEquipe(id);
        return ResponseEntity.ok(ApiResponse.success("Équipe supprimée", null));
    }

    @GetMapping("/{id}/techniciens")
    @Operation(summary = "Techniciens d'une équipe")
    public ResponseEntity<ApiResponse<List<TechnicienResponse>>> getTechniciensByEquipe(
            @PathVariable Long id) {
        List<TechnicienResponse> techniciens = equipeService.getTechniciensByEquipe(id);
        return ResponseEntity.ok(ApiResponse.success(techniciens));
    }
}
