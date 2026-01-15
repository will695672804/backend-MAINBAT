package com.mainbat.controller;

import com.mainbat.dto.batiment.BatimentRequest;
import com.mainbat.dto.batiment.BatimentResponse;
import com.mainbat.dto.common.ApiResponse;
import com.mainbat.service.BatimentService;
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

@RestController
@RequestMapping("/batiments")
@RequiredArgsConstructor
@Tag(name = "Bâtiments", description = "Gestion des bâtiments")
@SecurityRequirement(name = "bearerAuth")
public class BatimentController {

    private final BatimentService batimentService;

    @GetMapping
    @Operation(summary = "Liste des bâtiments")
    public ResponseEntity<ApiResponse<Page<BatimentResponse>>> getAllBatiments(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<BatimentResponse> batiments;
        if (search != null && !search.isEmpty()) {
            batiments = batimentService.searchBatiments(search, pageable);
        } else {
            batiments = batimentService.getAllBatiments(pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(batiments));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un bâtiment")
    public ResponseEntity<ApiResponse<BatimentResponse>> getBatimentById(@PathVariable Long id) {
        BatimentResponse batiment = batimentService.getBatimentById(id);
        return ResponseEntity.ok(ApiResponse.success(batiment));
    }

    @PostMapping
    @Operation(summary = "Créer un bâtiment")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<BatimentResponse>> createBatiment(
            @Valid @RequestBody BatimentRequest request) {
        BatimentResponse batiment = batimentService.createBatiment(request);
        return ResponseEntity.ok(ApiResponse.success("Bâtiment créé avec succès", batiment));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un bâtiment")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<BatimentResponse>> updateBatiment(
            @PathVariable Long id,
            @Valid @RequestBody BatimentRequest request) {
        BatimentResponse batiment = batimentService.updateBatiment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Bâtiment mis à jour avec succès", batiment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un bâtiment")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE')")
    public ResponseEntity<ApiResponse<Void>> deleteBatiment(@PathVariable Long id) {
        batimentService.deleteBatiment(id);
        return ResponseEntity.ok(ApiResponse.success("Bâtiment supprimé avec succès", null));
    }
}
